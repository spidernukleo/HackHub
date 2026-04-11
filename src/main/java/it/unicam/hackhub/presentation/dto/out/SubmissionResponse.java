package it.unicam.hackhub.presentation.dto.out;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionResponse {
    private Long id;

    private Long hackathonId;
    private String hackathonName;

    private Long teamId;
    private String teamName;

    private String title;
    private String description;
    private String repositoryUrl;

    private Integer score;
    private String gradeComment;

    private Long lastEditedById;
    private String lastEditedByUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
