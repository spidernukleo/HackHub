package it.unicam.hackhub.presentation.dto;

import java.time.LocalDateTime;
import it.unicam.hackhub.domain.enums.HackathonState;

public record HackathonDTO(
        Long id,
        String name,
        String rules,
        double prize,
        LocalDateTime creationDate,
        LocalDateTime startDate,
        LocalDateTime evaluationDate,
        LocalDateTime endingDate,
        HackathonState state,
        int minTeams,
        int maxTeams,
        Long organizerId,
        Long judgeId,
        Long winnerId
) {}

