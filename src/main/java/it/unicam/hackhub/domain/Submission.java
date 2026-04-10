package it.unicam.hackhub.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Table(name="submissions")
@Getter
@Setter
@RequiredArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private String content;

    @Getter
    @Column
    private BigInteger grade;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "hackathon_id",nullable = false)
    private Hackathon hackathon;

}
