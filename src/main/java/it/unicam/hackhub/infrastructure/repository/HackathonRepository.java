package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HackathonRepository extends JpaRepository<Hackathon, Long> {

    //TODO metodi db degli hackathon
}
