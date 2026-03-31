package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProclaimWinnerRequest {
    @NotNull
    private Long teamId;
}
