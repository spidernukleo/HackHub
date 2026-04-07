package it.unicam.hackhub.presentation.dto.in;

import it.unicam.hackhub.domain.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProclaimWinnerRequest {
    @NotNull
    private Long teamId;

    @NotNull
    private PaymentType paymentType;
}
