package ru.savin.webhook_processor_4_9.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;


/**
 * Интерфейс для работы с TransactionService
 */
@FeignClient(name = "transactionService", url = "${app.http.client.transaction-service}")
public interface TransactionServiceClient {


    @PostMapping(value = "/api/v1/transactions/process", produces = MediaType.APPLICATION_JSON_VALUE)
    void sendWebhook(@RequestBody ProcessDto topUpRequestDto);

}