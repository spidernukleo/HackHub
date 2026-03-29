package it.unicam.hackhub.presentation.mapper;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.presentation.dto.HackathonDTO;
import lombok.NonNull;

public class HackathonMapper {
    public HackathonDTO toDTO(@NonNull Hackathon hackathon) {
        return new HackathonDTO(
                hackathon.getId(),
                hackathon.getName(),
                hackathon.getRules(),
                hackathon.getPrize(),
                hackathon.getCreationDate(),
                hackathon.getStartDate(),
                hackathon.getEvaluationDate(),
                hackathon.getEndingDate(),
                hackathon.getState(),
                hackathon.getMinTeams(),
                hackathon.getMaxTeams(),
                hackathon.getOrganizer().getId(),
                hackathon.getJudge().getId(),
                hackathon.getWinner().getId()
        );
    }
}