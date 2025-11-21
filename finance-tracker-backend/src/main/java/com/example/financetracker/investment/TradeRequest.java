package com.example.financetracker.investment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {
    private String ticker;
    private Integer quantity;
    private BigDecimal price; // The price at which the trade is executed
}
