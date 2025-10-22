package com.example.simpletransferservice.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @NotNull
    private String fullName;
    @NotNull
    private String documentNumber;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String userType;
    @NotNull
    @Positive
    private BigDecimal initialBalance;
}
