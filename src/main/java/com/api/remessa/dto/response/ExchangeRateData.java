package com.api.remessa.dto.response;



import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExchangeRateData(BigDecimal cotacaoCompra,
                               BigDecimal cotacaoVenda,
                               @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
                               LocalDateTime dataHoraCotacao) {
}
