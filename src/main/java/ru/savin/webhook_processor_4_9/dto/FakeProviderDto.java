package ru.savin.webhook_processor_4_9.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeProviderDto {

    private String transactionId;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String message;
    private String currency;
    private String paymentMethod;
}
