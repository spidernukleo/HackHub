package it.unicam.hackhub.presentation.dto.out;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.ContributionState;
import it.unicam.hackhub.domain.enums.ContributionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class TeamInviteResponse {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long teamId;
    private ContributionState status;


}
