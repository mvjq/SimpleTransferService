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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean canTransfer() {
        return this.userType == UserType.CUSTOMER;
    }

    public boolean isCustomer() {
        return this.userType == UserType.CUSTOMER;
    }

    public boolean isMerchant() {
        return this.userType == UserType.MERCHANT;
    }


}
