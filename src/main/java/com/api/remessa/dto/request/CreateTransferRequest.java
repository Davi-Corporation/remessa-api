package com.api.remessa.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransferRequest(Long senderId,
                                    Long receiverId,
                                    @NotNull
                                    @Positive
                                    BigDecimal amountBrl) {
}
