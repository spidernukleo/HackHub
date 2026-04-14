package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.TeamService;
import it.unicam.hackhub.presentation.dto.in.PasswordConfirmationRequest;
import it.unicam.hackhub.presentation.dto.in.TeamCreateRequest;
import it.unicam.hackhub.presentation.dto.out.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamCreateRequest dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(dto, authentication.getName()));
    }

    @PostMapping("/abandon")
    @PreAuthorize("hasAnyRole('TEAM_LEADER', 'TEAM_MEMBER')")
    public ResponseEntity<Void> abandonTeam(Authentication authentication) {
        teamService.abandonTeam(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<Void> deleteTeam(@Valid @RequestBody PasswordConfirmationRequest request, Authentication authentication) {
        teamService.deleteTeam(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }


}
