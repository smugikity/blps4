package com.football.service.imp;

import com.football.exception.ResourceNotFoundException;
import com.football.model.*;
import com.football.repository.BetRepository;
import com.football.service.BetService;
import com.football.service.MatchService;
import com.football.service.TeamService;
import com.football.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BetServiceImp implements BetService {
    final BetRepository betRepository;
    final UserService userService;
    final MatchService matchService;
    final TeamService teamService;

    public BetServiceImp(BetRepository betRepository, UserService userService, MatchService matchService, TeamService teamService) {
        this.betRepository = betRepository;
        this.userService = userService;
        this.matchService = matchService;
        this.teamService = teamService;
    }

    @Override
    public List<Bet> findAll() {
        return betRepository.findAll();
    }

    @Override
    public Bet findById(Long id) throws Exception{
        return betRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Bet not found"));

    }

    @Override
    public List<Bet> findBetsByMatch(Match match) {
        return betRepository.findBetsByMatch(match);
    }

    @Override
    public List<Bet> findBetsByMatchAndTeam(Match match, Team team) {
        return betRepository.findBetsByMatchAndTeam(match,team);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Bet createBet(String username, Integer point, Long matchId, Long teamId) throws Exception {
        User user = userService.findByUsername(username);
        if (user==null) throw new Exception("User is invalid");
        Match match = matchService.findById(matchId);
        Team team = teamService.findById(teamId);
        if (point>user.getPoint()) throw new Exception("Not enough points");
        if (!(team.equals(match.getTeam1())||team.equals(match.getTeam2()))) throw new Exception("Chosen team is invalid");
        user.setPoint(user.getPoint()-point);
        user = userService.save(user);
        Bet bet = new Bet();
        bet.setUser(user);
        bet.setPoint(point);
        bet.setTeam(team);
        bet.setMatch(match);
        bet = betRepository.save(bet);
        return bet;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUsersPoints(long id) throws Exception {
        try {
            Match match = matchService.findById(id);
            if (match.getTeam1_score()==match.getTeam2_score()) {
                updateForEachUser(findBetsByMatch(match),1);
            } else {
                Team winningTeam = match.getTeam1_score()>match.getTeam2_score()?match.getTeam1():match.getTeam2();
                updateForEachUser(findBetsByMatchAndTeam(match,winningTeam),2);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private void updateForEachUser(List<Bet> bets, Integer multiplier) {
        bets.forEach(bet->{
            bet.getUser().setPoint(bet.getUser().getPoint()+multiplier*bet.getPoint());
            try {
                userService.save(bet.getUser());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
