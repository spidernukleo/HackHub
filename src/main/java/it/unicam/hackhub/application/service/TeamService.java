package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.TeamRepository;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.presentation.dto.in.TeamCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;


    @Transactional
    public void createTeam(TeamCreateRequest dto, String user){
        User leader = userRepository.findByEmail(user).orElseThrow(()->new UsernameNotFoundException(user));
        if(leader.getTeam()!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You already are in a team");
        }

        if(teamRepository.existsByName(dto.getName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Team already exists");
        }

        Team newTeam = new Team();
        newTeam.setName(dto.getName());
        newTeam.setLeader(leader);
        newTeam = teamRepository.save(newTeam); //genero id nel db
        newTeam.addMember(leader);
        leader.setUserRole(UserRole.TEAM_LEADER);
        userRepository.save(leader);
    }

    public boolean validateTeamName(String teamName) {
        // TODO: Implementare validazione del nome del team
        return false;
    }

    public boolean addMember(User u) {
        // TODO: Implementare aggiunta membro al team
        return false;
    }

    public void removeMember(Long userId) {
        // TODO: Implementare rimozione membro dal team
    }

    public void deleteTeam(Long teamId) {
        // TODO: Implementare eliminazione team
    }

}
