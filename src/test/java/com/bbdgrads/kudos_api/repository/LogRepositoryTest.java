package com.bbdgrads.kudos_api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvent;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LogRepositoryTest {

    @Autowired
    LogRepository logRepository;
    @Autowired
    KudoRepository kudoRepository;
    @Autowired
    UserRepository userRepository;

    private Log log;
    private User actingUser;
    private User targetUser;
    private Kudo testKudo;
    private LogEvent logEvent;

    @BeforeEach
    public void setUp() {
        actingUser = new User("john doe", "john123", false);
        targetUser = new User("jane doe", "jane123", false);

        userRepository.save(actingUser);
        userRepository.save(targetUser);

        testKudo = new Kudo();
        testKudo.setMessage("Good job!");
        testKudo.setSendingUser(targetUser);
        testKudo.setTargetUser(targetUser);
        kudoRepository.save(testKudo);

        log = new Log();
        log.setActingUser(actingUser);
        log.setTargetUser(targetUser);
        log.setKudo(testKudo);
        log.setEventId(1);
        log.setLogTime(LocalDateTime.now());
        logRepository.save(log);
    }

    @AfterEach
    public void tearDown() {
        logRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateLog() {
        Log log = new Log();
        log.setActingUser(actingUser);
        log.setTargetUser(targetUser);
        log.setKudo(testKudo);

        logRepository.save(log);

        var foundLog = logRepository.findById(log.getLogId());
        assertTrue(foundLog.isPresent());

        assertEquals(log.getActingUser().getUsername(),
                foundLog.get().getActingUser().getUsername());
        assertEquals(log.getTargetUser().getUsername(),
                foundLog.get().getTargetUser().getUsername());
        assertEquals(log.getKudo().getMessage(),
                foundLog.get().getKudo().getMessage());

    }

    @Test
    public void testReadLog() {

        var foundLog = logRepository.findById(log.getLogId());
        assertTrue(foundLog.isPresent());

        assertEquals(log.getActingUser().getUsername(),
                foundLog.get().getActingUser().getUsername());
        assertEquals(log.getTargetUser().getUsername(),
                foundLog.get().getTargetUser().getUsername());
        assertEquals(log.getKudo().getMessage(),
                foundLog.get().getKudo().getMessage());
    }

    @Test
    public void testFindByActingUser() {
        var foundLogs = logRepository.findByActingUser(log.getActingUser());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        var foundLog = foundLogs.get(0);

        assertEquals(log.getActingUser().getUsername(),
                foundLog.getActingUser().getUsername());
        assertEquals(log.getTargetUser().getUsername(),
                foundLog.getTargetUser().getUsername());
        assertEquals(log.getKudo().getMessage(),
                foundLog.getKudo().getMessage());
        assertEquals(foundLog.getEventId(), log.getEventId());
    }

    @Test
    public void testFindByEventId() {
        var foundLogs = logRepository.findByEventId(log.getEventId());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        var foundLog = foundLogs.get(0);

        assertEquals(log.getActingUser().getUsername(),
                foundLog.getActingUser().getUsername());
        assertEquals(log.getTargetUser().getUsername(),
                foundLog.getTargetUser().getUsername());
        assertEquals(log.getKudo().getMessage(),
                foundLog.getKudo().getMessage());
        assertEquals(foundLog.getEventId(), log.getEventId());
    }

    @Test
    public void testFindByLogId() {
        var foundLogs = logRepository.findByLogId(log.getLogId());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        var foundLog = foundLogs.get(0);

        assertEquals(log.getActingUser().getUsername(),
                foundLog.getActingUser().getUsername());
        assertEquals(log.getTargetUser().getUsername(),
                foundLog.getTargetUser().getUsername());
        assertEquals(log.getKudo().getMessage(),
                foundLog.getKudo().getMessage());
        assertEquals(foundLog.getEventId(), log.getEventId());
    }

    @Test
    public void testFindByTargetUser() {
        var foundLogs = logRepository.findByTargetUser(log.getTargetUser());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        var foundLog = foundLogs.get(0);

        assertEquals(log.getActingUser().getUsername(),
                foundLog.getActingUser().getUsername());
        assertEquals(log.getTargetUser().getUsername(),
                foundLog.getTargetUser().getUsername());
        assertEquals(log.getKudo().getMessage(),
                foundLog.getKudo().getMessage());
        assertEquals(foundLog.getEventId(), log.getEventId());
    }

    @Test
    public void testUpdateLog() {

        var foundLog = logRepository.findById(log.getLogId());
        assertTrue(foundLog.isPresent());

        foundLog.get().setEventId(44);
        logRepository.save(log);

        var updatedLog = logRepository.findById(log.getLogId());

        assertTrue(foundLog.isPresent());

        assertEquals(updatedLog.get().getEventId(), 44);
    }

    @Test
    public void testDeleteLog() {

        var foundLog = logRepository.findById(log.getLogId());
        assertTrue(foundLog.isPresent());

        logRepository.delete(foundLog.get());

        var deletedLog = logRepository.findById(log.getLogId());
        assertFalse(deletedLog.isPresent());
    }

}
