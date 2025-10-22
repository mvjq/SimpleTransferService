package com.example.simpletransferservice.infrastructure.persistence.repository;

import com.example.simpletransferservice.infrastructure.persistence.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<WalletEntity,Long> {
    Optional<WalletEntity> findByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.userId = :userId")
    Optional<WalletEntity> findByUserIdForUpdate(Long userId);
}
