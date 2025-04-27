package com.openpayd.task.dto.requests;

import lombok.Data;
import java.util.List;

@Data
public class BulkConversionRequest {
    private List<CurrencyConversionRequest> conversions;
}
