package com.api.remessa.dto.request;

import java.math.BigDecimal;

public record UpdateWalletRequest(BigDecimal balanceBrl,
                                  BigDecimal balanceUsd) {
}
