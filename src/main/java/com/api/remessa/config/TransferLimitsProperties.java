package com.api.remessa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "transfer.limits")
public record TransferLimitsProperties(BigDecimal pf,BigDecimal pj) {
}