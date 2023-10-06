package com.football.repository;

import com.football.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.point = u.point + :pointsToAdd")
    void addPointsToAllUsers(@Param("pointsToAdd") int pointsToAdd);
}