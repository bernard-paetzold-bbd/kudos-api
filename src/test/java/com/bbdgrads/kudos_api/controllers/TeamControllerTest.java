package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.security.JwtAuthFilter;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.TeamServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {
    @Mock
    private TeamServiceImpl teamServiceImpl;

    private final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TeamController teamController;

    private Team team;
    private User adminUser;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        team = new Team(12345L, "team1");
        adminUser = new User("JohnDoe", "googleID1", true);
        user = new User("JaneDoe", "googleID1", false);
        mockHttpServletRequest.addHeader("Bearer ", user);
    }

    @Test
    public void createTeamIsSuccessful() {
        Mockito.when(userServiceImpl.findByUserGoogleId(adminUser.getGoogleId())).thenReturn(Optional.ofNullable(adminUser));
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                (HttpStatus.CREATED);
        assertEquals(responseEntity,
                teamController.createTeam(team.getName(), adminUser.getGoogleId(), mockHttpServletRequest));
    }

    @Test
    public void createTeamFailsDueToInvalidUserToken() {
        ResponseEntity<String> invalidUserResponse = new ResponseEntity<>
                ("Invalid user token.", HttpStatus.UNAUTHORIZED);
        assertEquals(invalidUserResponse,
                teamController.createTeam(team.getName(), user.getGoogleId(), mockHttpServletRequest));
    }

    @Test
    public void createTeamFailsDueToNotHavingPermissions() {
        Mockito.when(userServiceImpl.findByUserGoogleId(user.getGoogleId())).thenReturn(Optional.ofNullable(user));
        ResponseEntity<String> noPermissionsResponse = new ResponseEntity<>
                ("You do not have permission to create teams.",HttpStatus.FORBIDDEN);
        assertEquals(noPermissionsResponse,
                teamController.createTeam(team.getName(), user.getGoogleId(), mockHttpServletRequest));
    }

    @Test
    public void getTeamByIdIsSuccessful() {
        ResponseEntity<?> responseEntity = new ResponseEntity<>
                (team, HttpStatus.OK);
        Mockito.when(teamServiceImpl.findByTeamId(team.getTeamId())).thenReturn(Optional.ofNullable(team));
        assertEquals(responseEntity,
                teamController.getTeamById(team.getTeamId()));
    }

    @Test
    public void getTeamByIdReturnsExceptionWhenServiceIsNull() {
        TeamController teamController1 = new TeamController(new JwtService(), new JwtAuthFilter());
        var msg = assertThrows(NullPointerException.class,
                () -> teamController1.getTeamById(team.getTeamId()));
        assertEquals("this.teamService is null.",
                msg.getMessage());
    }

    @Test
    public void getByIdFails() {
        ResponseEntity<?> responseEntity = new ResponseEntity<>
                ("Team not found.", HttpStatus.NOT_FOUND);
        assertEquals(responseEntity,
                teamController.getTeamById(team.getTeamId()));
    }

    @Test
    public void getAllTeams() {
        ResponseEntity<List<Team>> responseEntity = new ResponseEntity<>
                (List.of(), HttpStatus.OK);
        assertEquals(responseEntity,
                teamController.getAllTeams());
    }
}

