package com.example.simpletransferservice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {
    private long transactionId;
    private Long payerId;
    private Long payeeId;
    private BigDecimal value;
    private String status;
    private String message;
}
