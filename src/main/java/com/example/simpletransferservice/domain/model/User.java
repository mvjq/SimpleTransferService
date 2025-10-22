package com.example.simpletransferservice.domain.model;

import com.example.simpletransferservice.domain.enums.UserType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String fullName;
    private String email;
    private String document;
    private String password;
    private UserType userType;
    private Long walletId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean canTransfer() {
        return this.userType == UserType.CUSTOMER;
    }
}
