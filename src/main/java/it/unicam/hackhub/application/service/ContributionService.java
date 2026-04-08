package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.*;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import it.unicam.hackhub.domain.enums.ContributionType;
import it.unicam.hackhub.domain.enums.HackathonState;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.ContributionRepository;
import it.unicam.hackhub.infrastructure.repository.TeamRepository;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.presentation.dto.in.ContributionRequest;
import it.unicam.hackhub.presentation.dto.in.PasswordConfirmationRequest;
import it.unicam.hackhub.presentation.dto.in.ProposeCallRequest;
import it.unicam.hackhub.presentation.dto.out.ContributionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionService {
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;
    private final TeamService teamService;
    private final MockCalendarService mockCalendarService;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

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
    public ContributionResponse sendInvite(Long teamId, ContributionRequest dto, String username){
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

    @Transactional
    public ContributionResponse sendSupportRequest(Long teamId, ContributionRequest dto, String username) {
        User sender = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found."));

        Team team = sender.getTeam();
        if (team == null || !team.getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your team.");
        }

        if(team.getCurrentHackathon()==null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not in an hackathon.");
        }

        User mentor = userRepository.findByIdAndUserRole(dto.getReceiverId(), UserRole.MENTOR).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found."));

        if(mentor.getHackathon() == null || !mentor.getHackathon().getId().equals(team.getCurrentHackathon().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mentor not in that hackathon");
        }

        if (contributionRepository.existsByTeamAndReceiverAndStatusAndType(team, mentor, ContributionStatus.PENDING, ContributionType.SUPPORT_REQUEST)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A pending support request to this mentor already exists."); //antispam
        }

        if (dto.getMessage()==null || dto.getMessage().length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message too long.");
        }


        Contribution supportRequest = new Contribution();
        supportRequest.setType(ContributionType.SUPPORT_REQUEST);
        supportRequest.setSender(sender);
        supportRequest.setReceiver(mentor);
        supportRequest.setTeam(team);
        supportRequest.setMessage(dto.getMessage());

        supportRequest = contributionRepository.save(supportRequest);

        return mapToContributionResponse(supportRequest);
    }


    @Transactional
    public void proposeCall(Long id, ProposeCallRequest dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        Contribution contribution = contributionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found."));
        checkIfValidContribution(contribution, user);
        if (!contribution.getType().equals(ContributionType.SUPPORT_REQUEST)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a support request.");
        }

        Appointment appt = mockCalendarService.scheduleCall(
                user,
                contribution.getTeam(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        contribution.accept();

        String callDetails = String.format("\n\n[Meeting info] Date: %s - Link: %s",
                appt.getStartTime().toString(),
                appt.getUrl());
        contribution.setMessage(contribution.getMessage() + callDetails);
        contributionRepository.save(contribution);
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


    @Transactional
    public ContributionResponse sendReport(Long teamId, ContributionRequest dto, String username) {
        User mentor = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found."));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found."));

        if (team.getCurrentHackathon() == null || team.getCurrentHackathon().getState() != HackathonState.ONGOING) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Team is not in an ongoing hackathon.");
        }

        if (mentor.getHackathon() == null || !mentor.getHackathon().getId().equals(team.getCurrentHackathon().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Mentor not in that hackathon.");
        }

        User organizer = userRepository.findByIdAndUserRole(dto.getReceiverId(), UserRole.ORGANIZER).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organizer not found."));

        if (!organizer.getId().equals(team.getCurrentHackathon().getOrganizer().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The organizer does not manage this hackathon.");
        }

        if (contributionRepository.existsByTeamAndReceiverAndStatusAndType(team, organizer, ContributionStatus.PENDING, ContributionType.REPORT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A pending report for this team to this organizer already exists.");
        }

        if (dto.getMessage() == null || dto.getMessage().length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message invalid or too long.");
        }

        Contribution report = new Contribution();
        report.setType(ContributionType.REPORT);
        report.setSender(mentor);
        report.setReceiver(organizer);
        report.setTeam(team);
        report.setMessage(dto.getMessage());
        report.setHackathon(team.getCurrentHackathon());
        report = contributionRepository.save(report);

        return mapToContributionResponse(report);
    }


    @Transactional
    public void banTeam(Long reportId, PasswordConfirmationRequest dto, String username) {
        User organizer = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organizer not found"));
        if (!passwordEncoder.matches(dto.getPassword(), organizer.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials.");
        }
        Contribution report = contributionRepository.findById(reportId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        checkIfValidContribution(report, organizer);
        if (report.getType() != ContributionType.REPORT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contribution is not a report");
        }

        Team team = report.getTeam();
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No team associated with this report");
        }

        if (team.getCurrentHackathon() != null) {
            team.setCurrentHackathon(null);
            teamRepository.save(team);
        }
        //submissionService.deleteAll(team); //TODO NEXT ITERATION HANDLE SUBMISSIONS
        report.accept();
        contributionRepository.save(report);
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
