package it.unicam.hackhub.utilities;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.User;

import java.time.LocalDateTime;

public interface Builder<T> {
    T build();
}
