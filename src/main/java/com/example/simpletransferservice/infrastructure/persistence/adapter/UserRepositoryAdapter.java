package com.example.simpletransferservice.infrastructure.persistence.adapter;

import com.example.simpletransferservice.application.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.UserWithWallet;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.infrastructure.persistence.entity.UserEntity;
import com.example.simpletransferservice.infrastructure.persistence.entity.WalletEntity;
import com.example.simpletransferservice.infrastructure.persistence.repository.UserJpaRepository;
import com.example.simpletransferservice.infrastructure.persistence.repository.WalletJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final WalletJpaRepository walletJpaRepository;
    private final DomainMapper domainMapper;

    @Override
    public User save(User user) {
        var entity = domainMapper.toEntity(user);
        var saved = userJpaRepository.save(entity);
        return domainMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(domainMapper::toDomain);
    }

    @Override
    public Optional<UserWithWallet> findUserWithWalletById(Long userId) {
        Optional<UserEntity> userEntity = userJpaRepository.findById(userId);
        if (userEntity.isEmpty()) {
            return Optional.empty();
        }

        Optional<WalletEntity> walletEntity = walletJpaRepository.findByUserId(userId);
        if (walletEntity.isEmpty()) {
            return Optional.empty();
        }

        User user = domainMapper.toDomain(userEntity.get());
        Wallet wallet = domainMapper.toDomain(walletEntity.get());

        return Optional.of(new UserWithWallet(user, wallet));
    }
}
