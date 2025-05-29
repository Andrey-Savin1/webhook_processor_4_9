package ru.savin.webhook_processor_4_9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.webhook_processor_4_9.model.PaymentProviderCallback;

import java.util.UUID;

public interface PaymentProviderCallbackRepository extends JpaRepository<PaymentProviderCallback, UUID> {

    PaymentProviderCallback findByProviderTransactionUid(String uid);
}
