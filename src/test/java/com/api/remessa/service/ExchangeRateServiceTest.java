package com.api.remessa.service;

import com.api.remessa.exception.ResourceNotFoundException;
import com.api.remessa.model.ExchangeRate;
import com.api.remessa.persistence.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private RedisTemplate<String, ExchangeRate> redisTemplate;

    @Mock
    private ValueOperations<String, ExchangeRate> valueOperations;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private BcbExchangeRateClient bcbExchangeRateClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;


    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }


    @Test
    void shouldReturnExchangeRateFromRedis() {

        ExchangeRate rate = new ExchangeRate();
        rate.setBuyRate(new BigDecimal("5.0902"));

        when(valueOperations.get("exchange_rate:usd")).thenReturn(rate);

        ExchangeRate result =exchangeRateService.getLatestRate();

        assertSame(rate, result);

        verify(valueOperations).get("exchange_rate:usd");

        verifyNoInteractions(exchangeRateRepository);
    }


    @Test
    void shouldReturnExchangeRateFromDatabaseWhenCacheIsEmpty() {

        ExchangeRate rate = new ExchangeRate();
        rate.setBuyRate(new BigDecimal("5.0902"));

        when(valueOperations.get("exchange_rate:usd")).thenReturn(null);

        when(exchangeRateRepository.findFirstByOrderByQuotationDateTimeDesc()).thenReturn(Optional.of(rate));

        ExchangeRate result = exchangeRateService.getLatestRate();

        assertSame(rate, result);

        verify(exchangeRateRepository).findFirstByOrderByQuotationDateTimeDesc();

        verify(valueOperations).set("exchange_rate:usd", rate);
    }


    @Test
    void shouldThrowExceptionWhenExchangeRateDoesNotExist() {

        when(valueOperations.get("exchange_rate:usd")).thenReturn(null);

        when(exchangeRateRepository.findFirstByOrderByQuotationDateTimeDesc()).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> exchangeRateService.getLatestRate());

        verify(exchangeRateRepository).findFirstByOrderByQuotationDateTimeDesc();

        verify(valueOperations, never()).set(anyString(), any(ExchangeRate.class));
    }


    @Test
    void shouldUpdateExchangeRate() {

        ExchangeRate rate = new ExchangeRate();
        rate.setBuyRate(new BigDecimal("5.0902"));

        when(bcbExchangeRateClient.getLatestExchangeRate()).thenReturn(rate);

        exchangeRateService.updateTodayQuotation();

        verify(bcbExchangeRateClient).getLatestExchangeRate();

        verify(exchangeRateRepository).save(rate);

        verify(valueOperations).set("exchange_rate:usd", rate);
    }
}