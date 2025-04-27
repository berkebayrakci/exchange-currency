package com.openpayd.task.service.concretes;

import com.openpayd.task.dto.requests.*;
import com.openpayd.task.dto.responses.*;
import com.openpayd.task.exception.CustomException;
import com.openpayd.task.model.entity.ConversionTransaction;
import com.openpayd.task.model.enums.CurrencyCode;
import com.openpayd.task.repository.ConversionRepository;
import com.openpayd.task.service.abstracts.CurrencyService;
import com.openpayd.task.service.abstracts.ExternalExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final ExternalExchangeService externalExchangeService;
    private final ConversionRepository conversionRepository;

    @Override
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        validateCurrencyCode(request.getSourceCurrency());
        validateCurrencyCode(request.getTargetCurrency());

        BigDecimal rate = externalExchangeService.getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency());
        return new ExchangeRateResponse(request.getSourceCurrency(), request.getTargetCurrency(), rate);
    }

    @Override
    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest request) {
        validateCurrencyCode(request.getSourceCurrency());
        validateCurrencyCode(request.getTargetCurrency());

        BigDecimal rate = externalExchangeService.getExchangeRate(request.getSourceCurrency(), request.getTargetCurrency());
        BigDecimal convertedAmount = request.getAmount().multiply(rate);

        ConversionTransaction transaction = new ConversionTransaction();
        transaction.setSourceCurrency(request.getSourceCurrency());
        transaction.setTargetCurrency(request.getTargetCurrency());
        transaction.setAmount(request.getAmount());
        transaction.setConvertedAmount(convertedAmount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionId(UUID.randomUUID().toString());

        conversionRepository.save(transaction);

        return new CurrencyConversionResponse(transaction.getTransactionId(), convertedAmount);
    }

    @Override
    public Page<CurrencyConversionResponse> getConversionHistory(String transactionId, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (transactionId != null) {
            ConversionTransaction transaction = conversionRepository.findByTransactionId(transactionId);
            if (transaction == null) {
                throw new CustomException("Transaction not found for ID: " + transactionId, "TRANSACTION_NOT_FOUND");
            }
            return new PageImpl<>(
                    List.of(new CurrencyConversionResponse(transaction.getTransactionId(), transaction.getConvertedAmount()))
            );
        } else if (date != null) {
            Page<ConversionTransaction> transactions = conversionRepository.findByTransactionDate(date, pageable);
            return transactions.map(t -> new CurrencyConversionResponse(t.getTransactionId(), t.getConvertedAmount()));
        } else {
            throw new CustomException("Either transactionId or date must be provided", "INVALID_INPUT");
        }
    }

    @Override
    public BulkConversionResponse processBulkConversion(BulkConversionRequest request) {
        List<CurrencyConversionResponse> responses = request.getConversions().stream()
                .map(this::convertCurrency)
                .collect(Collectors.toList());

        return new BulkConversionResponse(responses);
    }

    private void validateCurrencyCode(String code) {
        try {
            CurrencyCode.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid currency code: " + code, "INVALID_CURRENCY");
        }
    }

}
