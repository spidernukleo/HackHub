package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.TeamService;
import it.unicam.hackhub.presentation.dto.in.TeamCreateRequest;
import it.unicam.hackhub.presentation.dto.out.TeamCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<TeamCreateResponse> create(@Valid @RequestBody TeamCreateRequest dto, Authentication authentication) {
        String user=authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(dto, user));
    }


    @PostMapping("/abandon")
    @PreAuthorize("hasAnyRole('TEAM_LEADER', 'TEAM_MEMBER')")
    public ResponseEntity<Void> create(Authentication authentication) {
        String user = authentication.getName();
        teamService.abandonTeam(user);
        return ResponseEntity.noContent().build();
    }


}
