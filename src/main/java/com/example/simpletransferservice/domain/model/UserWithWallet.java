package com.example.simpletransferservice.domain.model;

import lombok.Builder;

@Builder
public record UserWithWallet(User user, Wallet wallet) {}
