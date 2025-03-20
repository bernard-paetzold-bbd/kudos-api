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

import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.TeamRepository;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private LogServiceImpl logService;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;

    private User user;

    @Before
    public void setUp() {
        team = new Team();
        team.setName("team-zkl");

        user = new User();
        user.setUsername("john doe");
        user.setGoogleId("johnGoogle1234");
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
        doNothing().when(logService).save(any(Log.class));

        teamService.save(team, user);

        verify(teamRepository, times(1)).save(team);
    }

    @Test
    public void testDeleteTeam() {

        when(teamRepository.findById(any(Long.class))).thenReturn(Optional.of(team));
        doNothing().when(teamRepository).delete(any(Team.class));
        doNothing().when(logService).save(any(Log.class));

        teamService.delete(1L, user);

        verify(teamRepository, times(1)).delete(team);
    }

}
