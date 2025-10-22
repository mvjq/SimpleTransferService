package com.example.simpletransferservice.domain.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCompletedEvent {
    private Long transactionId;
    private Long payerId;
    private String payerName;
    private String payerEmail;
    private Long payeeId;
    private String payeeName;
    private String payeeEmail;
    private BigDecimal amount;
}
