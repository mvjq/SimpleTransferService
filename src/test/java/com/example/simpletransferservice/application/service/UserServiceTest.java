package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.UserCommand;
import com.example.simpletransferservice.application.port.in.UserResult;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.enums.UserType;
import com.example.simpletransferservice.domain.exception.UserAlreadyExistsException;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.domain.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.port.out.WalletRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private WalletRepositoryPort walletRepository;

    @Mock
    private DomainMapper domainMapper;

    @InjectMocks
    private UserService userService;

    private UserCommand userCommand;

    @BeforeEach
    void setUp() {
        userCommand = UserCommand.builder()
                .fullName("Marcos Vinicius Junqueira")
                .documentNumber("39293399865")
                .email("mviniciusjunqueira@gmail.com")
                .password("password123")
                .userType(UserType.CUSTOMER)
                .initialBalance(new BigDecimal("1000.00"))
                .build();
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when user already exists")
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(userRepository.existsByEmailOrDocument(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUserWithWallet(userCommand))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should throw exception when initial balance is negative")
    void shouldThrowExceptionWhenInitialBalanceIsNegative() {// Given
        userCommand.setInitialBalance(new BigDecimal("-100.00"));

        assertThatThrownBy(() -> userService.createUserWithWallet(userCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be negative");
    }

    @Test
    @DisplayName("Should create user with wallet successfully")
    void shouldCreateUserWithWalletSuccessfully() {

        User savedUser = getMockUser();
        Wallet savedWallet = getMockWallet();

        when(userRepository.existsByEmailOrDocument(any(), any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);

        UserResult result = userService.createUserWithWallet(userCommand);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("mviniciusjunqueira@gmail.com");
        assertThat(result.getBalance()).isEqualByComparingTo("1000.00");
        verify(userRepository, times(2)).save(any(User.class)); // save + update with walletId
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        User user = getMockUser();
        Wallet wallet = getMockWallet();

        when(userRepository.findByEmail("mviniciusjunqueira@gmail.com")).thenReturn(Optional.of(user));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        UserResult result = userService.getUserByEmail("mviniciusjunqueira@gmail.com");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("mviniciusjunqueira@gmail.com");
        assertThat(result.getBalance()).isEqualByComparingTo("1000.00");
    }


    @Test
    @DisplayName("Should find user by document number successfully")
    void shouldFindUserByDocumentSuccessfully() {
        // Given
        User user = getMockUser();

        Wallet wallet = getMockWallet();

        when(userRepository.findByDocument("39293399865")).thenReturn(Optional.of(user));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        UserResult result = userService.getUserByDocumentNumber("39293399865");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDocumentNumber()).isEqualTo("39293399865");
    }

    private static User getMockUser() {
        return User.builder()
                .id(1L)
                .fullName("Marcos Vinicius Junqueira")
                .email("mviniciusjunqueira@gmail.com")
                .document("39293399865")
                .userType(UserType.CUSTOMER)
                .build();
    }


    private static Wallet getMockWallet() {
        return Wallet.builder()
                .id(1L)
                .userId(1L)
                .balance(new BigDecimal("1000.00"))
                .build();
    }
}