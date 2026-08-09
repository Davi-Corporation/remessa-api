package com.api.remessa.service;

import com.api.remessa.dto.response.ExchangeRateApiResponse;
import com.api.remessa.dto.response.ExchangeRateData;
import com.api.remessa.exception.ResourceNotFoundException;
import com.api.remessa.model.ExchangeRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BcbExchangeRateClientTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private BcbExchangeRateClient bcbExchangeRateClient;


    @Test
    void shouldReturnExchangeRateFromBcb() {

        ExchangeRateData data = new ExchangeRateData(
                new BigDecimal("5.0902"),
                new BigDecimal("5.0902"),
                LocalDateTime.of(
                        2026,
                        8,
                        7,
                        13,
                        4,
                        19,
                        455752000
                )
        );

        ExchangeRateApiResponse response = new ExchangeRateApiResponse(new BigDecimal("5.0902"),List.of(data));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(ExchangeRateApiResponse.class)).thenReturn(response);

        ExchangeRate result = bcbExchangeRateClient.getLatestExchangeRate();

        assertNotNull(result);

        assertEquals(data.dataHoraCotacao(),result.getQuotationDateTime());

        assertEquals(data.cotacaoCompra(),result.getBuyRate());
    }


    @Test
    void shouldThrowExceptionWhenBcbReturnsNull() {

        when(restClient.get()).thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(ExchangeRateApiResponse.class)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> bcbExchangeRateClient.getLatestExchangeRate());
    }


    @Test
    void shouldThrowExceptionWhenBcbReturnsEmptyResponse() {

        ExchangeRateApiResponse response = new ExchangeRateApiResponse(new BigDecimal("5.0902"),List.of());

        when(restClient.get()).thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.body(ExchangeRateApiResponse.class)).thenReturn(response);

        assertThrows(ResourceNotFoundException.class,() -> bcbExchangeRateClient.getLatestExchangeRate());
    }
}