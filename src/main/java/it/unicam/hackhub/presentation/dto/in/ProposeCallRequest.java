package it.unicam.hackhub.presentation.dto.in;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProposeCallRequest {
    @NotNull(message = "Insert starting time")
    private LocalDateTime startTime;

    @NotNull(message = "Insert ending time")
    private LocalDateTime endTime;
}
