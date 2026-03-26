package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.TeamService;
import it.unicam.hackhub.presentation.dto.in.TeamCreateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('VISITOR')")
    public ResponseEntity<Boolean> create(@Valid @RequestBody TeamCreateDto dto, Authentication authentication) {
        return ResponseEntity.ok(true);
    }


}
