package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.HackathonState;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.HackathonRepository;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockController {

    //CONTROLLER UTILIZZATO AI FINI DI CREARE UTENTI , TEAM, HACKATHON, MOCK NEL DB

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HackathonRepository hackathonRepository;

    @PostMapping("/users")
    public ResponseEntity<String> createMockUsers() {
        List<User> mockUsers = new ArrayList<>();
        String encodedPassword = passwordEncoder.encode("Password123!");
        int userCounter = 1;
        for (int i = 0; i < 3; i++) {
            mockUsers.add(createMockUser("Organizzatore" + userCounter, encodedPassword, UserRole.ORGANIZER));
            userCounter++;
        }
        for (int i = 0; i < 3; i++) {
            mockUsers.add(createMockUser("Giudice" + userCounter, encodedPassword, UserRole.JUDGE));
            userCounter++;
        }
        for (int i = 0; i < 3; i++) {
            mockUsers.add(createMockUser("Mentor" + userCounter, encodedPassword, UserRole.MENTOR));
            userCounter++;
        }
        for (int i = 0; i < 10; i++) {
            mockUsers.add(createMockUser("User" + userCounter, encodedPassword, UserRole.USER));
            userCounter++;
        }
        userRepository.saveAll(mockUsers);
        return ResponseEntity.ok("Creati " + mockUsers.size() + " utenti mock con successo! (3 Org, 3 Giudici, 3 Mentor, 10 Visitor)");
    }

    private User createMockUser(String username, String password, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserRole(role);
        return user;
    }

    @PostMapping("/hackathons/{id}/next-state")
    public ResponseEntity<String> advanceHackathonState(@PathVariable Long id) {
        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon not found."));

        HackathonState currentState = hackathon.getState();
        HackathonState nextState;

        switch (currentState) {
            case ENROLLMENT:
                nextState = HackathonState.ONGOING;
                break;
            case ONGOING:
                nextState = HackathonState.EVALUATION;
                break;
            case EVALUATION:
                nextState = HackathonState.CONCLUDED;
                break;
            case CONCLUDED:
                return ResponseEntity.badRequest().body("L'Hackathon è già concluso, non ci sono altri stati.");
            default:
                nextState = HackathonState.ENROLLMENT;
        }

        hackathon.setState(nextState);
        hackathonRepository.save(hackathon);

        return ResponseEntity.ok("Stato dell'Hackathon " + id + " avanzato da " + currentState + " a " + nextState + ".");
    }


}
