package ru.savin.webhook_processor_4_9.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "verification_callbacks")
public class VerificationCallback {
    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "body")
    private String body;

    @Column(name = "transaction_uid")
    private UUID transactionUid;

    @Column(name = "profile_uid")
    private UUID profileUid;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

}