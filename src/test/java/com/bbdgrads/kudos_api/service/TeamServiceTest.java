package com.bbdgrads.kudos_api.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.repository.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;

    @Before
    public void setUp() {
        team = new Team();
        team.setName("team-zkl");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testfindByTeamId() {

        when(teamRepository.findById(any(Long.class))).thenReturn(Optional.of(team));

        var foundTeam = teamService.findByTeamId(1L);

        assertNotNull(foundTeam);
        assertFalse(foundTeam.isEmpty());

        assertEquals(team.getName(), foundTeam.get().getName());

        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveTeam() {

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        teamService.save(team);

        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testDeleteTeam() {

        when(teamRepository.findById(any(Long.class))).thenReturn(Optional.of(team));
        doNothing().when(teamRepository).delete(any(Team.class));

        teamService.delete(1L);

        verify(teamRepository, times(1)).delete(team);
    }

}
