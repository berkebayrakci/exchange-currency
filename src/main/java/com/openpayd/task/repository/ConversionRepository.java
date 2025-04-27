package com.openpayd.task.repository;

import com.openpayd.task.model.entity.ConversionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ConversionRepository extends JpaRepository<ConversionTransaction, Long> {

    Page<ConversionTransaction> findByTransactionDate(LocalDate transactionDate, Pageable pageable);

    ConversionTransaction findByTransactionId(String transactionId);
}
