package ru.savin.webhook_processor_4_9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.webhook_processor_4_9.model.ProviderAuthData;

import java.util.Optional;

public interface ProviderAuthDataRepository extends JpaRepository<ProviderAuthData, Integer> {

    Optional<ProviderAuthData> findByProviderName(String providerName);

}
