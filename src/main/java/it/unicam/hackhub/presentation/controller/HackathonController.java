package it.unicam.hackhub.presentation.controller;


import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import it.unicam.hackhub.application.service.HackathonService;
import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.presentation.dto.in.HackathonCreateRequest;
import it.unicam.hackhub.presentation.dto.out.HackathonDetailResponse;
import it.unicam.hackhub.presentation.dto.out.HackathonListResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hackathon")
public class HackathonController {

    private final HackathonService hackathonService;

    @GetMapping
    public ResponseEntity<List<HackathonListResponse>> getAll() {
        return ResponseEntity.ok(hackathonService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonDetailResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hackathonService.getById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<Hackathon> create(@Valid @RequestBody HackathonCreateRequest dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hackathonService.createHackathon(dto, authentication.getName()));
    }

    @PostMapping("/{id}/join")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<Void> joinHackathon(@PathVariable Long id, Authentication authentication) {
        hackathonService.joinHackathon(id, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
