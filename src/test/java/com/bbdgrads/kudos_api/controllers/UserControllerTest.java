package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.UserRepository;
import com.bbdgrads.kudos_api.service.TeamService;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import com.sun.jdi.InvalidTypeException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class UserControllerTest {
    @Mock
    UserServiceImpl userService;
    @Mock
    TeamService teamService;

    @Mock
    User user;

    @Mock
    Team team;

    @InjectMocks
    UserController userController;

    @Test
    public void createUserIsSuccessful() {
        ResponseEntity<?> responseEntity = new ResponseEntity<>
                ("User created", HttpStatus.CREATED);
        assertEquals(
                responseEntity.toString(),
                userController.createUser(user.getUsername(), user.getGoogleId()).toString()
        );

    }
    @Test
    public void createUserIsSuccessfull() {
        ResponseEntity<String> entity = new ResponseEntity<>
                ("User created", HttpStatus.OK);
        assertEquals(
                entity.toString(),
                userController.createUser("JohnDoe", "googleToken").toString());
    };

//    @Test
//    public void createUserHasAnExistingUser() {
//        ResponseEntity<String> responseEntity = new ResponseEntity<>
//                ("User exists already", HttpStatus.CONFLICT);
////        Mockito.when(userController.createUser("JohnDoe", "googleToken")).thenReturn(user);
//        Mockito.doReturn(user).when(userController.createUser("JohnDoe", "googleToken"));
////        userController.createUser("JohnDoe", "googleToken");
//        userService.save(user);
//        assertEquals(
//                responseEntity.toString(),
//                userController.createUser("JohnDoe", "googleToken").toString()
//        );
//    }

//    @Test
//    void createUserFailsDueToEmptyStringValue() throws IllegalArgumentException {
//        InvalidTypeException thrown = assertThrows(
//                InvalidTypeException.class,
//                () -> userController.createUser("", ""),
//                "Expected createUser function to throw IllegalArgumentException but it didn't."
//        );
//        assertTrue(thrown.getMessage().contains("IllegalArgumentException"));
//        ResponseEntity<?> responseEntity = new ResponseEntity<>
//                ("User not created because empty values have been entered.", HttpStatus.BAD_REQUEST);
//        assertEquals(
//                responseEntity.toString(),
//                userController.createUser("", ""));
//    }
//
//    @Test
//    void getUserReturnsSuccessfully() {
//        //TODO
//    }
//
    @Test
    public void addUserToTeamSuccessfullyAddsUser(){
        ResponseEntity<String> responseEntity = new ResponseEntity<>("User's team updated successfully.", HttpStatus.OK);
        assertEquals(
                responseEntity.toString(),
                userController.addUserToTeam(user.getUsername(), team.getName()).toString());
    }

    @Test
    public void addUserToTeamFailsToAddUser(){
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                ("Either the team does not exist or the user does not exist.", HttpStatus.NOT_FOUND);
        assertEquals(
                responseEntity.toString(),
                userController.addUserToTeam(user.getUsername(), team.getName()).toString());
    }

    @Test
    public void deleteUserByUsernameDeletesUserSuccessfully(){
        userService.save(user);
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                (String.format("User %s has been deleted.", user.getUsername()), HttpStatus.OK);
        assertEquals(
                responseEntity.toString(),
                userController.deleteUserByUsername(user.getUsername()));
        assertNull(user.getGoogleId());
    }

    @Test
    public void deleteUserByUsernameWhenUserDoesNotExistReturnsSuccessfully(){
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                (String.format("User with username %s does not exist.", user.getUsername()), HttpStatus.OK);
        assertEquals(
                responseEntity.toString(),
                userController.deleteUserByUsername(user.getUsername()));
        assertNull(user.getGoogleId());
    }

//    @Test
//    public void deleteUserByUsernameFails() {
//        InvalidTypeException thrown = assertThrows(
//                InvalidTypeException.class,
//                () -> userController.deleteUser(HttpServletRequest),
//                "Expected deleteUser function to throw InvalidTypeException but it didn't."
//        );
//        assertTrue(thrown.getMessage().contains("InvalidTypeException"));
//    }

//    @Test
//    public void deleteUserWhenUserIsPresentReturnSuccessfully(){
//        userController.createUser(user.getUsername(), user.getGoogleId());
////        Mockito.when(userController.deleteUser(user.getGoogleToken())).thenReturn(user);
//        ResponseEntity<String> responseEntity = new ResponseEntity<>
//                (String.format("User %s has been deleted.", user.getUsername()), HttpStatus.OK);
//        assertEquals(
//                responseEntity.toString(),
//                userController.deleteUser(user.getGoogleId()));
//        assertNull(user.getGoogleId());
//    }
//
//    @Test
//    public void deleteUserWhenUserDoesNotExistReturnsSuccessfully() {
//        ResponseEntity<String> responseEntity = new ResponseEntity<>
//                (String.format("User with token %s does not exist.", user.getGoogleId()), HttpStatus.NOT_FOUND);
//        assertEquals(
//                responseEntity,
//                userController.deleteUser(user.getGoogleId()));
//        assertNull(user.getGoogleId());
//    }

//    @Test
//    public void deleteUserFails() {
//        InvalidTypeException thrown = assertThrows(
//                InvalidTypeException.class,
//                () -> userController.deleteUser("1234abd"),
//                "Expected deleteUser function to throw InvalidTypeException but it didn't."
//        );
//        assertTrue(thrown.getMessage().contains("InvalidTypeException"));
//    }
}
