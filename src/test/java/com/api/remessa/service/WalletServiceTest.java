package com.api.remessa.service;

import com.api.remessa.dto.request.UpdateWalletRequest;
import com.api.remessa.dto.response.WalletResponse;
import com.api.remessa.exception.InsufficientBalanceException;
import com.api.remessa.exception.ResourceNotFoundException;
import com.api.remessa.model.User;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void shouldFindWalletByUserId() {

        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalanceBrl(new BigDecimal("1000.00"));
        wallet.setBalanceUsd(new BigDecimal("200.00"));

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        WalletResponse response = walletService.findByUserId(1L);

        assertEquals(1L, response.userId());
        assertEquals(new BigDecimal("1000.00"), response.balanceBrl());
        assertEquals(new BigDecimal("200.00"), response.balanceUsd());

        verify(walletRepository).findByUserId(1L);
    }

    @Test
    void shouldThrowExceptionWhenWalletDoesNotExist() {

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> walletService.findByUserId(1L));

        verify(walletRepository).findByUserId(1L);
    }

    @Test
    void shouldUpdateBrlBalance() {

        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalanceBrl(new BigDecimal("1000.00"));
        wallet.setBalanceUsd(new BigDecimal("200.00"));

        UpdateWalletRequest request = new UpdateWalletRequest(new BigDecimal("1500.00"),null);

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        WalletResponse response = walletService.update(1L, request);

        assertEquals(new BigDecimal("1500.00"),response.balanceBrl());

        assertEquals(new BigDecimal("200.00"),response.balanceUsd());

        verify(walletRepository).findByUserId(1L);
    }

    @Test
    void shouldUpdateUsdBalance() {

        User user = new User();
        user.setId(1L);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalanceBrl(new BigDecimal("1000.00"));
        wallet.setBalanceUsd(new BigDecimal("200.00"));

        UpdateWalletRequest request = new UpdateWalletRequest(null,new BigDecimal("500.00"));

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        WalletResponse response = walletService.update(1L, request);

        assertEquals(new BigDecimal("1000.00"),response.balanceBrl());

        assertEquals(new BigDecimal("500.00"),response.balanceUsd());
    }

    @Test
    void shouldDebitBrl() {

        Wallet wallet = new Wallet();
        wallet.setBalanceBrl(new BigDecimal("1000.00"));

        walletService.debitBrl(wallet,new BigDecimal("300.00"));

        assertEquals(new BigDecimal("700.00"),wallet.getBalanceBrl());
    }

    @Test
    void shouldThrowExceptionWhenBrlBalanceIsInsufficient() {

        Wallet wallet = new Wallet();
        wallet.setBalanceBrl(new BigDecimal("100.00"));

        assertThrows(InsufficientBalanceException.class, () -> walletService.debitBrl(wallet,new BigDecimal("150.00")));

        assertEquals(new BigDecimal("100.00"),wallet.getBalanceBrl());
    }

    @Test
    void shouldCreditUsd() {

        Wallet wallet = new Wallet();
        wallet.setBalanceUsd(new BigDecimal("100.00"));

        walletService.creditUsd(wallet,new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"),wallet.getBalanceUsd());
    }

    @Test
    void shouldFindWallet() {

        Wallet wallet = new Wallet();

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.findWallet(1L);

        assertSame(wallet, result);

        verify(walletRepository).findByUserId(1L);
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistingWallet() {

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,() -> walletService.findWallet(1L));
    }
}