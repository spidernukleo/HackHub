package it.unicam.hackhub.utilities.strategy;

import it.unicam.hackhub.domain.enums.PaymentType;
import it.unicam.hackhub.presentation.dto.in.PaymentRequest;
import org.springframework.stereotype.Component;


@Component
public class PaypalPaymentStrategy implements  PaymentStrategy {

    @Override
    public boolean pay(PaymentRequest request) {
        System.out.println("Avvio connessione con i server di PayPal...");
        System.out.println("Pagamento PAYPAL di €" + request.amount() + " inviato con successo al Team ID: " + request.teamId());
        System.out.println("Transazione completata. Ricevuta inviata.\n");
        return true;
    }

    @Override
    public PaymentType getType() {
        return PaymentType.PAYPAL;
    }
}
