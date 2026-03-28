package it.unicam.hackhub.presentation.dto;

import java.util.List;

public record TeamDTO(
        Long id,
        String name,
        Long leaderId,
        Long currentHackathonId,
        List<Long> memberIds
) {}

