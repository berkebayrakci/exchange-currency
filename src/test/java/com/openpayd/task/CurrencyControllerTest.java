package com.openpayd.task;

import com.openpayd.task.controller.CurrencyController;
import com.openpayd.task.dto.requests.BulkConversionRequest;
import com.openpayd.task.dto.requests.CurrencyConversionRequest;
import com.openpayd.task.dto.requests.ExchangeRateRequest;
import com.openpayd.task.dto.responses.BulkConversionResponse;
import com.openpayd.task.dto.responses.CurrencyConversionResponse;
import com.openpayd.task.dto.responses.ExchangeRateResponse;
import com.openpayd.task.service.abstracts.CurrencyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void bulkUpload_ShouldReturnBulkConversionResponse() throws Exception {
        CurrencyConversionResponse conversionResponse = new CurrencyConversionResponse(
                "trx123",
                new BigDecimal("3000")
        );

        BulkConversionResponse bulkResponse = new BulkConversionResponse(Collections.singletonList(conversionResponse));

        // 👇 Mock convertCurrency individually
        when(currencyService.convertCurrency(any(CurrencyConversionRequest.class)))
                .thenReturn(conversionResponse);

        when(currencyService.processBulkConversion(any(BulkConversionRequest.class)))
                .thenReturn(bulkResponse);

        String requestBody = """
        {
          "conversions": [
            {
              "amount": 100,
              "sourceCurrency": "EUR",
              "targetCurrency": "TRY"
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/currency/bulk-upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conversions").isArray())
                .andExpect(jsonPath("$.conversions[0].transactionId").value("trx123"))
                .andExpect(jsonPath("$.conversions[0].convertedAmount").value(3000));
    }

    @Test
    void convertCurrency_ShouldReturnCurrencyConversionResponse() throws Exception {
        CurrencyConversionResponse conversionResponse = new CurrencyConversionResponse("trx456", new BigDecimal("5000"));

        when(currencyService.convertCurrency(any(CurrencyConversionRequest.class)))
                .thenReturn(conversionResponse);

        String requestBody = """
            {
              "amount": 200,
              "sourceCurrency": "EUR",
              "targetCurrency": "JPY"
            }
            """;

        mockMvc.perform(post("/api/currency/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("trx456"))
                .andExpect(jsonPath("$.convertedAmount").value(5000));
    }

    @Test
    void exchangeRate_ShouldReturnExchangeRateResponse() throws Exception {
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse(
                "EUR",
                "TRY",
                new BigDecimal("30")
        );

        when(currencyService.getExchangeRate(any(ExchangeRateRequest.class)))
                .thenReturn(exchangeRateResponse);

        String requestBody = """
        {
          "sourceCurrency": "EUR",
          "targetCurrency": "TRY"
        }
        """;

        mockMvc.perform(post("/api/currency/exchange-rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sourceCurrency").value("EUR"))
                .andExpect(jsonPath("$.targetCurrency").value("TRY"))
                .andExpect(jsonPath("$.exchangeRate").value(30)); // ✅ Fixed
    }

}
