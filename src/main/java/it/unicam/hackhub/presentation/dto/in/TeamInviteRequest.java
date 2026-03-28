package it.unicam.hackhub.presentation.dto.in;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamInviteRequest {
    @NotBlank
    private Long receiverId;
}
