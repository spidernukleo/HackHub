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
import it.unicam.hackhub.presentation.dto.out.ContributionResponse;
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
    private final TeamService teamService;

    public List<ContributionResponse> getMyInvites(String username, ContributionStatus status) {
        User receiver = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<Contribution> invites;
        if (status != null) {
            invites = contributionRepository.findByReceiverAndTypeAndStatus(receiver, ContributionType.INVITE, status);
        } else {
            invites = contributionRepository.findByReceiverAndType(receiver, ContributionType.INVITE);
        }

        return invites.stream().map(this::mapToContributionResponse).toList();
    }

    @Transactional
    public ContributionResponse sendInvite(Long teamId, InviteRequest dto, String username){
        User leader = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found."));
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

        if (receiver.getUserRole().equals(UserRole.ORGANIZER) || receiver.getUserRole().equals(UserRole.MENTOR) || receiver.getUserRole().equals(UserRole.JUDGE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't invite staff member.");
        }

        if (contributionRepository.existsByTeamAndReceiverAndStatusAndType(team, receiver, ContributionStatus.PENDING, ContributionType.INVITE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver already invited."); //antispam
        }

        if (dto.getMessage().length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message too long");
        }

        Contribution invite = new Contribution();
        invite.setType(ContributionType.INVITE);
        invite.setSender(leader);
        invite.setReceiver(receiver);
        invite.setTeam(team);
        invite.setMessage(dto.getMessage());
        invite = contributionRepository.save(invite);

        return mapToContributionResponse(invite);
    }

    @Transactional
    public void acceptInvite(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Contribution contribution = contributionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite not found"));
        checkIfValidContribution(contribution, user);
        if (!contribution.getType().equals(ContributionType.INVITE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not an invite.");
        }

        if (user.getTeam() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You already are in a team.");
        }

        contribution.accept();
        contributionRepository.save(contribution);
        teamService.addMember(contribution.getTeam().getId(), user);
    }



    public List<ContributionResponse> getMySupportRequests(String username, ContributionStatus status) {
        User receiver = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<Contribution> invites;
        if (status != null) {
            invites = contributionRepository.findByReceiverAndTypeAndStatus(receiver, ContributionType.SUPPORT_REQUEST, status);
        } else {
            invites = contributionRepository.findByReceiverAndType(receiver, ContributionType.SUPPORT_REQUEST);
        }

        return invites.stream().map(this::mapToContributionResponse).toList();
    }

    public boolean sendSupportRequest(Long teamId, MessageRequest req, String username) {
        // TODO: Implementare la logica per inviare una richiesta di supporto
        return false;
    }

    @Transactional
    public void proposeCall(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Contribution contribution = contributionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found."));
        checkIfValidContribution(contribution, user);
        if (!contribution.getType().equals(ContributionType.SUPPORT_REQUEST)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a support request.");
        }
        //caso d'uso proporisionze call prossima iterazione
    }


    public List<ContributionResponse> getMyReports(String username, ContributionStatus status) {
        User receiver = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        List<Contribution> invites;
        if (status != null) {
            invites = contributionRepository.findByReceiverAndTypeAndStatus(receiver, ContributionType.REPORT, status);
        } else {
            invites = contributionRepository.findByReceiverAndType(receiver, ContributionType.REPORT);
        }

        return invites.stream().map(this::mapToContributionResponse).toList();
    }

    public boolean sendReport(Long teamId, MessageRequest req, String username) {
        // TODO: Implementare la logica per inviare un referto/report
        return false;
    }

    @Transactional
    public void banTeam(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Contribution contribution = contributionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found."));
        checkIfValidContribution(contribution, user);
        if (!contribution.getType().equals(ContributionType.REPORT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a report.");
        }

        //caso d'uso ban team prossima iterazione
    }




    public ContributionResponse getById(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Contribution contribution = contributionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found."));
        boolean isSender = contribution.getSender() != null && contribution.getSender().equals(user);
        boolean isReceiver = contribution.getReceiver() != null && contribution.getReceiver().equals(user);

        if (!isSender && !isReceiver) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not yours.");
        }

        return mapToContributionResponse(contribution);
    }


    @Transactional
    public void declineContribution(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Contribution contribution = contributionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        checkIfValidContribution(contribution, user);
        contribution.decline();
        contributionRepository.save(contribution);
    }

    private void checkIfValidContribution(Contribution contribution, User user) {
        if (!contribution.getReceiver().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not yours.");
        }

        if (contribution.getStatus() != ContributionStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already handled.");
        }
    }

    private ContributionResponse mapToContributionResponse(Contribution c) {
        return ContributionResponse.builder()
                .id(c.getId())
                .type(c.getType())
                .status(c.getStatus())
                .creationDate(c.getCreationDate())
                .senderId(c.getSender().getId())
                .receiverId(c.getReceiver().getId())
                .teamId(c.getTeam().getId())
                .hackathonId(c.getHackathon() != null ? c.getHackathon().getId() : null)
                .message(c.getMessage())
                .build();
    }
}
