package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Hackaton;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HackatonRepository extends JpaRepository<Hackaton, Long> {

    //TODO metodi db degli hackaton
}
