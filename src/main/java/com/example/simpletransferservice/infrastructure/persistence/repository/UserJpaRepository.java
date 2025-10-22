package com.example.simpletransferservice.infrastructure.persistence.repository;

import com.example.simpletransferservice.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmailOrDocument(String email, String document);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByDocument(String document);
}
