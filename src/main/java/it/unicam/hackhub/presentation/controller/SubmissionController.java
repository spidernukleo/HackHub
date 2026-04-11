package it.unicam.hackhub.presentation.controller;

import it.unicam.hackhub.application.service.SubmissionService;
import it.unicam.hackhub.presentation.dto.in.SubmissionGradeRequest;
import it.unicam.hackhub.presentation.dto.in.SubmissionUpsertRequest;
import it.unicam.hackhub.presentation.dto.out.SubmissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/submission")
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping("/hackathon/{hackathonId}")
    @PreAuthorize("hasAnyRole('TEAM_LEADER', 'TEAM_MEMBER')")
    public ResponseEntity<SubmissionResponse> submitOrUpdate(@PathVariable Long hackathonId, @Valid @RequestBody SubmissionUpsertRequest dto, Authentication authentication) {
        SubmissionResponse response = submissionService.submitOrUpdate(hackathonId, dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hackathon/{hackathonId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'JUDGE', 'MENTOR')")
    public ResponseEntity<List<SubmissionResponse>> getAllByHackathon(@PathVariable Long hackathonId, Authentication authentication) {
        List<SubmissionResponse> response = submissionService.getAllByHackathon(hackathonId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{submissionId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'JUDGE', 'MENTOR')")
    public ResponseEntity<SubmissionResponse> getById(@PathVariable Long submissionId, Authentication authentication) {
        SubmissionResponse response = submissionService.getById(submissionId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{submissionId}/grade")
    @PreAuthorize("hasRole('JUDGE')")
    public ResponseEntity<SubmissionResponse> gradeSubmission(@PathVariable Long submissionId, @Valid @RequestBody SubmissionGradeRequest dto, Authentication authentication) {
        SubmissionResponse response = submissionService.gradeSubmission(submissionId, dto, authentication.getName());
        return ResponseEntity.ok(response);
    }

}
