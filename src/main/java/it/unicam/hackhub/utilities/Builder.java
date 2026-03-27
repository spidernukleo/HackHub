package it.unicam.hackhub.utilities;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.User;

import java.time.LocalDateTime;

public interface Builder {
    void setName(String name);
    void setRules(String rules);
    void setPrize(double prize);
    void setCreationDate(LocalDateTime creationDate);
    void setStartDate(LocalDateTime startDate);
    void setEvaluationDate(LocalDateTime evaluationDate);
    void setEndingDate(LocalDateTime endingDate);
    void setminTeams(int minTeams);
    void setMaxTeams(int maxTeams);
    void setOrganizer(User organizer);
    void setJudge(User judge);
    
    Hackathon getResult();
}
