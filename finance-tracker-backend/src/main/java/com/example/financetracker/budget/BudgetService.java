package com.example.financetracker.budget;

import com.example.financetracker.auth.User;
import com.example.financetracker.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repository;
    private final UserRepository userRepository;

    public void saveBudget(BudgetRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        // Check if budget for category already exists, if so update it
        Optional<Budget> existingBudget = repository.findByUserIdAndCategory(user.getId(), request.getCategory());

        if (existingBudget.isPresent()) {
            Budget budget = existingBudget.get();
            budget.setAmount(request.getAmount());
            repository.save(budget);
        } else {
            Budget budget = Budget.builder()
                    .category(request.getCategory())
                    .amount(request.getAmount())
                    .user(user)
                    .build();
            repository.save(budget);
        }
    }

    public List<Budget> findAllBudgets() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return repository.findAllByUserId(user.getId());
    }
}
