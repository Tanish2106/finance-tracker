package com.example.financetracker.investment;

import com.example.financetracker.auth.User;
import com.example.financetracker.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final HoldingRepository repository;
    private final UserRepository userRepository;
    
    // Using Finnhub as a free option. 
    // You should ideally move this to application.properties
    private static final String FINNHUB_API_KEY = "ctpe1bpr01qj9k74k7g0ctpe1bpr01qj9k74k7gg"; // Free sandbox key for demo
    private static final String FINNHUB_URL = "https://finnhub.io/api/v1/quote?symbol=%s&token=" + FINNHUB_API_KEY;

    public BigDecimal getCurrentPrice(String ticker) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(FINNHUB_URL, ticker);
        try {
            // Finnhub returns JSON like: {"c": 150.00, "d": ...} where 'c' is current price
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.get("c") != null) {
                Object priceObj = response.get("c");
                return new BigDecimal(priceObj.toString());
            }
        } catch (Exception e) {
            // Fallback or error handling
            e.printStackTrace();
        }
        return BigDecimal.ZERO; // Or throw exception
    }

    public void buyStock(TradeRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Optional<Holding> existingHolding = repository.findByUserIdAndTicker(user.getId(), request.getTicker());

        if (existingHolding.isPresent()) {
            Holding holding = existingHolding.get();
            // Calculate new average price
            // (OldQty * OldAvg + NewQty * NewPrice) / (OldQty + NewQty)
            BigDecimal totalCost = holding.getAverageBuyPrice().multiply(new BigDecimal(holding.getQuantity()))
                    .add(request.getPrice().multiply(new BigDecimal(request.getQuantity())));
            int newQuantity = holding.getQuantity() + request.getQuantity();
            
            holding.setQuantity(newQuantity);
            holding.setAverageBuyPrice(totalCost.divide(new BigDecimal(newQuantity), BigDecimal.ROUND_HALF_UP));
            repository.save(holding);
        } else {
            Holding holding = Holding.builder()
                    .ticker(request.getTicker())
                    .quantity(request.getQuantity())
                    .averageBuyPrice(request.getPrice())
                    .user(user)
                    .build();
            repository.save(holding);
        }
    }

    public List<Holding> getPortfolio() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return repository.findAllByUserId(user.getId());
    }
}
