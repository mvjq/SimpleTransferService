package com.example.simpletransferservice.domain.model;


import com.example.simpletransferservice.domain.enums.TransactionStatus;
import jakarta.validation.constraints.DecimalMin;
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
public class Transaction {
    private Long id;
    private Long version;
    private Long payerId;
    private Long payeeId;
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
    private TransactionStatus status;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
