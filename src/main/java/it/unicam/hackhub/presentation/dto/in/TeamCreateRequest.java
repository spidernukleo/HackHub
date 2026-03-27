package it.unicam.hackhub.presentation.dto.in;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamCreateRequest {

    @NotBlank @Size(max=25)
    private String name;

}
