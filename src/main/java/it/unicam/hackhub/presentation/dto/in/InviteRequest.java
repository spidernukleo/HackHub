package it.unicam.hackhub.presentation.dto.in;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InviteRequest {
    @NotNull
    private Long receiverId;

    @Size(max = 50)
    private String message;
}
