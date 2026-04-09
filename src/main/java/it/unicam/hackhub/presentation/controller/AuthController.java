package it.unicam.hackhub.presentation.controller;



import it.unicam.hackhub.application.service.AuthService;
import it.unicam.hackhub.presentation.dto.in.PasswordConfirmationRequest;
import it.unicam.hackhub.presentation.dto.out.AuthResponse;
import it.unicam.hackhub.presentation.dto.in.AuthRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @DeleteMapping("/delete-account")
    @PreAuthorize("hasAnyRole('TEAM_LEADER', 'TEAM_MEMBER', 'USER')")
    public ResponseEntity<Void> deleteAccount(@Valid @RequestBody PasswordConfirmationRequest request, Authentication authentication) {
        authService.deleteAccount(authentication.getName(), request.getPassword());
        return ResponseEntity.noContent().build();
    }


}
