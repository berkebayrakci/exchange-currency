package com.openpayd.task.service.abstracts;

import com.openpayd.task.dto.requests.BulkConversionRequest;
import com.openpayd.task.dto.requests.CurrencyConversionRequest;
import com.openpayd.task.dto.requests.ExchangeRateRequest;
import com.openpayd.task.dto.responses.BulkConversionResponse;
import com.openpayd.task.dto.responses.CurrencyConversionResponse;
import com.openpayd.task.dto.responses.ExchangeRateResponse;
import org.springframework.data.domain.Page;
import java.time.LocalDate;

public interface CurrencyService {

    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);

    CurrencyConversionResponse convertCurrency(CurrencyConversionRequest request);

    Page<CurrencyConversionResponse> getConversionHistory(String transactionId, LocalDate date, int page, int size);

    BulkConversionResponse processBulkConversion(BulkConversionRequest request);
}
