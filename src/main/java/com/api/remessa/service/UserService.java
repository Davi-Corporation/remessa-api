package com.api.remessa.service;

import com.api.remessa.dto.request.CreateUserRequest;
import com.api.remessa.dto.response.UserResponse;
import com.api.remessa.exception.DuplicateResourceException;
import com.api.remessa.model.User;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.UserRepository;
import com.api.remessa.persistence.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public UserResponse create(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }

        if (userRepository.existsByCpfCnpj(request.cpfCnpj())) {
            throw new DuplicateResourceException("CPF/CNPJ already registered");
        }

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPersonType(request.personType());
        user.setCpfCnpj(request.cpfCnpj());

        User savedUser = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalanceBrl(BigDecimal.ZERO);
        wallet.setBalanceUsd(BigDecimal.ZERO);

        walletRepository.save(wallet);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPersonType(),
                savedUser.getCpfCnpj()
        );
    }
}
