package com.openpayd.task.controller;

import com.openpayd.task.dto.requests.*;
import com.openpayd.task.dto.responses.*;
import com.openpayd.task.service.abstracts.CurrencyService;
import com.openpayd.task.util.CsvHelper;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Tag(name = "Currency API", description = "Currency conversion and exchange rate operations")
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/exchange-rate")
    public ExchangeRateResponse getExchangeRate(@RequestBody ExchangeRateRequest request) {
        return currencyService.getExchangeRate(request);
    }

    @PostMapping("/convert")
    public CurrencyConversionResponse convertCurrency(@RequestBody CurrencyConversionRequest request) {
        return currencyService.convertCurrency(request);
    }

    @GetMapping("/conversion-history")
    public Page<CurrencyConversionResponse> getConversionHistory(
            @RequestParam(required = false) String transactionId,
            @Parameter(description = "Date in format yyyy-MM-dd (example: 2025-04-27)")
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return currencyService.getConversionHistory(transactionId, date, page, size);
    }

    @PostMapping("/bulk-upload")
    public BulkConversionResponse bulkUpload(@RequestBody BulkConversionRequest request) throws InterruptedException {
        List<CurrencyConversionResponse> responses = new ArrayList<>();

        for (CurrencyConversionRequest singleRequest : request.getConversions()) {
            CurrencyConversionResponse response = currencyService.convertCurrency(singleRequest);
            Thread.sleep(1500);
            responses.add(response);
        }

        return new BulkConversionResponse(responses);
    }

    @PostMapping(value = "/bulk-upload-csv", consumes = "multipart/form-data")
    public BulkConversionResponse bulkUploadCsv(
            @Parameter(
                    description = "Upload a CSV file. It must have headers: amount, sourceCurrency (EUR only for FREE PLAN), targetCurrency",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/octet-stream",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam("file") MultipartFile file) {
        try {
            List<CurrencyConversionRequest> conversionRequests = CsvHelper.parseCsv(file.getInputStream());

            List<CurrencyConversionResponse> responses = new ArrayList<>();

            for (CurrencyConversionRequest singleRequest : conversionRequests) {
                System.out.println("Processing CSV request: " + singleRequest); // Optional debug

                CurrencyConversionResponse response = currencyService.convertCurrency(singleRequest);
                responses.add(response);
                Thread.sleep(1500);
            }

            return new BulkConversionResponse(responses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage(), e);
        }
    }
}
