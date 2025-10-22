package com.example.simpletransferservice.application.command;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferCommand {
    @NotNull
    private Long payerId;
    @NotNull
    private Long payeeId;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal value;
}
