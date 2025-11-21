package com.example.financetracker.budget;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody BudgetRequest request
    ) {
        service.saveBudget(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<Budget>> findAllBudgets() {
        return ResponseEntity.ok(service.findAllBudgets());
    }
}
