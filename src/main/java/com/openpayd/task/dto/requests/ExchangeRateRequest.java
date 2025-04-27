package com.openpayd.task.dto.requests;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ExchangeRateRequest {
    @NotBlank
    private String sourceCurrency;

    @NotBlank
    private String targetCurrency;
}
