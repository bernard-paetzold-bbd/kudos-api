package com.bbdgrads.kudos_api.service;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;

import java.util.Optional;

public interface TeamService {

    Optional<Team> findByTeamId(Long teamId);

    Team save(Team team, User actingUser);

    void delete(long teamId, User actingUser);
}
