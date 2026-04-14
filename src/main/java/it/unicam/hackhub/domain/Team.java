package it.unicam.hackhub.domain;

import it.unicam.hackhub.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="teams")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE teams SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank @Size(max=25)
    @Column(unique = true, nullable = false)
    @Getter @Setter
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    @Getter @Setter
    private User leader;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @Getter
    private List<User> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id")
    @Getter @Setter
    private Hackathon currentHackathon;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    public void addMember(User user) {
        this.members.add(user);
        user.setTeam(this);
        user.setUserRole(UserRole.TEAM_MEMBER);
    }

    public void removeMember(User user) {
        this.members.remove(user);
        user.setTeam(null);
    }

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    private List<Submission> submissions = new ArrayList<>();

}