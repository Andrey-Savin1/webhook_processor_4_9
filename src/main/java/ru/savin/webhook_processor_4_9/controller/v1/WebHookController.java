package ru.savin.webhook_processor_4_9.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.savin.webhook_processor_4_9.service.WebhookService;


@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebHookController {

    private final WebhookService webhookService;

    /**
     * Обрабатывает входящий вебхук от провайдера.
     * <p>
     * Провайдер должен быть авторизован через Basic Auth.
     * В теле запроса ожидается JSON с данными транзакции.
     */
    @PostMapping()
    public ResponseEntity<Void> receiveWebhook(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @RequestBody String body) {

        webhookService.handleWebhook(body, authHeader);
        return ResponseEntity.noContent().build();
    }

}
