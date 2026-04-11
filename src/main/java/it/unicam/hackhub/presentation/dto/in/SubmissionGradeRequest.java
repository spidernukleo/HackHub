package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubmissionGradeRequest {
    @Min(0)
    @Max(10)
    private Integer score;

    @NotBlank
    @Size(max = 1000)
    private String gradeComment;
}
