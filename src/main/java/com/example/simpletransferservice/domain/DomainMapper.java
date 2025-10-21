package com.example.simpletransferservice.domain;

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

    public Transaction toDomain(TransactionEntity transactionEntity) {
        return null;
    }
    public TransactionEntity toEntity(Transaction domainTransaction) {
        return null;
    }

    public Wallet toDomain(WalletEntity walletEntity) {
        return null;
    }
    public WalletEntity toEntity(Wallet domainWallet) {
        return null;
    }

    public UserEntity toEntity(User domainUser) {
        return null;
    }

    public User toDomain(UserEntity userEntity) {
        return null;
    }
}
