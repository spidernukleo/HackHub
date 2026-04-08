package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordConfirmationRequest {
    @NotBlank(message = "Password is required")
    private String password;
}
