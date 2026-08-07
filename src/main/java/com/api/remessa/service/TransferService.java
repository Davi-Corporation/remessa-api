package com.api.remessa.service;

import com.api.remessa.dto.request.CreateTransferRequest;
import com.api.remessa.dto.response.TransferResponse;
import com.api.remessa.exception.BusinessException;
import com.api.remessa.model.Transfer;
import com.api.remessa.model.User;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        walletService.debit(senderWallet, request.amountBrl());
        walletService.credit(receiverWallet, request.amountBrl());

        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setAmountBrl(request.amountBrl());

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
