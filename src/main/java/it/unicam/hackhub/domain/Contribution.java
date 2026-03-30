package it.unicam.hackhub.domain;
import it.unicam.hackhub.domain.enums.ContributionStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import it.unicam.hackhub.domain.enums.ContributionType;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter @Setter
@Table(name = "contributions")
public class Contribution {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionStatus status;

    @JoinColumn(name = "sender_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @JoinColumn(name = "receiver_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(columnDefinition = "TEXT")
    private String message;

    @PrePersist
    protected void onCreate() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = ContributionStatus.PENDING;
        }
    }


    public void accept(){
        this.status = ContributionStatus.ACCEPTED;
    }

    public void decline(){
        this.status = ContributionStatus.DECLINED;
    }
}
