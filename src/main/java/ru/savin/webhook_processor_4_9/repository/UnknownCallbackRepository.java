package ru.savin.webhook_processor_4_9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.webhook_processor_4_9.model.UnknownCallback;

import java.util.UUID;

public interface UnknownCallbackRepository extends JpaRepository<UnknownCallback, UUID> {
}
