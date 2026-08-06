package com.api.remessa.dto.response;

import java.math.BigDecimal;

public record WalletResponse(Long id,
                             Long userId,
                             BigDecimal balanceBrl,
                             BigDecimal balanceUsd) {
}
