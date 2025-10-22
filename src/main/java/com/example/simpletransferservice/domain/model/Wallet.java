package com.example.simpletransferservice.domain.model;

import com.example.simpletransferservice.domain.exception.InsufficientBalanceException;
import jakarta.validation.constraints.*;
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
public class Wallet {
    private Long id;
    private Long userId;
    @PositiveOrZero
    @DecimalMin(value = "0.00")
    private BigDecimal balance;
    private Long version;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    public Wallet debit(BigDecimal value) {
        BigDecimal newBalance = balance.subtract(value);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
             throw new InsufficientBalanceException("Insufficient funds in wallet");
        }

        return Wallet.builder()
                .id(this.id)
                .userId(this.userId)
                .balance(newBalance)
                .version(this.version)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Wallet credit(BigDecimal value) {

        if (value == null || value.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new IllegalArgumentException("Credit value must be positive");
        }

        BigDecimal newBalance = balance.add(value);

        return Wallet.builder()
                .id(this.id)
                .userId(this.userId)
                .balance(newBalance)
                .version(this.version)
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }


}
