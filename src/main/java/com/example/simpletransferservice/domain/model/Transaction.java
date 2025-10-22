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
    @Builder.Default
    private TransactionStatus status =TransactionStatus.PENDING;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void transitionTo(TransactionStatus newStatus) {
        if (status.canTransitionTo(newStatus)) {
            failureReason = String.format("Invalid status transition from %s to %s", status, newStatus);
            throw new IllegalStateException(failureReason);
        }

        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void transitionWithFailureReason(TransactionStatus newStatus, String reason) {
        if (status.canTransitionTo(newStatus)) {
            failureReason = String.format("Invalid status transition from %s to %s", status, newStatus);
            throw new IllegalStateException(failureReason);
        }

        this.status = newStatus;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();
    }
}
