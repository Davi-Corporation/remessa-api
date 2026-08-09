package com.api.remessa.service;

import com.api.remessa.dto.response.ExchangeRateApiResponse;
import com.api.remessa.dto.response.ExchangeRateData;
import com.api.remessa.exception.ResourceNotFoundException;
import com.api.remessa.model.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BcbExchangeRateClient {

    private final RestClient restClient;


    public ExchangeRate getLatestExchangeRate() {

        LocalDateTime date = LocalDateTime.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        ExchangeRateApiResponse response = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/CotacaoDolarDia(dataCotacao=@dataCotacao)")
                        .queryParam("@dataCotacao","'" + formattedDate + "'")
                        .queryParam("$top", 100)
                        .queryParam("$format", "json")
                        .build())
                .retrieve()
                .body(ExchangeRateApiResponse.class);

        if (response == null || response.value().isEmpty()) {
            throw new ResourceNotFoundException("No exchange rate found for " + formattedDate);
        }

        ExchangeRateData data = response.value().getFirst();

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setQuotationDateTime(data.dataHoraCotacao());
        exchangeRate.setBuyRate(data.cotacaoCompra());

        return exchangeRate;
    }
}