package com.example.simpletransferservice.infrastructure.persistence.adapter;

import com.example.simpletransferservice.application.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
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
}
