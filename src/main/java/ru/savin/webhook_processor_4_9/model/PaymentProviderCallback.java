package ru.savin.webhook_processor_4_9.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_provider_callbacks")
public class PaymentProviderCallback {

    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "body")
    private String body;

    @Column(name = "provider_transaction_uid")
    private String providerTransactionUid;

    @Column(name = "type")
    private String type;

    @Column(name = "provider")
    private String provider;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "external_service_send_status")
    private String externalServiceSendStatus;


}