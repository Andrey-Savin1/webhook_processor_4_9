package ru.savin.webhook_processor_4_9.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;
import ru.savin.webhook_processor_4_9.exception.UnauthorizedAccessException;
import ru.savin.webhook_processor_4_9.mapper.WebhookMapper;
import ru.savin.webhook_processor_4_9.model.PaymentProviderCallback;
import ru.savin.webhook_processor_4_9.model.SendStatus;
import ru.savin.webhook_processor_4_9.model.UnknownCallback;
import ru.savin.webhook_processor_4_9.repository.PaymentProviderCallbackRepository;
import ru.savin.webhook_processor_4_9.repository.UnknownCallbackRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для обработки входящих вебхуков от провайдеров.
 *
 * <p>Основные задачи:</p>
 * <ul>
 *     <li>Аутентификация отправителя</li>
 *     <li>Маппинг данных из JSON в DTO</li>
 *     <li>Сохранение данных во временные сущности</li>
 *     <li>Отправка данных в TransactionService</li>
 * </ul>
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final UnknownCallbackRepository unknownCallbackRepository;
    private final PaymentProviderCallbackRepository paymentProviderCallbackRepository;
    private final WebhookSenderService webhookSenderService;
    private final WebhookAuthenticationService authService;
    private final List<WebhookMapper> mappers;

    /**
     * Основной метод обработки вебхука.
     * Выполняет следующие шаги:
     * Проверяет аутентификацию провайдера.
     * Находит соответствующий маппер по имени провайдера.
     * Преобразует JSON-тело запроса в общую DTO
     * Если статус транзакции SUCCESS или FAILED — сохраняет и отправляет данные.
     *
     * @param body       JSON-строка с данными вебхука
     * @param authHeader Заголовок авторизации (Basic Auth)
     * @throws UnauthorizedAccessException если провайдер не авторизован
     */
    public void handleWebhook(String body, String authHeader) {
        log.debug("Входящий webhook: {}", body);

        // 1. Аутентификация провайдера
        Optional<String> providerOptName = authService.authenticate(authHeader);
        if (providerOptName.isEmpty()) {
            log.warn("Неавторизованный доступ");
            saveUnknownCallback(body);
            throw new UnauthorizedAccessException("Нет прав доступа у данного провайдера");
        }

        // 2. Получили название провайдера
        var providerName = providerOptName.get();

        // 3. Ищем нужный маппер по названию провайдера, сразу мапим body и получаем итоговую дто
        var processDto = getMapperForProvider(providerName).map(body);

        // 4. Проверяем статус транзакции
        if (SendStatus.SUCCESS.name().equals(processDto.getStatus()) || SendStatus.FAILED.name().equals(processDto.getStatus())) {
            // 5. Сохраняем webhook в статусе IN_PROGRESS
            var providerCallback = savePaymentProviderCallbacks(processDto);
            // 6. Отправляем webhook в TransactionService
            sendWebhook(processDto, providerCallback);

        }
    }

    /**
     * Отправляет данные транзакции в TransactionService.
     * <p>
     * Если отправка завершилась ошибкой, статус внешней отправки меняется на FAILED.
     *
     * @param processDto       DTO с данными транзакции
     * @param providerCallback Сущность, связанная с текущей транзакцией
     */
    public void sendWebhook(ProcessDto processDto, PaymentProviderCallback providerCallback) {
        try {
            webhookSenderService.doSend(processDto);
            providerCallback.setExternalServiceSendStatus(SendStatus.SUCCESS.name());
            log.debug("Webhook успешно отправлен");
        } catch (Exception e) {
            log.error("Не удалось отправить вебхук после 3х попыток", e);
            providerCallback.setExternalServiceSendStatus(SendStatus.FAILED.name());
        } finally {
            paymentProviderCallbackRepository.save(providerCallback);
        }
    }

    /**
     * Возвращает маппер для указанного провайдера.
     *
     * @param providerName имя провайдера
     * @throws IllegalArgumentException если подходящего маппера нет
     */
    public WebhookMapper getMapperForProvider(String providerName) {
        return mappers.stream()
                .filter(mapper -> mapper.supports(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + providerName));
    }

    /**
     * Сохраняет вебхук как неизвестный (неудачный маппинг/обработка).
     *
     * @param body JSON-тело вебхука
     */
    public UnknownCallback saveUnknownCallback(String body) {
        return unknownCallbackRepository.save(UnknownCallback.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .body(body)
                .build());
    }

    /**
     * Сохраняет данные о транзакции до отправки в TransactionService.
     *
     * @param webhookBody DTO с данными транзакции
     */
    public PaymentProviderCallback savePaymentProviderCallbacks(ProcessDto webhookBody) {
        var callback = paymentProviderCallbackRepository.save(PaymentProviderCallback.builder()
                .type(webhookBody.getPaymentMethod())
                .providerTransactionUid(webhookBody.getTransactionId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .body("test")
                .transactionStatus(webhookBody.getStatus())
                .provider(webhookBody.getMessage())
                .externalServiceSendStatus(SendStatus.IN_PROGRESS.name())
                .build());
        log.debug("Сохранение входящего webhook");
        return callback;
    }


}
