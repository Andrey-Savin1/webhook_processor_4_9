package ru.savin.webhook_processor_4_9.mapper;

import org.mapstruct.Mapper;
import ru.savin.webhook_processor_4_9.dto.FakeProviderDto;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;

/**
 * MapStruct-интерфейс для маппинга между DTO разных провайдеров и общей DTO.
 */
@Mapper(componentModel = "spring")
public interface WebhookMapstructMapper {

    ProcessDto map(FakeProviderDto fakeProviderDto);

}
