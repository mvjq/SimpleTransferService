package com.example.simpletransferservice.domain.port.out;

import com.example.simpletransferservice.domain.model.Wallet;

import java.util.Optional;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);
    Optional<Wallet> findByUserId(Long id);
    Optional<Wallet> findByUserIdWithPessimisticLocking(Long id);
}
