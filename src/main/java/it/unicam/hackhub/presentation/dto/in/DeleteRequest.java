package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteRequest {
    @NotBlank(message = "Password is required")
    private String password;
}
