package com.bbdgrads.kudos_api.service;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void delete(long teamId) {
        Team tempTeam = teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Team with id %s does not exist.", teamId)));
        teamRepository.delete(tempTeam);
    }

    @Override
    public Optional<Team> findByTeamId(Long teamId) {
        return teamRepository.findById(teamId);
    }

    public Optional<Team> findByName(String team_name) {
        return teamRepository.findByName(team_name);
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }
}
