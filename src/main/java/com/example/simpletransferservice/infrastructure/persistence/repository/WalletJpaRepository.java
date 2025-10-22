package com.example.simpletransferservice.infrastructure.persistence.repository;

import com.example.simpletransferservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<WalletEntity,Long> {
    Optional<WalletEntity> findByUserId(long userId);
}
