package it.unicam.hackhub.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="teams")
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Getter
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    @Getter
    private User leader;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @Getter
    private List<User> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    @Getter
    @Setter
    private Hackathon currentHackathon;

    public void addMember(User user) {
        //TODO implementare
    }

    public void removeMember(User user) {
        //TODO implementare
    }

    public void setLeader(User leader) {
        //TODO implementare
    }

    public boolean isEmpty() {
        //TODO implementare
        return false;
    }
}