package it.unicam.hackhub.domain;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

import it.unicam.hackhub.domain.enums.ContributionState;
import it.unicam.hackhub.domain.enums.ContributionType;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Table(name = "contributions")
public class Contribution {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType type;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionState state;

    @Getter
    @JoinColumn(name = "sender_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @Getter
    @JoinColumn(name = "receiver_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private User receiver;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY,optional = false)     //riferimento ad un hackathon potrebbe essere completamente inutile,rivalutare sistema di Contributions
    private Hackathon hackathon;

    @Getter
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Getter
    @Column(nullable = false)
    private String message;


    public void accept(){
        //TODO implementare
    }

    public void decline(){
        //TODO implementare
    }
}
