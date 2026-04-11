package it.unicam.hackhub.presentation.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubmissionUpsertRequest {
    @NotBlank
    @Size(max = 500)
    private String repositoryUrl;
}
