package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.ContributionService;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import it.unicam.hackhub.presentation.dto.in.InviteRequest;
import it.unicam.hackhub.presentation.dto.in.MessageRequest;
import it.unicam.hackhub.presentation.dto.in.SupportRequest;
import it.unicam.hackhub.presentation.dto.out.ContributionResponse;
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

    @GetMapping("/me/invites")
    @PreAuthorize("hasAnyRole('TEAM_LEADER', 'TEAM_MEMBER', 'USER')")
    public ResponseEntity<List<ContributionResponse>> getInvites(@RequestParam(required = false) ContributionStatus status, Authentication authentication) {
        return ResponseEntity.ok(contributionService.getMyInvites(authentication.getName(), status));
    }

    @PostMapping("/{teamId}/invite")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<ContributionResponse> sendInvite(@PathVariable Long teamId, @RequestBody InviteRequest dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contributionService.sendInvite(teamId, dto, authentication.getName()));
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> acceptInvite(@PathVariable Long id, Authentication authentication) {
        contributionService.acceptInvite(id, authentication.getName());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/me/support-requests")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<List<ContributionResponse>> getSupportRequests(@RequestParam(required = false) ContributionStatus status, Authentication authentication) {
        return ResponseEntity.ok(contributionService.getMySupportRequests(authentication.getName(), status));
    }

    @PostMapping("/{teamId}/support")
    @PreAuthorize("hasRole('TEAM_MEMBER')")
    public ResponseEntity<ContributionResponse> sendSupportRequest(@PathVariable Long teamId, @Valid @RequestBody SupportRequest dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contributionService.sendSupportRequest(teamId, dto, authentication.getName()));
    }

    @PostMapping("/{id}/call")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Void> proposeCall(@PathVariable Long id, Authentication authentication) {
        contributionService.proposeCall(id, authentication.getName());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/me/reports")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<List<ContributionResponse>> getReports(@RequestParam(required = false) ContributionStatus status, Authentication authentication) {
        return ResponseEntity.ok(contributionService.getMyReports(authentication.getName(), status));
    }

    @PostMapping("/{teamId}/report")
    @PreAuthorize("hasRole('MENTOR')") /////////////////////TODO PROSSIMA ITERAZIONE
    public ResponseEntity<Void> sendReport(@PathVariable Long teamId, @Valid @RequestBody MessageRequest dto, Authentication authentication) {
        contributionService.sendReport(teamId, dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/ban")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Void> banTeam(@PathVariable Long id, Authentication authentication) {
        contributionService.banTeam(id, authentication.getName());
        return ResponseEntity.ok().build();
    }



    @GetMapping("/{id}")
    public ResponseEntity<ContributionResponse> getById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(contributionService.getById(id, authentication.getName()));
    }


    @PostMapping("/{id}/decline")
    @PreAuthorize("hasAnyRole('TEAM_LEADER', 'TEAM_MEMBER', 'USER')")
    public ResponseEntity<Void> declineContribution(@PathVariable Long id, Authentication authentication) {
        contributionService.declineContribution(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
