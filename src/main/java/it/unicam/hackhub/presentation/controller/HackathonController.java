package it.unicam.hackhub.presentation.controller;


import java.util.List;

import it.unicam.hackhub.domain.enums.ContributionStatus;
import it.unicam.hackhub.domain.enums.HackathonState;
import it.unicam.hackhub.presentation.dto.in.AddMentorRequest;
import it.unicam.hackhub.presentation.dto.in.ProclaimWinnerRequest;
import it.unicam.hackhub.presentation.dto.out.HackathonResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import it.unicam.hackhub.application.service.HackathonService;
import it.unicam.hackhub.presentation.dto.in.HackathonCreateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hackathon")
public class HackathonController {

    private final HackathonService hackathonService;

    @GetMapping
    public ResponseEntity<List<HackathonResponse>> getAll(@RequestParam(required = false) HackathonState state) {
        return ResponseEntity.ok(hackathonService.getAll(state));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hackathonService.getById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<HackathonResponse> create(@Valid @RequestBody HackathonCreateRequest dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hackathonService.createHackathon(dto, authentication.getName()));
    }


    @PostMapping("/{hackathonId}/add-mentor")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<HackathonResponse> addMentor(@Valid @RequestBody AddMentorRequest dto, @PathVariable Long hackathonId, Authentication authentication) {
        HackathonResponse response = hackathonService.addMentor(hackathonId, dto, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{hackathonId}/proclaim-winner")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<HackathonResponse> proclaimWinner(@Valid @RequestBody ProclaimWinnerRequest dto, @PathVariable Long hackathonId, Authentication authentication) {
        HackathonResponse response = hackathonService.setWinner(hackathonId, dto, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{hackathonId}/join")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<HackathonResponse> joinHackathon(@PathVariable Long hackathonId, Authentication authentication) {
        HackathonResponse response = hackathonService.joinHackathon(hackathonId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{hackathonId}/leave")
    @PreAuthorize("hasRole('TEAM_LEADER')")
    public ResponseEntity<HackathonResponse> abandonHackathon(@PathVariable Long hackathonId, Authentication authentication) {
        HackathonResponse response = hackathonService.abandonHackathon(hackathonId, authentication.getName());
        return ResponseEntity.ok(response);
    }



}
