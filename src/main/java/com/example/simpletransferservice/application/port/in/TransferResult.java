package com.example.simpletransferservice.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// TODO implement result
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResult {
    private Long transactionId;
    private Long payerId;
    private Long payeeId;
    private BigDecimal amount;
    private String status;
    private String message;
}
