package com.football.service.imp;

import com.football.exception.ResourceNotFoundException;
import com.football.model.Bet;
import com.football.model.Match;
import com.football.model.Team;
import com.football.model.User;
import com.football.repository.MatchRepository;
import com.football.service.BetService;
import com.football.service.MatchService;
import com.football.service.TeamService;
import com.football.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MatchServiceImp implements MatchService {

    final MatchRepository matchRepository;
    final TeamService teamService;
    final UserService userService;

    public MatchServiceImp(MatchRepository matchRepository, TeamService teamService, UserService userService) {
        this.matchRepository = matchRepository;
        this.teamService = teamService;
        this.userService = userService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateScore(long id, byte team1_score, byte team2_score) throws Exception {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("match not exist with id :" + id));
        match.setTeam1_score(team1_score);
        match.setTeam2_score(team2_score);
        matchRepository.save(match);
    }

    @Override
    public Match findById(Long id) throws Exception {
        return matchRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Match not found"));
    }

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Match create(String name, Long team1, Long team2) throws Exception{
        Match match = new Match();
        match.setName(name);
        Team t1 = teamService.findById(team1);
        Team t2 = teamService.findById(team2);
        if ((t1==null) || (t2==null)) throw new ResourceNotFoundException("Team not exist");
        match.setTeam1(t1);
        match.setTeam2(t2);
        match = matchRepository.save(match);
        return match;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) throws Exception {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("match not exist with id :" + id));

        matchRepository.delete(match);
    }
}
