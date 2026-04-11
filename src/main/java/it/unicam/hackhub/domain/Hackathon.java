package it.unicam.hackhub.domain;

import it.unicam.hackhub.domain.enums.HackathonState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hackathons")
@Getter @Setter
@NoArgsConstructor // Richiesto da JPA
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable=false)
    private String rules;

    @Column(nullable=false)
    private String location;

    @Column(nullable=false)
    private double prize;

    @Column(nullable=false)
    private LocalDateTime enrollmentDeadline;

    @Column(nullable=false)
    private LocalDateTime startDate;

    @Column(nullable=false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private HackathonState state;

    @Column(nullable=false)
    private int maxTeamSize;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable=false)
    private User organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id", nullable=false)
    private User judge;

    @OneToMany(mappedBy = "hackathon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> mentors = new ArrayList<>();

    @OneToMany(mappedBy = "currentHackathon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Team> teams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private Team winner;

    @OneToMany(mappedBy = "hackathon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Submission> submissions = new ArrayList<>();


    public Hackathon(String name, String rules, String location, double prize,
                     LocalDateTime enrollmentDeadline, LocalDateTime startDate,
                     LocalDateTime endDate, HackathonState state, int maxTeamSize,
                     User organizer, User judge, List<User> mentors) {
        this.name = name;
        this.rules = rules;
        this.location = location;
        this.prize = prize;
        this.enrollmentDeadline = enrollmentDeadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.maxTeamSize = maxTeamSize;
        this.organizer = organizer;
        this.judge = judge;
        if (mentors != null) {
            this.mentors.addAll(mentors);
            for (User mentor : mentors) {
                mentor.setHackathon(this);
            }
        }
    }

    public void addMentor(@NonNull User target) {
        if (this.mentors.contains(target)) return;
        this.mentors.add(target);
        target.setHackathon(this);
    }

    public void proclaimWinner(@NonNull Team target) {
        this.winner = target;
        this.state = HackathonState.CONCLUDED;

        for (User mentor : this.mentors) { //pulisco mentori
            mentor.setHackathon(null);
        }
        for (Team team : this.teams) { //pulisco teammembers
            team.setCurrentHackathon(null);
        }
    }
}
