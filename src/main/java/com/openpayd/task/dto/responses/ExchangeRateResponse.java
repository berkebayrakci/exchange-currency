package com.openpayd.task.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRateResponse {
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal exchangeRate;
}
