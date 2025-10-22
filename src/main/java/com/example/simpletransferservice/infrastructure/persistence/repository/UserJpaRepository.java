package com.example.simpletransferservice.infrastructure.persistence.repository;

import com.example.simpletransferservice.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {}
