package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.domain.enums.UserRole;
import it.unicam.hackhub.infrastructure.repository.TeamRepository;
import it.unicam.hackhub.infrastructure.repository.UserRepository;
import it.unicam.hackhub.presentation.dto.in.TeamCreateRequest;
import it.unicam.hackhub.presentation.dto.out.TeamCreateResponse;
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
    public TeamCreateResponse createTeam(TeamCreateRequest dto, String user){
        User leader = userRepository.findByEmail(user).orElseThrow(()->new UsernameNotFoundException(user));
        if(leader.getTeam()!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You already are in a team");
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

        return new TeamCreateResponse(newTeam.getId(), newTeam.getName());
    }

    @Transactional
    public void abandonTeam(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(email));
        Team team = user.getTeam();

        if(team==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not in a team.");
        }
        if(team.getCurrentHackathon()!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Team in an Hackathon");
        }

        boolean isLeader = user.isTeamLeader();

        team.removeMember(user);
        user.setTeam(null);
        user.setUserRole(UserRole.VISITOR);
        userRepository.save(user);

        if(isLeader || team.isEmpty()){
            for(User remaining:team.getMembers()){
                remaining.setTeam(null);
                remaining.setUserRole(UserRole.VISITOR);
                userRepository.save(remaining);
            }
            teamRepository.delete(team);
        }else{
            teamRepository.save(team);
        }
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
