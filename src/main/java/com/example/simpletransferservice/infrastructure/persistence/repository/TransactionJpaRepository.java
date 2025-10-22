package com.example.simpletransferservice.infrastructure.persistence.repository;

import com.example.simpletransferservice.domain.model.Transaction;
import com.example.simpletransferservice.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity,Long> {
}
