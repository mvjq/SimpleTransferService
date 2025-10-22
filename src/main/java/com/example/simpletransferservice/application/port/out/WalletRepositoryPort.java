package com.example.simpletransferservice.application.port.out;

import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.infrastructure.persistence.entity.WalletEntity;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);
    Optional<Wallet> findByUserId(Long id);
    Optional<Wallet> findByUserIdWithPessimisticLocking(Long id);
}
