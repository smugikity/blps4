package com.football.service;

import com.football.model.Bet;
import com.football.model.Match;
import com.football.model.Team;

import java.util.List;

public interface BetService {
    List<Bet> findAll();
    Bet findById(Long id) throws Exception;
    List<Bet> findBetsByMatch(Match match);
    List<Bet> findBetsByMatchAndTeam(Match match, Team team);
    void updateUsersPoints(long id) throws Exception ;
    Bet createBet(String username, Integer point, Long matchId, Long teamId) throws Exception;
}
