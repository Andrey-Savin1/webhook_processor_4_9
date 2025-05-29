package ru.savin.webhook_processor_4_9.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessDto {

    private String transactionId;
    private String status;
    private String message;
    private String currency;
    private String paymentMethod;


}
