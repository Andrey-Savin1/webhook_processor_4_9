package ru.savin.webhook_processor_4_9.mapper;

import ru.savin.webhook_processor_4_9.dto.ProcessDto;

public interface WebhookMapper {

    /**
     * Проверяем, может ли данный маппер обработать провайдера
     */
    boolean supports(String providerName);

    /**
     * Преобразование JSON-строку в общую DTO
     */
    ProcessDto map(String body);
}
