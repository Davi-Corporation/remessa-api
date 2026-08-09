package com.api.remessa.service;

import com.api.remessa.dto.request.CreateTransferRequest;
import com.api.remessa.dto.response.TransferResponse;
import com.api.remessa.enums.PersonType;
import com.api.remessa.exception.DailyLimitExceededException;
import com.api.remessa.exception.DuplicateResourceException;
import com.api.remessa.exception.InsufficientBalanceException;
import com.api.remessa.model.ExchangeRate;
import com.api.remessa.model.Transfer;
import com.api.remessa.model.User;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private UserService userService;

    @Mock
    private WalletService walletService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private TransferValidator transferValidator;

    @InjectMocks
    private TransferService transferService;


    @Test
    void shouldTransferSuccessfully() {

        CreateTransferRequest request = new CreateTransferRequest(1L,2L,new BigDecimal("100.00"));

        User sender = new User();
        sender.setId(1L);
        sender.setPersonType(PersonType.PF);

        User receiver = new User();
        receiver.setId(2L);
        receiver.setPersonType(PersonType.PF);

        Wallet senderWallet = new Wallet();
        senderWallet.setBalanceBrl(new BigDecimal("1000.00"));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setBalanceUsd(new BigDecimal("10.00"));

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBuyRate(new BigDecimal("5.0902"));

        Transfer savedTransfer = new Transfer();
        savedTransfer.setId(1L);
        savedTransfer.setSender(sender);
        savedTransfer.setReceiver(receiver);
        savedTransfer.setAmountBrl(new BigDecimal("100.00"));
        savedTransfer.setAmountUsd(new BigDecimal("19.65"));
        savedTransfer.setExchangeRate(new BigDecimal("5.0902"));
        savedTransfer.setCreatedAt(LocalDateTime.now());

        when(userService.findById(1L)).thenReturn(sender);

        when(userService.findById(2L)).thenReturn(receiver);

        when(walletService.findWallet(1L)).thenReturn(senderWallet);

        when(walletService.findWallet(2L)).thenReturn(receiverWallet);

        when(exchangeRateService.getLatestRate()).thenReturn(exchangeRate);

        when(transferRepository.save(any(Transfer.class))).thenReturn(savedTransfer);

        TransferResponse response = transferService.transfer(request);

        assertEquals(1L, response.id());
        assertEquals(1L, response.senderId());
        assertEquals(2L, response.receiverId());
        assertEquals(new BigDecimal("100.00"),response.amountBrl());
        assertEquals(new BigDecimal("19.65"),response.amountUsd());
        assertEquals(new BigDecimal("5.0902"),response.exchangeRate());

        verify(transferValidator).validateDailyLimit(sender, request.amountBrl());

        verify(walletService).debitBrl(senderWallet, request.amountBrl());

        verify(walletService).creditUsd(receiverWallet, new BigDecimal("19.65"));

        verify(exchangeRateService).getLatestRate();

        verify(transferRepository).save(any(Transfer.class));
    }


    @Test
    void shouldThrowExceptionWhenSenderAndReceiverAreTheSame() {

        CreateTransferRequest request = new CreateTransferRequest(1L,1L,new BigDecimal("100.00"));

        User user = new User();
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);

        assertThrows(DuplicateResourceException.class, () -> transferService.transfer(request));

        verify(userService, times(2)).findById(1L);

        verifyNoInteractions(transferValidator,walletService,exchangeRateService,transferRepository);
    }


    @Test
    void shouldNotTransferWhenDailyLimitIsExceeded() {

        CreateTransferRequest request = new CreateTransferRequest(1L,2L,new BigDecimal("100.00"));

        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        when(userService.findById(1L)).thenReturn(sender);

        when(userService.findById(2L)).thenReturn(receiver);

        doThrow(new DailyLimitExceededException("Daily transaction limit exceeded")).when(transferValidator).validateDailyLimit(sender, request.amountBrl());

        assertThrows(DailyLimitExceededException.class,() -> transferService.transfer(request));

        verify(transferValidator).validateDailyLimit(sender, request.amountBrl());

        verifyNoInteractions(walletService,exchangeRateService,transferRepository);
    }


    @Test
    void shouldNotSaveTransferWhenBalanceIsInsufficient() {

        CreateTransferRequest request = new CreateTransferRequest(1L,2L,new BigDecimal("100.00"));

        User sender = new User();
        sender.setId(1L);

        User receiver = new User();
        receiver.setId(2L);

        Wallet senderWallet = new Wallet();
        senderWallet.setBalanceBrl(new BigDecimal("50.00"));

        Wallet receiverWallet = new Wallet();

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBuyRate(new BigDecimal("5.0902"));

        when(userService.findById(1L)).thenReturn(sender);

        when(userService.findById(2L)).thenReturn(receiver);

        when(walletService.findWallet(1L)).thenReturn(senderWallet);

        when(walletService.findWallet(2L)).thenReturn(receiverWallet);

        when(exchangeRateService.getLatestRate()).thenReturn(exchangeRate);

        doThrow(new InsufficientBalanceException("Insufficient balance")).when(walletService).debitBrl(senderWallet, request.amountBrl());

        assertThrows(InsufficientBalanceException.class,() -> transferService.transfer(request));

        verify(walletService).debitBrl(senderWallet, request.amountBrl());

        verify(walletService, never()).creditUsd(any(Wallet.class), any(BigDecimal.class));

        verify(transferRepository, never()).save(any(Transfer.class));
    }
}