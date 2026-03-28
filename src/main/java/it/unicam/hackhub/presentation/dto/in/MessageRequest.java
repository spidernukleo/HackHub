package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    @NotBlank
    private String message;
}
