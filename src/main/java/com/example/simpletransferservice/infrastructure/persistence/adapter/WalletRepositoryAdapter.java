package com.example.simpletransferservice.infrastructure.persistence.adapter;

import com.example.simpletransferservice.domain.port.out.WalletRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.infrastructure.persistence.repository.WalletJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WalletRepositoryAdapter implements WalletRepositoryPort {

    private final DomainMapper domainMapper;
    private final WalletJpaRepository walletJpaRepository;

    @Override
    public Wallet save(Wallet wallet) {
        var entity = domainMapper.toEntity(wallet);
        var saved = walletJpaRepository.save(entity);
        return domainMapper.toDomain(saved);
    }

    @Override
    public Optional<Wallet> findByUserId(Long id) {
        return walletJpaRepository.findByUserId(id)
                .map(domainMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Wallet> findByUserIdWithPessimisticLocking(Long id) {
        return walletJpaRepository.findByUserIdForUpdate(id)
                .map(domainMapper::toDomain);
    }
}
