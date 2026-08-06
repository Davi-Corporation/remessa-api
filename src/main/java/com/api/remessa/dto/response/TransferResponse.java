package com.api.remessa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(Long id,
                               Long senderId,
                               Long receiverId,
                               BigDecimal amountBrl,
                               BigDecimal amountUsd,
                               BigDecimal exchangeRate,
                               LocalDateTime createdAt) {
}
