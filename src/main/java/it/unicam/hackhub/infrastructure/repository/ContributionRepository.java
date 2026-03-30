package it.unicam.hackhub.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.domain.enums.ContributionType;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    List<Contribution> findByReceiverAndType(User receiver, ContributionType type);

    List<Contribution> findByReceiverAndTypeAndStatus(User receiver, ContributionType type, ContributionStatus status);

    Boolean existsByTeamAndReceiverAndStatusAndType(Team team, User reciever, ContributionStatus state, ContributionType type);

}
