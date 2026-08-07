package com.api.remessa.dto.response;

import java.math.BigDecimal;

public record WalletResponse(Long userId,
                             BigDecimal balanceBrl,
                             BigDecimal balanceUsd) {
}
