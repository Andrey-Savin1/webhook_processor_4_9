package ru.savin.webhook_processor_4_9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.webhook_processor_4_9.model.VerificationCallback;

import java.util.UUID;

public interface VerificationCallbackRepository extends JpaRepository<VerificationCallback, UUID> {
}
