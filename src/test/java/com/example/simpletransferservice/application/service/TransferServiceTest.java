package com.example.simpletransferservice.application.service;

import com.example.simpletransferservice.application.command.TransferCommand;
import com.example.simpletransferservice.application.port.in.TransferResult;
import com.example.simpletransferservice.domain.DomainMapper;
import com.example.simpletransferservice.domain.enums.UserType;
import com.example.simpletransferservice.domain.event.TransactionCompletedEvent;
import com.example.simpletransferservice.domain.exception.InsufficientBalanceException;
import com.example.simpletransferservice.domain.exception.UserNotFoundException;
import com.example.simpletransferservice.domain.model.Transaction;
import com.example.simpletransferservice.domain.model.User;
import com.example.simpletransferservice.domain.model.Wallet;
import com.example.simpletransferservice.domain.port.out.AuthorizationPort;
import com.example.simpletransferservice.domain.port.out.TransactionRepositoryPort;
import com.example.simpletransferservice.domain.port.out.UserRepositoryPort;
import com.example.simpletransferservice.domain.port.out.WalletRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private WalletRepositoryPort walletRepository;

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private AuthorizationPort authorizationPort;

    @Mock
    private DomainMapper domainMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TransferService transferService;

    // mocks
    private User payer;
    private User payee;
    private Wallet payerWallet;
    private Wallet payeeWallet;
    private TransferCommand command;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        createMockObjects();
    }

    @Test
    @DisplayName("Should complete transfer successfully")
    public void shouldCompleteTransferSuccessfully() {
        when(domainMapper.toDomain(command)).thenReturn(transaction);
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(walletRepository.findByUserIdWithPessimisticLocking(1L)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserIdWithPessimisticLocking(2L)).thenReturn(Optional.of(payeeWallet));
        when(authorizationPort.authorize(command)).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferResult result = transferService.transfer(command);

        assertTrue(result.isSuccess());
        verify(eventPublisher, times(1)).publishEvent(any(TransactionCompletedEvent.class));
        verify(walletRepository, times(2)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("Should throw exception when payer and payee are the same")
    void shouldThrowExceptionWhenTransferringToSameUser() {
        command = TransferCommand.builder()
                .payerId(1L)
                .payeeId(1L) // Same user
                .value(new BigDecimal("150.00"))
                .build();

        assertThatThrownBy(() -> transferService.transfer(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be different");
    }

    @Test
    @DisplayName("Should thwo exception when payer does not exist")
    void shouldThrowExceptionWhenPayerNotFound() {
        when(domainMapper.toDomain(command)).thenReturn(transaction);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transferService.transfer(command))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Payer not found");
    }

    @Test
    @DisplayName("Should throw exception when merchant tries to transfer")
    void shouldThrowExceptionWhenMerchantTriesToTransfer() {
        User merchantPayer = User.builder()
                .id(1L)
                .fullName("Bingus Store")
                .userType(UserType.MERCHANT)
                .build();

        when(domainMapper.toDomain(command)).thenReturn(transaction);
        when(userRepository.findById(1L)).thenReturn(Optional.of(merchantPayer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        assertThatThrownBy(() -> transferService.transfer(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not allowed to make transfers");
    }

    @Test
    @DisplayName("Should throw exception when payer has insufficient balance")
    void shouldThrowExceptionWhenInsufficientBalance() {
        Wallet poorWallet = Wallet.builder()
                .userId(1L)
                .balance(new BigDecimal("50.00")) // Less than transfer amount
                .build();

        when(domainMapper.toDomain(command)).thenReturn(transaction);
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(walletRepository.findByUserIdWithPessimisticLocking(1L)).thenReturn(Optional.of(poorWallet));
        when(walletRepository.findByUserIdWithPessimisticLocking(2L)).thenReturn(Optional.of(payeeWallet));

        assertThatThrownBy(() -> transferService.transfer(command))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    @DisplayName("Should fail transfer when authorization is denied")
    void shouldFailTransferWhenAuthorizationDenied() {
        // Given
        when(domainMapper.toDomain(command)).thenReturn(transaction);
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(walletRepository.findByUserIdWithPessimisticLocking(1L)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUserIdWithPessimisticLocking(2L)).thenReturn(Optional.of(payeeWallet));
        when(authorizationPort.authorize(command)).thenReturn(false); // DENIED

        TransferResult result = transferService.transfer(command);

        assertThat(result.isSuccess()).isFalse();
        verify(walletRepository, never()).save(any(Wallet.class)); // No wallet updates
        verify(eventPublisher, never()).publishEvent(any()); // No event published
    }



    private void createMockObjects() {
        payer = User.builder()
                .id(1L)
                .fullName("Marcos Vinicius Junqueira")
                .email("mviniciusjunqueira.com")
                .userType(UserType.CUSTOMER)
                .build();

        payee = User.builder()
                .id(2L)
                .fullName("Vinicius Junqueira")
                .email("viniciusjunqueira@gmail.com.com")
                .userType(UserType.CUSTOMER)
                .build();

        payerWallet = Wallet.builder()
                .id(1L)
                .userId(1L)
                .balance(new BigDecimal("1000.00"))
                .build();

        payeeWallet = Wallet.builder()
                .id(2L)
                .userId(2L)
                .balance(new BigDecimal("500.00"))
                .build();

        command = TransferCommand.builder()
                .payerId(1L)
                .payeeId(2L)
                .value(new BigDecimal("150.00"))
                .build();

        transaction = Transaction.builder()
                .id(1L)
                .payerId(1L)
                .payeeId(2L)
                .amount(new BigDecimal("150.00"))
                .build();
    }
}