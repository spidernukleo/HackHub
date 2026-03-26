package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank @Size(max=25)
    private String name;

    @NotBlank @Size(max=25)
    private String surname;

    @NotBlank @Email @Size(max=50)
    private String email;

    @NotBlank @Size(min=8, max=25)
    private String password;
}
