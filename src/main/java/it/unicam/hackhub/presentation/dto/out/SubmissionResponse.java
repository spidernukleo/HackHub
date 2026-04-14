package it.unicam.hackhub.presentation.dto.out;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionResponse {
    private Long id;
    private Long hackathonId;
    private Long teamId;

    private String repositoryUrl;

    private Integer score;
    private String gradeComment;

    private Long lastEditedById;
    private LocalDateTime updatedAt;
}
