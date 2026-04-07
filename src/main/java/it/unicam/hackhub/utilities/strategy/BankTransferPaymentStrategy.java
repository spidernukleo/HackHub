package it.unicam.hackhub.utilities.strategy;

import it.unicam.hackhub.domain.enums.PaymentType;
import it.unicam.hackhub.presentation.dto.in.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean pay(PaymentRequest request) {
        System.out.println("Generazione distinta per il Bonifico Bancario...");
        System.out.println("Bonifico BANCARIO di €" + request.amount() + " preso in carico per il Team ID: " + request.teamId());
        System.out.println("I fondi saranno disponibili tra 2-3 giorni lavorativi.\n");
        return true;
    }

    @Override
    public PaymentType getType() {
        return PaymentType.BANK_TRANSFER;
    }
}

