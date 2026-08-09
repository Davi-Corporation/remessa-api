package com.api.remessa.service;

import com.api.remessa.config.TransferLimitsProperties;
import com.api.remessa.enums.PersonType;
import com.api.remessa.exception.DailyLimitExceededException;
import com.api.remessa.model.User;
import com.api.remessa.persistence.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransferValidator {

    private final TransferLimitsProperties limits;
    private final TransferRepository transferRepository;

    public void validateDailyLimit(User sender, BigDecimal amountBrl) {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        BigDecimal dailyAmount = transferRepository.sumAmountBrlBySenderAndDate(sender.getId(), startOfDay, endOfDay);

        BigDecimal limit = sender.getPersonType() == PersonType.PF ? limits.pf() : limits.pj();

        if (dailyAmount.add(amountBrl).compareTo(limit) > 0) {
            throw new DailyLimitExceededException("Daily transaction limit exceeded");
        }
    }
}