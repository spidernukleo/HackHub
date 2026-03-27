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
}
