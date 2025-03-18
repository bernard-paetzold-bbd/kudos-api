package com.bbdgrads.kudos_api.service;

import com.bbdgrads.kudos_api.model.Team;

import java.util.Optional;

public interface TeamService {

    Optional<Team> findByTeamId(Long teamId);

    Team save(Team team);

    void delete(long teamId);
}
