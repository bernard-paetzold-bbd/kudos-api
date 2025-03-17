package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional since we are looking for a single object to be returned;
    public Optional<User> findByUserId(Long userId);

    public Optional<User> findByGoogleToken(String googleToken);

    public Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.team = :team WHERE u.id = :userId")
    int updateUserTeam(@Param("userId") Long userId, @Param("team") Team team);

}
