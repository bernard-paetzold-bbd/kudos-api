package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.service.LogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {
    @Autowired
    @Mock
    private LogServiceImpl logServiceImpl;

    @InjectMocks
    private LogController logController;

    private Log log;
    private User user1;
    private User user2;
    private Kudo kudo;
    private Team team;

    @BeforeEach
    public void setUp() {
        user1 = new User("JohnDoe", "googleID", false);
        user2 = new User("JaneDoe", "googleID1", false);
        kudo = new Kudo("message", user1, user2);
        team = new Team(1234L, "team1");
        log = new Log(1234L, user1, user2, kudo, team, 1, "Log entry", LocalDateTime.now());
    }

    @Test
    public void createLogReturnSuccessfully() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>
                ("Saved", HttpStatus.OK);
        assertEquals(responseEntity,
                logController.createLog(user1.getUserId(), user1.getUsername()));
    }

    @Test
    public void createLogShouldReturnNullExceptionWhenLogServiceIsNull() {
        LogController logController1 = new LogController();
        var msg = assertThrows(NullPointerException.class,
                () -> logController1.createLog(user1.getUserId(), user1.getUsername()));
        assertEquals("this.logService is null.",
                msg.getMessage());
    }

    @Test
    public void getUserLogsReturnsSuccessfully() {
        List<Log> logs = new ArrayList<>();
        assertEquals(logs,
                logController.getUserLogs(user1.getUserId()));
    }

}
