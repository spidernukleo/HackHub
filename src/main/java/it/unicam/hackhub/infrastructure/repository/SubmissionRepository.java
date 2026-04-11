package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByHackathonIdAndTeamId(Long hackathonId, Long teamId);

    List<Submission> findByHackathonId(Long hackathonId);
}

