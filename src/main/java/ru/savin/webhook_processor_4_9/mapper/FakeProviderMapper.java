package ru.savin.webhook_processor_4_9.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.savin.webhook_processor_4_9.config.ObjectMapperConfig;
import ru.savin.webhook_processor_4_9.dto.FakeProviderDto;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;

/**
 * Реализация {@link WebhookMapper} для провайдера "FakePay".
 * <p>
 * Отвечает за:
 * Проверку, поддерживает ли данный маппер провайдера ("FakePay")
 * Десериализацию JSON-тела вебхука в DTO, специфичное для FakePay ({@link FakeProviderDto})
 * Конвертацию в общий формат {@link ProcessDto} через MapStruct
 */
@Component
@RequiredArgsConstructor
public class FakeProviderMapper implements WebhookMapper {

    private final ObjectMapperConfig objectMapperConfig;
    private final WebhookMapstructMapper mapstructMapper;


    /**
     * Проверяет, поддерживает ли этот маппер указанного провайдера.
     *
     * @param providerName имя провайдера из заголовка запроса
     * @return true, если провайдер — "FakePay", иначе false
     */
    @Override
    public boolean supports(String providerName) {
        return "FakePay".equals(providerName);
    }

    /**
     * Преобразует JSON-строку во внутренний формат {@link ProcessDto}.
     * <p>
     * Алгоритм работы:
     * С помощью ObjectMapper парсится JSON в {@link FakeProviderDto}
     * Через MapStruct происходит маппинг в общий формат {@link ProcessDto}
     *
     * @param body JSON-строка с данными вебхука от провайдера FakePay
     * @throws IllegalArgumentException если не удалось распарсить или замаппить данные
     */
    @Override
    public ProcessDto map(String body) {
        ProcessDto processDto;
        FakeProviderDto fakeProviderDto;
        try {
            fakeProviderDto = objectMapperConfig.objectMapper().readValue(body, FakeProviderDto.class);
            processDto = mapstructMapper.map(fakeProviderDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse or map the webhook transaction data for FakeProviderDto", e);
        }
        return processDto;
    }
}
