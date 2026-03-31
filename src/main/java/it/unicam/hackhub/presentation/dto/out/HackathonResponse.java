package it.unicam.hackhub.presentation.dto.out;

import it.unicam.hackhub.domain.enums.HackathonState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HackathonResponse {
    private Long id;
    private String name;
    private String rules;
    private String location;
    private double prize;
    private LocalDateTime enrollmentDeadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private HackathonState state;
    private int maxTeamSize;
    private Long organizerId;
    private Long judgeId;
    private List<Long> mentorIds;
    private Long winnerTeamId;
}