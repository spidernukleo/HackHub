package it.unicam.hackhub.presentation.dto.out;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamCreateResponse {
    private Long id;
    private String name;
}
