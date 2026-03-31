package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import it.unicam.hackhub.domain.enums.ContributionType;
import it.unicam.hackhub.domain.enums.HackathonState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {
    List<Hackathon> findByState(HackathonState state);
}
