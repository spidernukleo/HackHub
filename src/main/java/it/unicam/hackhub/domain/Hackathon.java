package it.unicam.hackhub.domain;

import it.unicam.hackhub.domain.enums.HackathonState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name="hackathons")
public class Hackathon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(unique=true, nullable=false)
    @Getter
    private String name;

    @Column(unique=true, nullable=false)
    @Getter
    private String rules;

    @Column(nullable=false)
    @Getter
    private double prize;

    @Column(nullable=false) @Getter private LocalDateTime creationDate;
    @Column(nullable=false) @Getter private LocalDateTime startDate;
    @Column(nullable=false) @Getter private LocalDateTime evaluationDate;
    @Column(nullable=false) @Getter private LocalDateTime endingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Getter @Setter
    private HackathonState state;

    @Column(nullable=false)
    @Getter
    private int minTeams;

    @Column(nullable=false)
    @Getter
    private int maxTeams;

    //rivalutare se vanno trattati come tipo User o UserRole
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    @Getter
    private User organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id")
    @Getter @Setter
    private User judge;

    @OneToMany(mappedBy = "hackathon", fetch = FetchType.LAZY)
    @Getter @Setter
    private List<User> mentors = new ArrayList<>();

    @OneToMany(mappedBy = "currentHackathon", fetch = FetchType.LAZY)
    @Getter
    private List<Team> teams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    @Getter @Setter
    private Team winner;

    public Hackathon(String name, String rules, double prize, LocalDateTime creationDate, LocalDateTime startDate, LocalDateTime evaluationDate, LocalDateTime endingDate, int minTeams, int maxTeams, User organizer, User judge) {
    }

    public boolean registerTeam(@NonNull Team target) {
        if (this.teams.contains(target)) return false;
        this.teams.add(target);
        target.setCurrentHackathon(this);
        return true;
    }

    public boolean removeTeam(@NonNull Team target) {
        if (!this.teams.contains(target)) return false;
        this.teams.remove(target);
        target.setCurrentHackathon(null);
        return true;
    }

    public boolean addMentor(@NonNull User target) {
        if (this.mentors.contains(target)) return false;
        this.mentors.add(target);
        target.setHackathon(this);
        return true;
    }

    public void proclaimWinner(Team target) {
        this.winner = target;
    }
}
