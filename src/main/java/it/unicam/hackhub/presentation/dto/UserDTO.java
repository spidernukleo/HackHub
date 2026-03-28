package it.unicam.hackhub.presentation.dto;

import it.unicam.hackhub.domain.enums.UserRole;

public record UserDTO(
        Long id,
        String name,
        String surname,
        String email,
        UserRole userRole,
        Long teamId,
        Long hackathonId
) {}

