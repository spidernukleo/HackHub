package it.unicam.hackhub.domain;


import it.unicam.hackhub.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name="users")
public class User implements UserDetails { //estendendo questa diventa l'user di spring security
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank @Size(max=50)
    @Column(unique=true, nullable=false)
    @Getter @Setter
    private String username;

    @NotBlank @Size(max=120)
    @Column(nullable=false)
    @Getter @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", nullable = false)
    @Getter @Setter
    private UserRole userRole = UserRole.USER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @Getter @Setter
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id")
    @Getter @Setter
    private Hackathon hackathon;

    public boolean isTeamLeader() {
        if (this.team == null || this.team.getLeader() == null) return false;
        return this.equals(this.team.getLeader());
    }

    //metodi dell'interfaccia spring UserDetails, non toccare
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userRole.name()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
