package com.example.simpletransferservice.domain.port.out;

import com.example.simpletransferservice.domain.model.Transaction;

import java.util.Optional;

//TODO if implement crud for transaction, add more method
public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long id);
}
