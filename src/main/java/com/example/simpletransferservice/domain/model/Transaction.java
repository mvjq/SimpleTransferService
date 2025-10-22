package com.example.simpletransferservice.domain.model;

import com.example.simpletransferservice.domain.enums.TransactionStatus;
import com.example.simpletransferservice.domain.exception.IlegalTransitionStateException;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import java.time.LocalDateTime;

@Slf4j
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
    private TransactionStatus status = TransactionStatus.PENDING;
    private String failureReason;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;


    public void transitionTo(TransactionStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            failureReason = String.format("Invalid status transition from %s to %s", status, newStatus);
            throw new IlegalTransitionStateException(failureReason);
        }

        log.info("Transitioning transaction [{}] from status [{}] to [{}]", id, status, newStatus);

        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void transitionWithFailureReason(TransactionStatus newStatus, String reason) {
        if (!status.canTransitionTo(newStatus)) {
            failureReason = String.format("Invalid status transition from [%s] -> [%s]", status, newStatus);
            throw new IlegalTransitionStateException(failureReason);
        }

        log.info("Transitioning transaction [{}] from status [{}] to [{}] with failure reason {}", id, status,
                newStatus, failureReason);

        this.status = newStatus;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void failedAuthorization(String reason) {
        transitionWithFailureReason(TransactionStatus.AUTH_DENIED, reason);
    }

    public void failedValidation(String reason) {
        transitionWithFailureReason(TransactionStatus.FAILED, reason);
    }

    public void process() {
        transitionTo(TransactionStatus.PROCESSING);
    }

    public void failProcessing(String reason) {
        transitionWithFailureReason(TransactionStatus.FAILED, reason);
    }

    public void complete() {
        transitionTo(TransactionStatus.COMPLETED);
    }

    public void reverse(String reason) {
        transitionWithFailureReason(TransactionStatus.REVERSED, reason);
    }

    public boolean isCompleted() {
        return this.status == TransactionStatus.COMPLETED;
    }
}
