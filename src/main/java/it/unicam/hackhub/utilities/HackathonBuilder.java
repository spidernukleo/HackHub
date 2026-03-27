package it.unicam.hackhub.utilities;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.User;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;

import java.time.LocalDateTime;

public class HackathonBuilder implements Builder{

    private String name;
    private String rules;
    private double prize;
    private LocalDateTime creationDate;
    private LocalDateTime startDate;
    private LocalDateTime evaluationDate;
    private LocalDateTime endingDate;
    private int minTeams;
    private int maxTeams;
    private User organizer;
    private User judge;

    @Override
    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Override
    public void setRules(@NonNull String rules) {
        this.rules = rules;
    }

    @Override
    public void setPrize(@Positive double prize) {
        this.prize = prize;
    }


    @Override
    public void setCreationDate(@NonNull LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public void setStartDate(@NonNull LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEvaluationDate(@NonNull LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    @Override
    public void setEndingDate(@NonNull LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    @Override
    public void setminTeams(@Positive int minTeams) {
        this.minTeams = minTeams;
    }

    @Override
    public void setMaxTeams(@Positive int maxTeams) {
         this.maxTeams = maxTeams;
    }

    @Override
    public void setOrganizer(@NonNull User organizer) {
        this.organizer = organizer;
    }

    @Override
    public void setJudge(@NonNull User judge) {
        this.judge = judge;
    }

    @Override
    public Hackathon getResult() {
        return new Hackathon(name, rules, prize, creationDate, startDate, evaluationDate, endingDate, minTeams, maxTeams, organizer, judge);
    }
}
