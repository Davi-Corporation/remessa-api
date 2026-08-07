package com.api.remessa.scheduler;


import com.api.remessa.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "${exchange-rate.scheduler.cron}")
    public void updateExchageRate() {
        exchangeRateService.updateTodayQuotation();
    }
}
