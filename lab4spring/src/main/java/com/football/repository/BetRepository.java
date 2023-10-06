package com.football.repository;

import com.football.model.Bet;
import com.football.model.Match;
import com.football.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long>{
    List<Bet> findBetsByMatch(Match match);
    List<Bet> findBetsByMatchAndTeam(Match match, Team team);
}