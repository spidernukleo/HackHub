package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.TeamService;
import it.unicam.hackhub.presentation.dto.in.TeamCreateRequest;
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
    public ResponseEntity<String> create(@Valid @RequestBody TeamCreateRequest dto, Authentication authentication) {
        String user=authentication.getName();
        teamService.createTeam(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Team '"+dto.getName()+"' created");
    }


}
