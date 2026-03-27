package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {

}
