package com.api.remessa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExchangeRateData(BigDecimal cotacaoCompra,
                               BigDecimal cotacaoVenda,
                               LocalDateTime dataHoraCotacao) {
}
