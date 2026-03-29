package it.unicam.hackhub.presentation.mapper;

import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.presentation.dto.UserDTO;
import lombok.NonNull;

public class UserMapper {
    public UserDTO toDTO(@NonNull User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getUserRole(),
                user.getTeam().getId(),
                user.getHackathon().getId()
        );
    }
}