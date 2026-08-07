package com.api.remessa.controller;

import com.api.remessa.dto.request.UpdateWalletRequest;
import com.api.remessa.dto.response.WalletResponse;
import com.api.remessa.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWalletByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.findByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<WalletResponse> updateWalletBalance(@PathVariable Long userId, @RequestBody UpdateWalletRequest request) {
        return ResponseEntity.ok(walletService.update(userId, request));
    }

}
