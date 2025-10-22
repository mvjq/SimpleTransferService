package com.example.simpletransferservice.application.port.out;

import com.example.simpletransferservice.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);
    Optional<Wallet> findByUserId(Long id);
}
