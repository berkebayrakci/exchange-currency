package com.openpayd.task.service.concretes;

import com.openpayd.task.service.abstracts.ExternalExchangeService;
import com.openpayd.task.config.ExternalApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ExternalExchangeServiceImpl implements ExternalExchangeService {

    private final RestTemplate restTemplate;
    private final ExternalApiProperties externalApiProperties;

    @Override
    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency) {
        String url = externalApiProperties.getBaseUrl()
                + "?access_key=" + externalApiProperties.getApiKey()
                + "&base=" + sourceCurrency
                + "&symbols=" + targetCurrency;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("rates")) {
            throw new RuntimeException("Invalid API response");
        }

        Map<String, Double> rates = (Map<String, Double>) body.get("rates");
        Double rate = rates.get(targetCurrency.toUpperCase());

        if (rate == null) {
            throw new RuntimeException("Rate not found for target currency");
        }

        return BigDecimal.valueOf(rate);
    }
}
