package com.example.simpletransferservice.infrastructure.persistence.adapter;

import com.example.simpletransferservice.application.port.out.TransactionRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.model.Transaction;
import com.example.simpletransferservice.infrastructure.persistence.repository.TransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter  implements TransactionRepositoryPort {

    private final DomainMapper domainMapper;
    private final TransactionJpaRepository transactionJpaRepository;

    @Override
    public Transaction save(Transaction transaction) {
        var entity = domainMapper.toEntity(transaction);
        var saved = transactionJpaRepository.save(entity);
        return domainMapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionJpaRepository.findById(id).map(domainMapper::toDomain);
    }
}
