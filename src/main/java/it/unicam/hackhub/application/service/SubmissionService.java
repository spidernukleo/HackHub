package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.Submission;
import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.HackathonState;
import it.unicam.hackhub.infrastructure.repository.HackathonRepository;
import it.unicam.hackhub.infrastructure.repository.SubmissionRepository;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.presentation.dto.in.SubmissionGradeRequest;
import it.unicam.hackhub.presentation.dto.in.SubmissionUpsertRequest;
import it.unicam.hackhub.presentation.dto.out.SubmissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;


    @Transactional
    public SubmissionResponse submitOrUpdate(Long hackathonId, SubmissionUpsertRequest dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon not found"));

        Team team = user.getTeam();
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not belong to any team");
        }

        if (team.getCurrentHackathon() == null || !team.getCurrentHackathon().getId().equals(hackathonId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team is not registered for this hackathon");
        }

        if (hackathon.getState() != HackathonState.ONGOING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hackathon not started or already finished");
        }

        Submission submission = submissionRepository.findByHackathonIdAndTeamId(hackathonId, team.getId())
                .orElseGet(() -> {
                    Submission s = new Submission();
                    s.setHackathon(hackathon);
                    s.setTeam(team);
                    return s;
                });

        submission.setRepositoryUrl(dto.getRepositoryUrl());
        submission.setLastEditedBy(user);

        Submission saved = submissionRepository.save(submission);
        return toResponse(saved);
    }

    public List<SubmissionResponse> getAllByHackathon(Long hackathonId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon not found"));

        checkStaffHackathon(user, hackathon);

        return submissionRepository.findByHackathonId(hackathonId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public SubmissionResponse getById(Long submissionId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found"));

        Hackathon hackathon = submission.getHackathon();

        checkStaffHackathon(user, hackathon);

        return toResponse(submission);
    }



    public SubmissionResponse gradeSubmission(Long submissionId, SubmissionGradeRequest dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found"));

        Hackathon hackathon = submission.getHackathon();

        checkStaffHackathon(user, hackathon);

        if (hackathon.getState() != HackathonState.EVALUATION) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not in evaluation.");
        }

        if (submission.getScore() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already graded");
        }

        submission.setScore(dto.getScore());
        submission.setGradeComment(dto.getGradeComment());

        Submission saved = submissionRepository.save(submission);
        return toResponse(saved);
    }


    private void checkStaffHackathon(User user, Hackathon hackathon) {
        boolean isOrganizer = hackathon.getOrganizer() != null && hackathon.getOrganizer().getId().equals(user.getId());

        boolean isJudge = hackathon.getJudge() != null && hackathon.getJudge().getId().equals(user.getId());

        boolean isMentor = hackathon.getMentors() != null && hackathon.getMentors().stream().anyMatch(m -> m.getId().equals(user.getId()));

        if (!isOrganizer && !isJudge && !isMentor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not yours.");
        }
    }


    private SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .hackathonId(submission.getHackathon().getId())
                .teamId(submission.getTeam().getId())
                .repositoryUrl(submission.getRepositoryUrl())
                .score(submission.getScore())
                .gradeComment(submission.getGradeComment())
                .lastEditedById(submission.getLastEditedBy().getId())
                .updatedAt(submission.getUpdatedAt())
                .build();
    }

}
