package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.infrastructure.repository.ContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;

    public List<Contribution> getContributions(Long userId) {
        // TODO: Implementare la logica per ottenere le contributions di un utente
        return null;
    }

    public boolean addContribution(Contribution c) {
        // TODO: Implementare la logica per aggiungere una contribution
        return false;
    }

    public boolean sendInvite(Long teamId, Long targetId) {
        // TODO: Implementare la logica per inviare un invito a un utente
        return false;
    }

    public boolean sendSupportRequest(Long teamId, String msg) {
        // TODO: Implementare la logica per inviare una richiesta di supporto
        return false;
    }

    public boolean sendReport(Long teamId, String msg) {
        // TODO: Implementare la logica per inviare un referto/report
        return false;
    }

    public boolean acceptContribution(Long id) {
        // TODO: Implementare la logica per accettare una contribution
        return false;
    }

    public boolean declineContribution(Long id) {
        // TODO: Implementare la logica per rifiutare una contribution
        return false;
    }

    public Contribution getContributionDetails(Long id) {
        // TODO: Implementare la logica per ottenere i dettagli di una contribution
        return null;
    }
}
