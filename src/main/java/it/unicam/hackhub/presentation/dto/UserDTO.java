package it.unicam.hackhub.presentation.dto;

import it.unicam.hackhub.domain.enums.UserRole;

public record UserDTO(
        Long id,
        String username,
        UserRole userRole,
        Long teamId,
        Long hackathonId
) {}

