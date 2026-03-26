package it.unicam.hackhub.presentation;



import it.unicam.hackhub.application.service.AuthService;
import it.unicam.hackhub.presentation.dto.out.AuthResponse;
import it.unicam.hackhub.presentation.dto.in.UserLoginDto;
import it.unicam.hackhub.presentation.dto.in.UserRegistrationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

}
