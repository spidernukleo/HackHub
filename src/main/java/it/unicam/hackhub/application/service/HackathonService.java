package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Hackathon;
import it.unicam.hackhub.infrastructure.repository.HackathonRepository;
import it.unicam.hackhub.presentation.dto.out.HackathonDetailResponse;
import it.unicam.hackhub.presentation.dto.out.HackathonListResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.HackathonState;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;

    public List<HackathonListResponse> getAll(){
        List<Hackathon> hackathons = hackathonRepository.findAll();
        return hackathons.stream().map(h -> new HackathonListResponse(
                h.getId(),
                h.getName()
        )).collect(Collectors.toList());
    }

    public HackathonDetailResponse getById(Long id){
        Hackathon find = hackathonRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hackathon Not Found"));
        HackathonDetailResponse response = new HackathonDetailResponse();
        response.setId(find.getId());
        response.setName(find.getName());
        response.setPrize(find.getPrize());
        return response;
    }

    public Hackathon createHackathon() {
        // TODO: Implementare la logica per creare un hackathon
        return null;
    }

    public boolean validateInfo() {
        // TODO: Implementare controlli di validazione
        return false;
    }

    public List<Hackathon> showHackathons() {
        // TODO: Alternativa o delegato di getAll(). Implementare.
        return null;
    }

    public Hackathon getHackathonDetails() {
        // TODO: Questo metodo potrebbe richiedere un id come parametro o essere delegato. Implementare.
        return null;
    }

    public boolean joinHackathon() {
        // TODO: Implementare (forse serviranno User/Team come argomenti in base al contesto)
        return false;
    }

    public Hackathon abandonHackathon(Long id) {
        // TODO: Logica di abbandono
        return null;
    }

    public boolean addTeam(User u, Team t) {
        // TODO: Implementare aggiunta di un team all'hackathon
        return false;
    }

    public boolean removeTeam(Team t) {
        // TODO: Implementare rimozione di un team dall'hackathon
        return false;
    }

    public List<Team> getParticipants() {
        // TODO: Implementare recupero dei team iscritti
        return null;
    }

    public boolean setWinner(Team team) {
        // TODO: Implementare assegnazione del vincitore
        return false;
    }

    public void setHackathonState(HackathonState s) {
        // TODO: Aggiornare lo stato dell'Hackathon (richiederebbe possibilmente l'ID)
    }

    public boolean addMentor(User mentor) {
        // TODO: Aggiungere un mentor al hackathon
        return false;
    }
}
