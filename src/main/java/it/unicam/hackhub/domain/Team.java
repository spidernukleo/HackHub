package it.unicam.hackhub.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="teams")
@Data
@NoArgsConstructor
public class Team {
    @Id @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique=true, nullable=false)
    private String name;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="leader_id", nullable=false)
    private User leader;

    @OneToMany(mappedBy = "team")
    private List<User> members = new ArrayList<>();

    //bisogna aggiungere hackaton reference qua?
}
