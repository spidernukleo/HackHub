package it.unicam.hackhub.utilities.strategy;

import it.unicam.hackhub.domain.enums.PaymentType;
import it.unicam.hackhub.presentation.dto.in.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(PaymentRequest request) {
        System.out.println("Processing Bank Transfer for team " + request.teamId() + " amount: " + request.amount());
        return true;
    }

    @Override
    public PaymentType getType() {
        return PaymentType.BANK_TRANSFER;
    }
}
