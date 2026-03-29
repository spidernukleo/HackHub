package it.unicam.hackhub.presentation.mapper;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.presentation.dto.TeamDTO;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeamMapper {
    public TeamDTO toDTO(@NonNull Team team) {
        return new TeamDTO(
                team.getId(),
                team.getName(),
                team.getLeader().getId(),
                team.getCurrentHackathon().getId(),
                team.getMembers().stream().map(User::getId).collect(Collectors.toList())
        );
    }
}

