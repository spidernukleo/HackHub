package it.unicam.hackhub.domain;


import it.unicam.hackhub.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
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
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User implements UserDetails { //estendendo questa diventa l'user di spring security
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank @Size(max=25)
    @Column(unique=true, nullable=false)
    @Getter @Setter
    private String name;

    @NotBlank @Size(max=25)
    @Column(unique=true, nullable=false)
    @Getter @Setter
    private String surname;

    @NotBlank @Email @Size(max=50)
    @Column(unique=true, nullable=false)
    @Getter @Setter
    private String email;

    @NotBlank @Size(max=120)
    @Column(unique=true, nullable=false)
    @Getter @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "userRole", nullable = false)
    @Getter @Setter
    private UserRole userRole = UserRole.VISITOR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @Getter @Setter
    private Team team;

    //metodi dell'interfaccia spring UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userRole.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public Team createTeam() {
        //TODO implementare
        return null;
    }
    public boolean isTeamLeader(){
        //TODO implementare
        return false;
    }
}
