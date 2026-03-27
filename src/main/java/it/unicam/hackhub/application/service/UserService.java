package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        // Usa nativamente il metodo del repository
        return userRepository.findAll();
    }

    public List<User> getMentors() {
        // TODO: Implementare la logica per ottenere gli utenti che hanno il ruolo di Mentor
        // es. return userRepository.findByUserRole(UserRole.MENTOR);
        return null;
    }
}
