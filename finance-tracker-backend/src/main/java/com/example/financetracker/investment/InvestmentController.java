package com.example.financetracker.investment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final StockService service;

    @GetMapping("/price/{ticker}")
    public ResponseEntity<BigDecimal> getPrice(@PathVariable String ticker) {
        return ResponseEntity.ok(service.getCurrentPrice(ticker));
    }

    @PostMapping("/buy")
    public ResponseEntity<Void> buyStock(@RequestBody TradeRequest request) {
        service.buyStock(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<Holding>> getPortfolio() {
        return ResponseEntity.ok(service.getPortfolio());
    }
}
