package ru.savin.webhook_processor_4_9.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;
import ru.savin.webhook_processor_4_9.feign.TransactionServiceClient;


/**
 * Сервис для отправки данных в TransactionService.
 * <p>
 * Используется для передачи обработанных вебхуков во внешний сервис.
 * Поддерживает автоматические повторные попытки при неудаче благодаря Resilience4j.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookSenderService {

    private final TransactionServiceClient transactionServiceClient;


    /**
     * Отправляет данные транзакции через Feign-клиент в TransactionService.
     * <p>
     * Метод помечен аннотацией {@link Retry}, что позволяет автоматически повторять
     * запрос при временных ошибках (например, сетевые проблемы, ошибки 5xx).
     *
     * @param processDto DTO с данными транзакции, которую нужно отправить
     */
    @Retry(name = "transactionService")
    public void doSend(ProcessDto processDto) {
        log.debug("Отправляемые данные в TransactionService: {}", processDto);
        transactionServiceClient.sendWebhook(processDto);
    }

}
