package com.example.simpletransferservice.domain.port.out;

import com.example.simpletransferservice.domain.model.User;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    boolean existsByEmailOrDocument(@NotNull String email, @NotNull String documentNumber);
    Optional<User> findByEmail(@NotNull String email);
    Optional<User> findByDocument(@NotNull String documentNumber);
}
