package com.football.service;

import com.football.model.Team;

import java.util.List;

public interface TeamService {

    Team findById(Long id) throws Exception;

    List<Team> findAll();

    Team create(String name,String stadium, String city) throws Exception;

    void delete(Long id) throws Exception;
}
