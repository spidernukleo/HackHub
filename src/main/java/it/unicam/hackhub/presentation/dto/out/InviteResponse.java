package it.unicam.hackhub.presentation.dto.out;

import it.unicam.hackhub.domain.enums.ContributionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InviteResponse {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long teamId;
    private ContributionStatus status;


}
