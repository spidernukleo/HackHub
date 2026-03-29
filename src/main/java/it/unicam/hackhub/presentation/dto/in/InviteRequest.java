package it.unicam.hackhub.presentation.dto.in;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InviteRequest {
    @NotBlank
    private Long receiverId;
}
