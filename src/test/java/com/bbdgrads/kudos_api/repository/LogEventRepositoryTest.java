package com.bbdgrads.kudos_api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbdgrads.kudos_api.model.LogEvent;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LogEventRepositoryTest {

    @Autowired
    LogEventRepository logEventRepository;

    private LogEvent logEvent;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void setUp() {
        logEvent = new LogEvent();
        logEvent.setDescription("Created new user.");
        logEventRepository.save(logEvent);
    }

    @AfterEach
    public void tearDown() {
        logEventRepository.deleteAll();
    }

    @Test
    public void testCreateLogEvent() {
        var logEvent = new LogEvent();
        logEvent.setDescription("Deleted user.");

        logEventRepository.save(logEvent);

        var foundLogEvent = logEventRepository.findById(logEvent.getEventId());
        assertTrue(foundLogEvent.isPresent());

        assertEquals(logEvent.getDescription(), foundLogEvent.get().getDescription());
    }

    @Test
    public void testReadLogEvent() {
        var foundLogEvent = logEventRepository.findById(logEvent.getEventId());
        assertTrue(foundLogEvent.isPresent());

        assertEquals(logEvent.getDescription(), foundLogEvent.get().getDescription());
    }

    @Test
    public void testUpdateLogEvent() {

        var foundLogEvent = logEventRepository.findById(logEvent.getEventId());
        assertTrue(foundLogEvent.isPresent());

        foundLogEvent.get().setDescription("Updated description");
        ;

        logEventRepository.save(logEvent);

        var updatedLogEvent = logEventRepository.findById(logEvent.getEventId());
        assertTrue(updatedLogEvent.isPresent());

        assertTrue(foundLogEvent.isPresent());
        assertEquals(foundLogEvent.get().getDescription(), updatedLogEvent.get().getDescription());
    }

    @Test
    public void testDeleteLogEvent() {

        var foundLogEvent = logEventRepository.findById(logEvent.getEventId());
        assertTrue(foundLogEvent.isPresent());

        logEventRepository.delete(foundLogEvent.get());

        var deletedUser = logEventRepository.findById(logEvent.getEventId());
        assertFalse(deletedUser.isPresent());
    }

}
