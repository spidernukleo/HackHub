package it.unicam.hackhub.infrastructure.security;

import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //il login automatico di spring si puo gestire qua, in questo caso ritorniamo l'user se lo trova dall'email
    @Override
    public User loadUserByUsername(String email) throws BadCredentialsException {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Bad credentials."));
    }



}

