package com.bbdgrads.kudos_api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class KudoRepositoryTest {

    @Autowired
    KudoRepository kudoRepository;
    @Autowired
    UserRepository userRepository;

    private Kudo testKudo;
    private User sendingUser;
    private User targetUser;

    @BeforeEach
    public void setUp() {
        sendingUser = new User("john doe", "john123", false);
        targetUser = new User("jane doe", "jane123", false);

        userRepository.save(sendingUser);
        userRepository.save(targetUser);

        testKudo = new Kudo();

        testKudo.setMessage("Good job!");
        testKudo.setSendingUser(sendingUser);
        testKudo.setTargetUser(targetUser);

        kudoRepository.save(testKudo);
    }

    @AfterEach
    public void tearDown() {
        kudoRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateKudo() {

        Kudo kudo = new Kudo();

        kudo.setMessage("Great job!");
        kudo.setSendingUser(sendingUser);
        kudo.setTargetUser(targetUser);

        kudoRepository.save(kudo);

        var foundKudo = kudoRepository.findById(kudo.getKudoId());

        assertTrue(foundKudo.isPresent());

        assertEquals(kudo.getMessage(), foundKudo.get().getMessage());
        assertEquals(kudo.getSendingUser().getUsername(), foundKudo.get().getSendingUser().getUsername());

    }

    @Test
    public void testGetKudo() {

        var foundKudo = kudoRepository.findById(testKudo.getKudoId());

        assertTrue(foundKudo.isPresent());

        assertEquals(testKudo.getMessage(), foundKudo.get().getMessage());
        assertEquals(testKudo.getSendingUser().getUsername(), foundKudo.get().getSendingUser().getUsername());
        assertEquals(testKudo.getTargetUser().getUsername(), foundKudo.get().getTargetUser().getUsername());

    }

    @Test
    public void testFindByTargetUser() {
        List<Kudo> foundKudos = kudoRepository.findByTargetUser(testKudo.getTargetUser());

        assertFalse(foundKudos.isEmpty());

        Kudo foundKudo = foundKudos.get(0);

        assertEquals(testKudo.getMessage(), foundKudo.getMessage());
        assertEquals(testKudo.getSendingUser().getUsername(), foundKudo.getSendingUser().getUsername());
        assertEquals(testKudo.getTargetUser().getUsername(), foundKudo.getTargetUser().getUsername());
    }

    @Test
    public void testFindBySendingUser() {
        List<Kudo> foundKudos = kudoRepository.findBySendingUser(testKudo.getSendingUser());

        assertFalse(foundKudos.isEmpty());

        Kudo foundKudo = foundKudos.get(0);

        assertEquals(testKudo.getMessage(), foundKudo.getMessage());
        assertEquals(testKudo.getSendingUser().getUsername(), foundKudo.getSendingUser().getUsername());
        assertEquals(testKudo.getTargetUser().getUsername(), foundKudo.getTargetUser().getUsername());
    }

    @Test
    public void testUpdateKudo() {

        var foundKudo = kudoRepository.findById(testKudo.getKudoId());

        assertTrue(foundKudo.isPresent());

        foundKudo.get().setMessage("Excellent!");

        kudoRepository.save(foundKudo.get());

        var updatedKudo = kudoRepository.findById(foundKudo.get().getKudoId());

        assertTrue(updatedKudo.isPresent());

        assertTrue(foundKudo.isPresent());
        assertEquals(updatedKudo.get().getMessage(), foundKudo.get().getMessage());
    }

    @Test
    public void testDeleteKudo() {

        var foundKudo = kudoRepository.findById(testKudo.getKudoId());

        assertTrue(foundKudo.isPresent());

        kudoRepository.delete(foundKudo.get());

        var deletedKudo = kudoRepository.findById(testKudo.getKudoId());
        assertFalse(deletedKudo.isPresent());

    }

}
