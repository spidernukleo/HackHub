package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HackathonCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String rules;

    @Min(0)
    private double prize;

    @NotNull
    @Future
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime evaluationDate;

    @NotNull
    @Future
    private LocalDateTime endingDate;

    @Min(1)
    private int minTeams;

    @Min(1)
    private int maxTeams;

    private Long judgeId;
}
