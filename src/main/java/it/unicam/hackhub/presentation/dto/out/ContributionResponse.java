package it.unicam.hackhub.presentation.dto.out;


import com.fasterxml.jackson.annotation.JsonInclude;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import it.unicam.hackhub.domain.enums.ContributionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContributionResponse {

    private Long id;
    private ContributionType type;
    private ContributionStatus status;
    private LocalDateTime creationDate;
    private Long senderId;
    private Long receiverId;
    private Long teamId;
    private Long hackathonId;
    private String message;


}

