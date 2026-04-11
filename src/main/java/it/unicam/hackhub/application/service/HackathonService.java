package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.HackathonRepository;
import it.unicam.hackhub.presentation.dto.in.AddMentorRequest;
import it.unicam.hackhub.presentation.dto.in.HackathonCreateRequest;
import it.unicam.hackhub.presentation.dto.in.ProclaimWinnerRequest;
import it.unicam.hackhub.presentation.dto.out.HackathonResponse;
import it.unicam.hackhub.utilities.builder.HackathonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.HackathonState;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.domain.enums.PaymentType;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final UserRepository userRepository;
    private final MockPaymentService paymentService;

    public List<HackathonResponse> getAll(HackathonState state) {
        List<Hackathon> hackathons;
        if (state != null) {
            hackathons = hackathonRepository.findByState(state);
        } else {
            hackathons = hackathonRepository.findAll();
        }
        return hackathons.stream().map(this::mapToResponse).toList();
    }

    public HackathonResponse getById(Long id){
        Hackathon find = hackathonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon Not Found"));
        return mapToResponse(find);
    }

    @Transactional
    public HackathonResponse createHackathon(HackathonCreateRequest dto, String organizerUsername) {
        User organizer = userRepository.findByUsername(organizerUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ("Organizer not found")));
        User judge = userRepository.findByIdAndUserRole(dto.getJudgeId(), UserRole.JUDGE).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ("Judge not found")));

        /// DESIGN PATTERN BUILDER /////
        HackathonBuilder builder = new HackathonBuilder()
                .name(dto.getName())
                .rules(dto.getRules())
                .location(dto.getLocation())
                .prize(dto.getPrize())
                .enrollmentDeadline(dto.getEnrollmentDeadline())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .maxTeamSize(dto.getMaxTeamSize())
                .organizer(organizer)
                .judge(judge);

        for (Long mentorId : dto.getMentorIds()) {
            User mentor = userRepository.findByIdAndUserRole(mentorId, UserRole.MENTOR).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found for ID: " + mentorId));
            if (mentor.getHackathon() != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Mentor with ID " + mentorId + " is already assigned to another hackathon.");
            }

            builder.addMentor(mentor);
        }

        Hackathon newHackathon = builder.build();
        /////////////////////////////////////////

        Hackathon savedHackathon = hackathonRepository.save(newHackathon);
        return mapToResponse(savedHackathon);
    }

    @Transactional
    public HackathonResponse addMentor(Long hackathonId, AddMentorRequest dto, String organizerUsername) {
        User organizer = userRepository.findByUsername(organizerUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ("Organizer not found")));
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!hackathon.getOrganizer().getUsername().equals(organizer.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not yours.");
        }

        User mentor = userRepository.findByIdAndUserRole(dto.getMentorId(), UserRole.MENTOR).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found for ID: " + dto.getMentorId()));

        if (mentor.getHackathon() != null) {
            if (mentor.getHackathon().getId().equals(hackathonId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Mentor already assigned to this hackathon.");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mentor with ID " + mentor.getId() + " already assigned to another hackathon.");
        }

        hackathon.addMentor(mentor);
        Hackathon savedHackathon = hackathonRepository.save(hackathon);

        return mapToResponse(savedHackathon);
    }

    @Transactional
    public HackathonResponse setWinner(Long hackathonId, ProclaimWinnerRequest dto, String organizerUsername) {
        User organizer = userRepository.findByUsername(organizerUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organizer not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon not found"));

        if (!hackathon.getOrganizer().getUsername().equals(organizer.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not yours.");
        }

        if (hackathon.getState().equals(HackathonState.CONCLUDED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already concluded.");
        }

        Team winningTeam = hackathon.getTeams().stream()
                .filter(team -> team.getId().equals(dto.getTeamId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not in this hackathon"));

        hackathon.proclaimWinner(winningTeam);
        Hackathon savedHackathon = hackathonRepository.save(hackathon);
        paymentService.sendPrize(winningTeam, savedHackathon.getPrize(), dto.getPaymentType());

        return mapToResponse(savedHackathon);
    }


    public HackathonResponse joinHackathon(Long id, String username) {
        User teamLeader = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Team team = teamLeader.getTeam();
        if (team == null || !teamLeader.isTeamLeader()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the leader.");
        }

        Hackathon hackathon = hackathonRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon Not Found"));
        if (hackathon.getState() != HackathonState.ENROLLMENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hackathon is not in ENROLLMENT state.");
        }

        if (java.time.LocalDateTime.now().isAfter(hackathon.getEnrollmentDeadline())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enrollment deadline has passed.");
        }

        int teamSize = team.getMembers().size();
        if (teamSize < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team too small, minimum allowed: 2");
        }

        if (teamSize > hackathon.getMaxTeamSize()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team too large, maximum allowed: " + hackathon.getMaxTeamSize());
        }

        if (hackathon.getTeams().contains(team)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team already in this hackathon.");
        }

        if (team.getCurrentHackathon() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already in an hackathon.");
        }

        hackathon.getTeams().add(team);
        team.setCurrentHackathon(hackathon);
        Hackathon savedHackathon = hackathonRepository.save(hackathon);
        return mapToResponse(savedHackathon);
    }

    @Transactional
    public HackathonResponse abandonHackathon(Long hackathonId, String username) {
        User teamLeader = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Team team = teamLeader.getTeam();
        if (team == null || !teamLeader.isTeamLeader()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not the leader.");
        }

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon not found"));

        if (hackathon.getState() != HackathonState.ENROLLMENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hackathon already started, cannot leave.");
        }

        if (team.getCurrentHackathon() == null || !team.getCurrentHackathon().getId().equals(hackathonId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team not in this hackathon.");
        }

        hackathon.getTeams().remove(team);
        team.setCurrentHackathon(null);

        Hackathon savedHackathon = hackathonRepository.save(hackathon);
        return mapToResponse(savedHackathon);
    }



    private HackathonResponse mapToResponse(Hackathon hackathon) {
        List<Long> mentorIds = hackathon.getMentors().stream()
                .map(User::getId)
                .toList();

        Long organizerId = hackathon.getOrganizer() != null ? hackathon.getOrganizer().getId() : null;
        Long judgeId = hackathon.getJudge() != null ? hackathon.getJudge().getId() : null;

        return HackathonResponse.builder()
                .id(hackathon.getId())
                .name(hackathon.getName())
                .rules(hackathon.getRules())
                .location(hackathon.getLocation())
                .prize(hackathon.getPrize())
                .enrollmentDeadline(hackathon.getEnrollmentDeadline())
                .startDate(hackathon.getStartDate())
                .endDate(hackathon.getEndDate())
                .state(hackathon.getState())
                .maxTeamSize(hackathon.getMaxTeamSize())
                .organizerId(organizerId)
                .judgeId(judgeId)
                .mentorIds(mentorIds)
                .winnerTeamId(hackathon.getWinner()!=null ? hackathon.getWinner().getId() : null)
                .build();
    }












}
