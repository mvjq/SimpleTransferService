package com.example.simpletransferservice.domain;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.domain.enums.TransactionStatus;
import com.example.simpletransferservice.domain.enums.UserType;
import com.example.simpletransferservice.domain.model.Transaction;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.infrastructure.persistence.entity.TransactionEntity;
import com.example.simpletransferservice.infrastructure.persistence.entity.UserEntity;
import com.example.simpletransferservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class DomainMapper {

    public DomainMapper() {}

    public Transaction toDomain(TransferCommand command) {
        return Transaction.builder()
                .payerId(command.getPayerId())
                .payeeId(command.getPayeeId())
                .amount(command.getAmount())
                .build();
    }

    public Transaction toDomain(TransactionEntity transactionEntity) {
        return Transaction.builder()
                .id(transactionEntity.getId())
                .payerId(transactionEntity.getPayerId())
                .payeeId(transactionEntity.getPayeeId())
                .amount(transactionEntity.getAmount())
                .status(TransactionStatus.valueOf(transactionEntity.getStatus()))
                .failureReason(transactionEntity.getFailureReason())
                .createdAt(transactionEntity.getCreatedAt())
                .updatedAt(transactionEntity.getUpdatedAt())
                .version(transactionEntity.getVersion())
                .build();
    }

    public TransactionEntity toEntity(Transaction domainTransaction) {
        return TransactionEntity.builder()
                .id(domainTransaction.getId())
                .payerId(domainTransaction.getPayerId())
                .payeeId(domainTransaction.getPayeeId())
                .amount(domainTransaction.getAmount())
                .status(domainTransaction.getStatus().toString())
                .failureReason(domainTransaction.getFailureReason())
                .createdAt(domainTransaction.getCreatedAt())
                .updatedAt(domainTransaction.getUpdatedAt())
                .version(domainTransaction.getVersion())
                .build();
    }

    public Wallet toDomain(WalletEntity walletEntity) {
        return Wallet.builder()
                .id(walletEntity.getId())
                .userId(walletEntity.getUserId())
                .balance(walletEntity.getBalance())
                .version(walletEntity.getVersion())
                .createdAt(walletEntity.getCreatedAt())
                .updatedAt(walletEntity.getUpdatedAt())
                .build();
    }

    public WalletEntity toEntity(Wallet domainWallet) {
        return WalletEntity.builder()
                .id(domainWallet.getId())
                .userId(domainWallet.getUserId())
                .balance(domainWallet.getBalance())
                .version(domainWallet.getVersion())
                .createdAt(domainWallet.getCreatedAt())
                .updatedAt(domainWallet.getUpdatedAt())
                .build();
    }

    public User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .document(userEntity.getDocument())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .userType(UserType.valueOf(userEntity.getUserType()))
                .walletId(userEntity.getWalledId())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public UserEntity toEntity(User domainUser) {
        if (domainUser == null) return null;
        return UserEntity.builder()
                .id(domainUser.getId())
                .fullName(domainUser.getFullName())
                .document(domainUser.getDocument())
                .email(domainUser.getEmail())
                .password(domainUser.getPassword())
                .userType(domainUser.getUserType().toString())
                .walledId(domainUser.getWalletId())
                .createdAt(domainUser.getCreatedAt())
                .updatedAt(domainUser.getUpdatedAt())
                .build();
    }

}
