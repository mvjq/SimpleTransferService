package com.example.simpletransferservice.presentation.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    @NotNull
    @Positive
    private Long payer;
    @Positive
    @NotNull Long payee;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal value;
}
