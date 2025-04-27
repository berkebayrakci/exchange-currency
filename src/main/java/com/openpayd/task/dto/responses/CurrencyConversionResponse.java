package com.openpayd.task.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyConversionResponse {
    private String transactionId;
    private BigDecimal convertedAmount;
}
