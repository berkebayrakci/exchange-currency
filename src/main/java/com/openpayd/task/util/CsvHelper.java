package com.openpayd.task.util;

import com.openpayd.task.dto.requests.CurrencyConversionRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {

    public static List<CurrencyConversionRequest> parseCsv(InputStream is) {
        try {
            CSVParser parser = new CSVParser(new InputStreamReader(is),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<CurrencyConversionRequest> requests = new ArrayList<>();

            for (CSVRecord record : parser) {
                CurrencyConversionRequest request = new CurrencyConversionRequest();
                request.setAmount(new java.math.BigDecimal(record.get("amount")));
                request.setSourceCurrency(record.get("sourceCurrency"));
                request.setTargetCurrency(record.get("targetCurrency"));
                requests.add(request);
            }

            return requests;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
