package ru.savin.webhook_processor_4_9.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.savin.webhook_processor_4_9.dto.ProcessDto;
import ru.savin.webhook_processor_4_9.model.PaymentProviderCallback;
import ru.savin.webhook_processor_4_9.model.UnknownCallback;
import ru.savin.webhook_processor_4_9.repository.PaymentProviderCallbackRepository;
import ru.savin.webhook_processor_4_9.repository.UnknownCallbackRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class RepositoryTest {

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private UnknownCallbackRepository unknownCallbackRepository;

    @Autowired
    private PaymentProviderCallbackRepository paymentProviderCallbackRepository;

    @Test
    void saveUnknownCallback_ShouldPersistToDatabase() {
        // Given
        String body = "{\"event\": \"test\"}";

        // When
       var res =  webhookService.saveUnknownCallback(body);

        // Then
        Optional<UnknownCallback> saved = unknownCallbackRepository.findById(res.getId());
        assertThat(saved).isPresent();
        assertThat(saved.get().getBody()).isEqualTo(body);
    }

    @Test
    void savePaymentProviderCallbacks_ShouldPersistToDatabase() {
        // Given
        ProcessDto dto = new ProcessDto("tx123", "SUCCESS", "top_up", "FakePay", "VISA");

        // When
        PaymentProviderCallback saved = webhookService.savePaymentProviderCallbacks(dto);

        // Then
        Optional<PaymentProviderCallback> found = paymentProviderCallbackRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getProviderTransactionUid()).isEqualTo("tx123");
        assertThat(found.get().getTransactionStatus()).isEqualTo("SUCCESS");
        assertThat(found.get().getType()).isEqualTo("VISA");
        assertThat(found.get().getProvider()).isEqualTo("top_up");
        assertThat(found.get().getExternalServiceSendStatus()).isEqualTo("IN_PROGRESS");
    }

}
