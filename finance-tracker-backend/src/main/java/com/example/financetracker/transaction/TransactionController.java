package com.example.financetracker.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody TransactionRequest request
    ) {
        service.saveTransaction(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> findAllTransactions() {
        return ResponseEntity.ok(service.findAllTransactions());
    }
}
