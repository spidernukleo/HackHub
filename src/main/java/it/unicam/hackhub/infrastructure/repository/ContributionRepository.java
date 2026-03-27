package it.unicam.hackhub.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.domain.enums.ContributionType;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    List<Contribution> findByType(ContributionType type);

    Optional<Contribution> findById(Long id);

}
