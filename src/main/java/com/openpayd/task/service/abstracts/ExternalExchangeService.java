package com.openpayd.task.service.abstracts;

import java.math.BigDecimal;

public interface ExternalExchangeService {
    BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency);
}
