package com.football.service.imp;

import com.football.exception.ResourceNotFoundException;
import com.football.model.Team;
import com.football.repository.TeamRepository;
import com.football.service.TeamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamServiceImp implements TeamService {
    final
    TeamRepository teamRepository;

    public TeamServiceImp(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team findById(Long id) throws Exception {

        return teamRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Team not found"));
    }

    @Override
    public List<Team> findAll() {
        //jmsTemplate.convertAndSend("myQueue", "hello :3");
        return teamRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Team create(String name, String stadium, String city) throws Exception {
        Team team = new Team();
        team.setName(name);
        team.setStadium(stadium);
        team.setCity(city);
        team = teamRepository.save(team);
        //if (true) throw new Exception("uwuw");
        return team;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) throws Exception {
        Team match = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tem not exist with id :" + id));
        teamRepository.delete(match);
    }


}
