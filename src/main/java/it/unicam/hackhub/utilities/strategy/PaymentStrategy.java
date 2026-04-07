package it.unicam.hackhub.utilities.strategy;

import it.unicam.hackhub.domain.enums.PaymentType;
import it.unicam.hackhub.presentation.dto.in.PaymentRequest;

public interface PaymentStrategy {
    boolean pay(PaymentRequest request);
    PaymentType getType();
}
