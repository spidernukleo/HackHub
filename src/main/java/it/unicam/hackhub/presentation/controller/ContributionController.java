package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.ContributionService;
import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.domain.enums.ContributionState;
import it.unicam.hackhub.presentation.dto.in.TeamInviteRequest;
import it.unicam.hackhub.presentation.dto.out.HackathonDetailResponse;
import it.unicam.hackhub.presentation.dto.out.HackathonListResponse;
import it.unicam.hackhub.presentation.dto.out.TeamInviteResponse;
import it.unicam.hackhub.presentation.dto.in.MessageRequest;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contribution")
@RequiredArgsConstructor
public class ContributionController {

    private final ContributionService contributionService;

    @GetMapping("/invites")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<List<TeamInviteResponse>> getInvites(@RequestParam(required = false) ContributionState status, Authentication authentication) {
        return ResponseEntity.ok(contributionService.getContributions(authentication.getName(), status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamInviteResponse> getById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(contributionService.getById(id, authentication.getName()));
    }

    @PostMapping("/{teamId}/invite")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<TeamInviteResponse> sendInvite(@PathVariable Long teamId, @RequestBody TeamInviteRequest dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contributionService.sendInvite(teamId, dto, authentication.getName()));
    }

    @PostMapping("/{teamId}/support")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<Void> sendSupportRequest(@PathVariable Long teamId, @Valid @RequestBody MessageRequest dto, Authentication authentication) {
        contributionService.sendSupportRequest(teamId, dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{teamId}/report")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<Void> sendReport(@PathVariable Long teamId, @Valid @RequestBody MessageRequest dto, Authentication authentication) {
        contributionService.sendReport(teamId, dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<Void> acceptContribution(@PathVariable Long id, Authentication authentication) {
        contributionService.acceptContribution(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decline")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<Void> declineContribution(@PathVariable Long id, Authentication authentication) {
        contributionService.declineContribution(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

}
