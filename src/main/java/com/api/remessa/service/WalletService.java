package com.api.remessa.service;

import com.api.remessa.dto.request.UpdateWalletRequest;
import com.api.remessa.dto.response.WalletResponse;
import com.api.remessa.exception.ResourceNotFoundException;
import com.api.remessa.model.Wallet;
import com.api.remessa.persistence.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletResponse findByUserId(Long userId) {

        Wallet wallet = findWallet(userId);
        return toResponse(wallet);
    }

    @Transactional
    public WalletResponse update(Long userId, UpdateWalletRequest request){

        Wallet wallet = findWallet(userId);

        if (request.balanceBrl() != null) {
            wallet.setBalanceBrl(request.balanceBrl());
        }

        if (request.balanceUsd() != null) {
            wallet.setBalanceUsd(request.balanceUsd());
        }

        return toResponse(wallet);
    }

    private Wallet findWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    private WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getUser().getId(),
                wallet.getBalanceBrl(),
                wallet.getBalanceUsd()
        );
    }
}
