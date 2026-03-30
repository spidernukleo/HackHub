package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank @Size(max=50)
    private String username;

    @NotBlank @Size(min=8, max=25)
    private String password;
}
