package com.api.remessa.service;

import com.api.remessa.dto.request.CreateTransferRequest;
import com.api.remessa.dto.response.TransferResponse;
import com.api.remessa.exception.BusinessException;
import com.api.remessa.model.ExchangeRate;
import com.api.remessa.model.Transfer;
import com.api.remessa.model.User;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final ExchangeRateService exchangeRateService;

    @Transactional
    public TransferResponse transfer(CreateTransferRequest request) {

        User sender = userService.findById(request.senderId());
        User receiver = userService.findById(request.receiverId());

        if (sender.getId().equals(receiver.getId())) {
            throw new BusinessException("Sender and receiver cannot be the same");
        }

        Wallet senderWallet = walletService.findWallet(sender.getId());
        Wallet receiverWallet = walletService.findWallet(receiver.getId());

        ExchangeRate exchangeRate = exchangeRateService.getLatestRate();

        BigDecimal amountUsd = request.amountBrl().divide(exchangeRate.getBuyRate(),2, RoundingMode.HALF_UP);

        walletService.debitBrl(senderWallet, request.amountBrl());
        walletService.creditUsd(receiverWallet, amountUsd);

        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setAmountBrl(request.amountBrl());
        transfer.setAmountUsd(amountUsd);
        transfer.setExchangeRate(exchangeRate.getBuyRate());

        return toResponse(transferRepository.save(transfer)
        );
    }

    private TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSender().getId(),
                transfer.getReceiver().getId(),
                transfer.getAmountBrl(),
                transfer.getAmountUsd(),
                transfer.getExchangeRate(),
                transfer.getCreatedAt()
        );
    }
}
