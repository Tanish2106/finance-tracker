package com.example.financetracker.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private BigDecimal amount;
    private TransactionType type;
    private String category;
    private String description;
    private LocalDateTime date;
}
