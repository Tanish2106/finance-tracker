package com.example.financetracker.transaction;

import com.example.financetracker.auth.User;
import com.example.financetracker.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final UserRepository userRepository;

    public void saveTransaction(TransactionRequest request) {
        // Get current authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        var transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .description(request.getDescription())
                .date(request.getDate())
                .user(user)
                .build();
        repository.save(transaction);
    }

    public List<Transaction> findAllTransactions() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return repository.findAllByUserId(user.getId());
    }
}
