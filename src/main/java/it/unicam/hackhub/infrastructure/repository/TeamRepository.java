package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Boolean existsByName(String email);
}
