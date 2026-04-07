package it.unicam.hackhub.utilities.builder;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.HackathonState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HackathonBuilder implements Builder<Hackathon> {

    private String name;
    private String rules;
    private String location;
    private double prize;
    private LocalDateTime enrollmentDeadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int maxTeamSize;
    private User organizer;
    private User judge;
    private HackathonState state = HackathonState.ENROLLMENT;
    private List<User> mentors = new ArrayList<>();

    public HackathonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HackathonBuilder rules(String rules) {
        this.rules = rules;
        return this;
    }

    public HackathonBuilder location(String location) {
        this.location = location;
        return this;
    }

    public HackathonBuilder prize(double prize) {
        this.prize = prize;
        return this;
    }

    public HackathonBuilder enrollmentDeadline(LocalDateTime enrollmentDeadline) {
        this.enrollmentDeadline = enrollmentDeadline;
        return this;
    }

    public HackathonBuilder startDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public HackathonBuilder endDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public HackathonBuilder maxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
        return this;
    }

    public HackathonBuilder organizer(User organizer) {
        this.organizer = organizer;
        return this;
    }

    public HackathonBuilder judge(User judge) {
        this.judge = judge;
        return this;
    }

    public HackathonBuilder addMentor(User mentor) {
        this.mentors.add(mentor);
        return this;
    }

    @Override
    public Hackathon build() {
        if (this.startDate != null && this.endDate != null && this.startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("Starting date can't be after ending date.");
        }

        if (this.mentors == null || this.mentors.isEmpty()) {
            throw new IllegalArgumentException("A mentor required.");
        }

        return new Hackathon(
                this.name,
                this.rules,
                this.location,
                this.prize,
                this.enrollmentDeadline,
                this.startDate,
                this.endDate,
                this.state,
                this.maxTeamSize,
                this.organizer,
                this.judge,
                this.mentors
        );
    }
}
