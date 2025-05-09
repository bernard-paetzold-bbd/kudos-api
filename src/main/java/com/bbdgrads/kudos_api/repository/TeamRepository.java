package com.bbdgrads.kudos_api.repository;

import com.bbdgrads.kudos_api.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {

    public Optional<Team> findByName(String team_name);
}
