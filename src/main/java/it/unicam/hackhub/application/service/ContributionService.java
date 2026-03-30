package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Contribution;
import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import it.unicam.hackhub.domain.enums.ContributionType;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.ContributionRepository;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.presentation.dto.in.InviteRequest;
import it.unicam.hackhub.presentation.dto.out.InviteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import it.unicam.hackhub.presentation.dto.in.MessageRequest;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;

    public List<InviteResponse> getContributions(String email, ContributionStatus status) {
        User receiver = userRepository.findByUsername(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<Contribution> invites;
        if (status != null) {
            invites = contributionRepository.findByReceiverAndTypeAndStatus(receiver, ContributionType.INVITE, status);
        } else {
            invites = contributionRepository.findByReceiverAndType(receiver, ContributionType.INVITE);
        }

        return invites.stream().map(this::mapInviteToDTO).toList();
    }

    public InviteResponse getById(Long id, String email) {
        User receiver = userRepository.findByUsername(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        Contribution invite = contributionRepository.findByIdAndReceiver(id, receiver).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite not found."));

        return mapInviteToDTO(invite);
    }


    @Transactional
    public InviteResponse sendInvite(Long teamId, InviteRequest dto, String email){
        User leader = userRepository.findByUsername(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found."));
        Team team = leader.getTeam();
        if (team == null || !team.getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your team.");
        }

        if (!leader.isTeamLeader()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the team leader can send invites.");
        }

        User receiver = userRepository.findById(dto.getReceiverId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found."));
        if (leader.equals(receiver)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot invite yourself.");
        }

        if (!receiver.getUserRole().equals(UserRole.USER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver is already in a team. / Can't invite staff member.");
        }

        if (contributionRepository.existsByTeamAndReceiverAndStatusAndType(team, receiver, ContributionStatus.PENDING, ContributionType.INVITE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver already invited."); //antispam
        }

        Contribution invite = new Contribution();
        invite.setType(ContributionType.INVITE);
        invite.setSender(leader);
        invite.setReceiver(receiver);
        invite.setTeam(team);
        invite = contributionRepository.save(invite);

        return mapInviteToDTO(invite);
    }


    private InviteResponse mapInviteToDTO(Contribution invite) {
        return InviteResponse.builder()
                .id(invite.getId())
                .teamId(invite.getTeam().getId())
                .senderId(invite.getSender().getId())
                .status(invite.getStatus())
                .build();
    }

    public boolean sendSupportRequest(Long teamId, MessageRequest req, String username) {
        // TODO: Implementare la logica per inviare una richiesta di supporto
        return false;
    }

    public boolean sendReport(Long teamId, MessageRequest req, String username) {
        // TODO: Implementare la logica per inviare un referto/report
        return false;
    }

    public boolean acceptContribution(Long id, String username) {
        // TODO: Implementare la logica per accettare una contribution
        return false;
    }

    public boolean declineContribution(Long id, String username) {
        // TODO: Implementare la logica per rifiutare una contribution
        return false;
    }

}
