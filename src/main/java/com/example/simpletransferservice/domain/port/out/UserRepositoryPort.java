package com.example.simpletransferservice.domain.port.out;

import com.example.simpletransferservice.domain.model.User;

import java.util.Optional;

//TODO if implemetns user controller, add more methods
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
}
