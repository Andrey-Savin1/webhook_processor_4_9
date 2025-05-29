package ru.savin.webhook_processor_4_9.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequestDto {

    private String paymentMethod;
    private String provider;
    private Long userUid;
    private Long walletUid;
    private BigDecimal amount;
    private String comment;
    private String currency;
    private String paymentStatus;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
