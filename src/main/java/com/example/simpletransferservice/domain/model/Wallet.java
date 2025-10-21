package com.example.simpletransferservice.domain.model;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
