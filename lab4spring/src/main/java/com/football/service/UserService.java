package com.football.service;

import com.football.model.User;

import java.util.List;

public interface UserService {
    User create(String username,String password) throws Exception;
    List<User> findAll();

    User findByUsername(String username) throws Exception;

    Integer getPointByUsername(String username) throws Exception;

    void addPointsToAllUsers(int pointsToAdd);

    User findById(Long id) throws Exception;

    User save(User user) throws Exception;
}
