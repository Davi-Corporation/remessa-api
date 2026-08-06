package com.api.remessa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    public BigDecimal getCurrentRate() {
        // Implement the logic to fetch the current exchange rate from an external API or database
        // For example, you can use a REST client to call an external service and retrieve the exchange rate
        return null; // Replace with actual implementation
    }
}
