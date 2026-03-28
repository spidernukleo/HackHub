package it.unicam.hackhub.presentation.dto;

import java.time.LocalDateTime;
import it.unicam.hackhub.domain.enums.ContributionType;
import it.unicam.hackhub.domain.enums.ContributionState;

public record ContributionDTO(
        Long id,
        ContributionType type,
        ContributionState state,
        Long senderId,
        Long receiverId,
        Long hackathonId,
        Long teamId,
        LocalDateTime creationDate
) {}

