package com.openpayd.task.dto.requests;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CurrencyConversionRequest {
    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String sourceCurrency;

    @NotBlank
    private String targetCurrency;
}
