package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.application.service.HackathonService;
import it.unicam.hackhub.presentation.dto.out.HackathonDetailResponse;
import it.unicam.hackhub.presentation.dto.out.HackathonListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
