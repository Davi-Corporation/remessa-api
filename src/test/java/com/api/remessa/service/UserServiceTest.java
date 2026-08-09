package com.api.remessa.service;

import com.api.remessa.dto.request.CreateUserRequest;
import com.api.remessa.dto.response.UserResponse;
import com.api.remessa.enums.PersonType;
import com.api.remessa.exception.DuplicateResourceException;
import com.api.remessa.exception.UserNotFoundException;
import com.api.remessa.model.User;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.UserRepository;
import com.api.remessa.persistence.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserAndWallet() {

        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "joao@email.com",
                "123456",
                PersonType.PF,
                "12345678900"
        );

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFullName(request.fullName());
        savedUser.setEmail(request.email());
        savedUser.setPassword(request.password());
        savedUser.setPersonType(request.personType());
        savedUser.setCpfCnpj(request.cpfCnpj());

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        when(userRepository.existsByCpfCnpj(request.cpfCnpj())).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.create(request);

        assertEquals(1L, response.id());
        assertEquals("João Silva", response.fullName());
        assertEquals("joao@email.com", response.email());
        assertEquals(PersonType.PF, response.personType());
        assertEquals("12345678900", response.cpfCnpj());

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).existsByCpfCnpj(request.cpfCnpj());
        verify(userRepository).save(any(User.class));
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "joao@email.com",
                "123456",
                PersonType.PF,
                "12345678900"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,() -> userService.create(request));

        verify(userRepository).existsByEmail(request.email());

        verify(userRepository, never()).existsByCpfCnpj(anyString());

        verify(userRepository, never()).save(any(User.class));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void shouldThrowExceptionWhenCpfCnpjAlreadyExists() {

        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "joao@email.com",
                "123456",
                PersonType.PF,
                "12345678900"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        when(userRepository.existsByCpfCnpj(request.cpfCnpj())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,() -> userService.create(request));

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).existsByCpfCnpj(request.cpfCnpj());

        verify(userRepository, never()).save(any(User.class));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void shouldFindUserById() {

        User user = new User();
        user.setId(1L);
        user.setFullName("João Silva");
        user.setEmail("joao@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertSame(user, result);

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> userService.findById(999L));

        verify(userRepository).findById(999L);
    }

    @Test
    void shouldCreateWalletWithZeroBalances() {

        CreateUserRequest request = new CreateUserRequest(
                "João Silva",
                "joao@email.com",
                "123456",
                PersonType.PF,
                "12345678900"
        );

        User savedUser = new User();
        savedUser.setId(1L);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);

        when(userRepository.existsByCpfCnpj(request.cpfCnpj())).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        userService.create(request);

        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);

        verify(walletRepository).save(walletCaptor.capture());

        Wallet wallet = walletCaptor.getValue();

        assertSame(savedUser, wallet.getUser());
        assertEquals(BigDecimal.ZERO, wallet.getBalanceBrl());
        assertEquals(BigDecimal.ZERO, wallet.getBalanceUsd());
    }
}