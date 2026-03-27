package it.unicam.hackhub.presentation.controller;


import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockController {

    //CONTROLLER UTILIZZATO AI FINI DI CREARE UTENTI , TEAM, HACKATHON, MOCK NEL DB

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<String> createMockUsers() {
        List<User> mockUsers = new ArrayList<>();
        String encodedPassword = passwordEncoder.encode("Password123!");
        int userCounter = 1;
        for (int i = 0; i < 3; i++) {
            mockUsers.add(createMockUser("Organizzatore" + userCounter, "Cognome" + userCounter, "utente" + userCounter + "@hackhub.com", encodedPassword, UserRole.ORGANIZER));
            userCounter++;
        }
        for (int i = 0; i < 3; i++) {
            mockUsers.add(createMockUser("Giudice" + userCounter, "Cognome" + userCounter, "utente" + userCounter + "@hackhub.com", encodedPassword, UserRole.JUDGE));
            userCounter++;
        }
        for (int i = 0; i < 3; i++) {
            mockUsers.add(createMockUser("Mentor" + userCounter, "Cognome" + userCounter, "utente" + userCounter + "@hackhub.com", encodedPassword, UserRole.MENTOR));
            userCounter++;
        }
        for (int i = 0; i < 10; i++) {
            mockUsers.add(createMockUser("Visitor" + userCounter, "Cognome" + userCounter, "utente" + userCounter + "@hackhub.com", encodedPassword, UserRole.VISITOR));userCounter++;
        }
        userRepository.saveAll(mockUsers);
        return ResponseEntity.ok("Creati " + mockUsers.size() + " utenti mock con successo! (3 Org, 3 Giudici, 3 Mentor, 10 Visitor)");
    }

    private User createMockUser(String name, String surname, String email, String password, UserRole role) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(role);
        return user;
    }

}
