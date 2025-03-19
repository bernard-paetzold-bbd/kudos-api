package com.bbdgrads.kudos_api.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvent;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.LogRepository;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogServiceImpl logService;

    private Log log;
    private User actingUser;
    private User targetUser;
    private Kudo testKudo;
    private LogEvent logEvent;

    @Before
    public void setUp() {
        actingUser = new User("john doe", "john123", false);
        targetUser = new User("jane doe", "jane123", false);

        testKudo = new Kudo();
        testKudo.setMessage("Good job!");
        testKudo.setSendingUser(targetUser);
        testKudo.setTargetUser(targetUser);

        logEvent = new LogEvent();
        logEvent.setDescription("Created new user.");

        log = new Log();
        log.setActingUser(actingUser);
        log.setTargetUser(targetUser);
        log.setKudo(testKudo);
        log.setEventId(1);
        log.setLogTime(LocalDateTime.now());
        log.setVerboseLog("created user");

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFindByActingUser() {

        logRepository.count();

        when(logRepository.findByActingUser(any(User.class))).thenReturn(Lists.list(log));

        var foundLogs = logService.findByActingUser(log.getActingUser());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        Log foundLog = foundLogs.get(0);

        assertEquals(log.getActingUser().getUsername(),
                foundLog.getActingUser().getUsername());

        verify(logRepository, times(1)).findByActingUser(log.getActingUser());
    }

    @Test
    public void testFindByEvent() {
        when(logRepository.findByEventId(any(Integer.class))).thenReturn(Lists.list(log));

        var foundLogs = logService.findByEventId(log.getEventId());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        Log foundLog = foundLogs.get(0);

        assertEquals(log.getEventId(), foundLog.getEventId());

        verify(logRepository, times(1)).findByEventId(log.getEventId());
    }

    @Test
    public void testFindByLogId() {
        when(logRepository.findByLogId(any(Long.class))).thenReturn(Lists.list(log));

        var foundLogs = logService.findByLogId(1L);

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        Log foundLog = foundLogs.get(0);

        assertEquals(log.getLogId(), foundLog.getLogId());

        verify(logRepository, times(1)).findByLogId(1L);
    }

    @Test
    public void testFindByTargetUser() {
        when(logRepository.findByTargetUser(any(User.class))).thenReturn(Lists.list(log));

        var foundLogs = logService.findByTargetUser(log.getTargetUser());

        assertNotNull(foundLogs);
        assertFalse(foundLogs.isEmpty());

        Log foundLog = foundLogs.get(0);

        assertEquals(log.getTargetUser().getUsername(),
                foundLog.getTargetUser().getUsername());

        verify(logRepository, times(1)).findByTargetUser(log.getTargetUser());
    }

    @Test
    public void testSave() {
        when(logRepository.save(any(Log.class))).thenReturn(log);
        logService.save(log);
        verify(logRepository, times(1)).save(log);
    }

}
