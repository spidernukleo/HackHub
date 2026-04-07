package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.enums.PaymentType;
import it.unicam.hackhub.presentation.dto.in.PaymentRequest;
import it.unicam.hackhub.utilities.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MockPaymentService {

    private final Map<PaymentType, PaymentStrategy> strategies;

    public MockPaymentService(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(PaymentStrategy::getType, Function.identity()));
    }

    public void sendPrize(Team team, double amount, PaymentType type){
        PaymentStrategy strategy = strategies.get(type);
        if (strategy != null) {
            PaymentRequest request = new PaymentRequest(team.getId(), amount, type);
            strategy.pay(request);
        } else {
            throw new IllegalStateException("Nessuna strategia di pagamento configurata/trovata per tipo " + type);
        }
    }
}
