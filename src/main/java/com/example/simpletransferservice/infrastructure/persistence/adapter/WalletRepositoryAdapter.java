package com.example.simpletransferservice.infrastructure.persistence.adapter;

import com.example.simpletransferservice.application.port.out.WalletRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.infrastructure.persistence.repository.WalletJpaRepository;
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
    public Optional<Wallet> findById(Long id) {
        return walletJpaRepository.findById(id)
                .map(domainMapper::toDomain);
    }
}
