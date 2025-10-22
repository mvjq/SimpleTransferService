package com.example.simpletransferservice.application.command;


import com.example.simpletransferservice.domain.enums.UserType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionIdJdbcTypeCode;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCommand {
    @NotNull
    private String fullName;
    @NotNull
    private String documentNumber;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private UserType userType;
    @NotNull
    @Positive
    private BigDecimal initialBalance;
}
