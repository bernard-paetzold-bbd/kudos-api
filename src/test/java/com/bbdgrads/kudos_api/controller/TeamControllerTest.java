package com.bbdgrads.kudos_api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bbdgrads.kudos_api.controllers.TeamController;
import com.bbdgrads.kudos_api.controllers.UserController;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.TeamRepository;
import com.bbdgrads.kudos_api.repository.UserRepository;
import com.bbdgrads.kudos_api.service.AuthService;
import com.bbdgrads.kudos_api.service.LogService;
import com.bbdgrads.kudos_api.service.TeamService;
import com.bbdgrads.kudos_api.service.UserService;
import com.bbdgrads.kudos_api.service.UserServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@RunWith(MockitoJUnitRunner.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TeamController teamController;

    private User adminTestUser;
    private User nonAdminTestUser;

    private Team testTeam;

    @BeforeEach
    public void setUp() {
        // mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        adminTestUser = new User();
        adminTestUser.setUsername("testAdmin");
        adminTestUser.setGoogleToken("testAdminGoogleToken123");
        adminTestUser.setAdmin(true);
        userRepository.save(adminTestUser);

        nonAdminTestUser = new User();
        nonAdminTestUser.setUsername("testNonAdmin");
        nonAdminTestUser.setGoogleToken("testNonAdminGoogleToken123");
        userRepository.save(nonAdminTestUser);

        testTeam = new Team();
        testTeam.setName("testTeam");
        teamRepository.save(testTeam);

    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    public void testCreateTeamByNonAdminUser() throws Exception {

        mockMvc.perform(post("/teams/create")
                .param("name", "team-1")
                .param("googleToken", nonAdminTestUser.getGoogleToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You do not have permission to create teams."));

    }

    @Test
    public void testCreateTeamByAdminUser() throws Exception {
        mockMvc.perform(post("/teams/create")
                .param("name", "team-2")
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isCreated()).andExpect(
                        jsonPath("$.name").value("team-2"));
    }

    @Test
    public void testCreateTeamAlreadyExists() throws Exception {
        mockMvc.perform(post("/teams/create")
                .param("name", "testTeam")
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isConflict()).andExpect(
                        content().string("A team with the name 'testTeam' already exists."));
    }

    @Test
    public void testGetTeamById() throws Exception {
        mockMvc.perform(get("/teams/" + testTeam.getTeamId()))
                .andExpect(status().isOk()).andExpect(
                        jsonPath("$.name").value(testTeam.getName()));
    }

    @Test
    public void testGetTeamByInvalidId() throws Exception {
        mockMvc.perform(get("/teams/0000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTeams() throws Exception {
        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk()).andExpect(
                        jsonPath("$[0].name").value(testTeam.getName()));
    }

    @Test
    public void testUpdateTeamNonAdminUser() throws Exception {
        mockMvc.perform(put("/teams/" + testTeam.getTeamId())
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("newName", "NewTestTeamName")
                .param("googleToken", nonAdminTestUser.getGoogleToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You do not have permission to update team names."));
    }

    @Test
    public void testUpdateTeamNameExists() throws Exception {
        mockMvc.perform(put("/teams/" + testTeam.getTeamId())
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("newName", "testTeam")
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isConflict())
                .andExpect(content().string("A team with this name already exists."));
    }

    @Test
    public void testUpdateTeamNotFound() throws Exception {
        mockMvc.perform(put("/teams/0000")
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("newName", "NewTestTeamName")
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Team not found."));
    }

    @Test
    public void testUpdateTeamSuccess() throws Exception {
        mockMvc.perform(put("/teams/" + testTeam.getTeamId())
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("newName", "NewTestTeamName")
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTeamNonAdminUser() throws Exception {
        mockMvc.perform(delete("/teams/" + testTeam.getTeamId())
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("googleToken", nonAdminTestUser.getGoogleToken()))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You do not have permission to delete teams."));
    }

    @Test
    public void testDeleteTeamNotFound() throws Exception {
        mockMvc.perform(delete("/teams/0000")
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Team not found."));
    }

    @Test
    public void testDeleteTeamSuccess() throws Exception {
        mockMvc.perform(delete("/teams/" + testTeam.getTeamId())
                .param("teamId", Long.toString(testTeam.getTeamId()))
                .param("googleToken", adminTestUser.getGoogleToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Team 'testTeam' has been deleted."));
    }

}
