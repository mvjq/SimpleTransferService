package com.example.simpletransferservice.application.port.out;

import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.UserWithWallet;

import java.util.Optional;

//TODO if implemetns user controller, add more methods
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<UserWithWallet> findUserWithWalletById(Long id);
}
