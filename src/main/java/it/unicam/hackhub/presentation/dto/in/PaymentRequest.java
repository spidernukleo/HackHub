package it.unicam.hackhub.presentation.dto.in;

import it.unicam.hackhub.domain.enums.PaymentType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
        @NotNull Long teamId,
        @Positive double amount,
        @NotNull PaymentType type
) {}
