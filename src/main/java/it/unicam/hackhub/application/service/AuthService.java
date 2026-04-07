package it.unicam.hackhub.application.service;


import it.unicam.hackhub.application.exception.UsernameAlreadyUsedException;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.infrastructure.security.JwtService;
import it.unicam.hackhub.presentation.dto.in.AuthRequest;
import it.unicam.hackhub.presentation.dto.out.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public AuthResponse register(AuthRequest dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameAlreadyUsedException("Username already used.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserRole(UserRole.USER);
        User saved = userRepository.save(user); //salva a db registrazione completa

        String jwtToken = jwtService.generateToken(saved);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(AuthRequest dto) {
        Authentication authentication = authenticationManager.authenticate( //login direttamente gestito da spring
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        return new AuthResponse(jwtService.generateToken(user));
    }

    @Transactional
    public void deleteAccount(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Bad Credentials.");
        }

        if (user.getTeam() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Abandon your team before deleting your account.");
        }

        userRepository.delete(user);
    }


}
