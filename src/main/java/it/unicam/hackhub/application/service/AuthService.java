package it.unicam.hackhub.application.service;


import it.unicam.hackhub.application.exception.EmailAlreadyUsedException;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.infrastructure.security.JwtService;
import it.unicam.hackhub.presentation.dto.out.AuthResponse;
import it.unicam.hackhub.presentation.dto.in.UserLoginDto;
import it.unicam.hackhub.presentation.dto.in.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyUsedException("Email already used.");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setUserRole(UserRole.VISITOR);
        User saved = userRepository.save(user); //salva a db registrazione completa

        String jwtToken = jwtService.generateToken(saved);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(UserLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate( //login direttamente gestito da spring
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        return new AuthResponse(jwtService.generateToken(user));
    }

}
