package com.openpayd.task.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class BulkConversionResponse {
    private List<CurrencyConversionResponse> conversions;
}
