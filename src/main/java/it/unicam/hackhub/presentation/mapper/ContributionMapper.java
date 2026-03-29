package it.unicam.hackhub.presentation.mapper;

import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.presentation.dto.ContributionDTO;
import lombok.NonNull;

public class ContributionMapper {
    public ContributionDTO toDTO(@NonNull Contribution contribution) {
        return new ContributionDTO(
                contribution.getId(),
                contribution.getType(),
                contribution.getStatus(),
                contribution.getSender().getId(),
                contribution.getReceiver().getId(),
                contribution.getHackathon().getId(),
                contribution.getTeam().getId(),
                contribution.getCreationDate()
        );
    }
}