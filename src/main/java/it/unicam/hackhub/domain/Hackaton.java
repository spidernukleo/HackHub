package it.unicam.hackhub.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="hackatons")
public class Hackaton {
    @Id @GeneratedValue
    private Long id;

    //TODO hackaton entity capire bene come fare
}
