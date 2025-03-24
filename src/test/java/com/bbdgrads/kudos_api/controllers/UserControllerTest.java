package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.TeamRepository;
import com.bbdgrads.kudos_api.repository.UserRepository;
import com.bbdgrads.kudos_api.security.JwtAuthFilter;
import com.bbdgrads.kudos_api.service.*;
import com.sun.jdi.InvalidTypeException;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
//@RunWith(SpringJUnit4ClassRunner.class)
class UserControllerTest {
    @Autowired
    @Mock
    private UserRepository userRepository;
    @Autowired
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private LogService logService;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    TeamServiceImpl teamService;

    @InjectMocks
    UserController userController;

    private User user;
    private Team team;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("JohnDoe", "googleID", false);
        team = new Team(1234L, "team1");
        teamRepository.save(team);

    }

    @Mock
    UserRepository repository;

    @Test
    public void createUserIsSuccessfully() {
        ResponseEntity<String> entity = new ResponseEntity<>
                (HttpStatus.CREATED);
        assertEquals(
                entity.toString(),
                userController.createUser(user.getUsername(), user.getGoogleId()).toString());
    }

    @Test
    public void addUserToTeamThrowsNullExceptionWhenUserIsNull() {
        UserController userController1 = new UserController(new JwtAuthFilter(), new JwtService());
        var msg = assertThrows(NullPointerException.class,
                () -> userController1.addUserToTeam(user.getUsername(), team.getName()));
        assertEquals("Either this.userService is null or this.teamService is null",
                msg.getMessage());
    }

    @Test
    public void addUserToTeamSuccessfullyAddsUser() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                ("User's team updated successfully.", HttpStatus.OK);
        Mockito.when(userServiceImpl.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        Mockito.when(teamService.findByName(team.getName())).thenReturn(Optional.ofNullable(team));
        Mockito.when(userServiceImpl.updateUserTeam(user.getUserId(), team.getTeamId())).thenReturn(true);
        assertEquals(
                responseEntity.toString(),
                userController.addUserToTeam(user.getUsername(), team.getName()).toString());
    }

    @Test
    public void addUserToTeamFailsToAddUser() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                ("Either the team does not exist or the user does not exist.", HttpStatus.NOT_FOUND);
        assertEquals(
                responseEntity.toString(),
                userController.addUserToTeam(user.getUsername(), team.getName()).toString());
    }

//    @Test
//    public void deleteUserByUsernameDeletesUserSuccessfully() {
//        userServiceImpl.save(user);
//        ResponseEntity<String> responseEntity = new ResponseEntity<>
//                (String.format("User %s has been deleted.", user.getUsername()), HttpStatus.OK);
//        assertEquals(
//                responseEntity.toString(),
//                userController.deleteUserByUsername(user.getUsername()));
//        assertNull(user.getGoogleId());
//    }

    @Test
    public void deleteUserByUsernameWhenUserDoesNotExistReturnsSuccessfully() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                (String.format("User with username %s does not exist.", user.getUsername()), HttpStatus.NOT_FOUND);
        assertEquals(
                responseEntity.toString(),
                userController.deleteUserByUsername(user.getUsername()).toString());
        assertEquals(Optional.empty(),
                userRepository.findByUsername(user.getUsername()));
    }

    @Test
    public void deleteUserByUsernameFails() {
        UserController userController1 = new UserController(new JwtAuthFilter(), new JwtService());
        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> userController1.deleteUserByUsername(user.getUsername()),
                "Expected deleteUser function to throw NullPointerException but it didn't."
        );
    }
}
