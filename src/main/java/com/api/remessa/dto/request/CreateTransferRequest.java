package com.api.remessa.dto.request;

import java.math.BigDecimal;

public record CreateTransferRequest(Long senderId,
                                    Long receiverId,
                                    BigDecimal amountBrl) {
}
