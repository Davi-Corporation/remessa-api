package com.api.remessa.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ExchangeRateApiResponse(BigDecimal cotacaoCompra,
                                      List<ExchangeRateData> value) {
}
