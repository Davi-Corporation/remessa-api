package com.api.remessa.service;

import com.api.remessa.exception.ResourceNotFoundException;
import com.api.remessa.model.ExchangeRate;
import com.api.remessa.persistence.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RedisTemplate<String, ExchangeRate> redisTemplate;
    private final ExchangeRateRepository exchangeRateRepository;
    private final BcbExchangeRateClient bcbExchangeRateClient;

    private static final String CACHE_KEY = "exchange_rate:usd";

    public ExchangeRate getLatestRate() {

        ExchangeRate cached = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cached != null) {
            return cached;
        }

        ExchangeRate rate = exchangeRateRepository
                .findFirstByOrderByQuotationDateTimeDesc()
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate not found"));

        redisTemplate.opsForValue().set(CACHE_KEY, rate);

        return rate;
    }

    @Transactional
    public void updateTodayQuotation() {

        ExchangeRate exchangeRate = bcbExchangeRateClient.getLatestExchangeRate();
        exchangeRateRepository.save(exchangeRate);
        redisTemplate.opsForValue().set(CACHE_KEY, exchangeRate);
    }
}
