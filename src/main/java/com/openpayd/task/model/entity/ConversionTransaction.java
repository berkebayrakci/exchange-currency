package com.openpayd.task.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class ConversionTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private String sourceCurrency;

    private String targetCurrency;

    private BigDecimal amount;

    private BigDecimal convertedAmount;

    private LocalDate transactionDate;
}
