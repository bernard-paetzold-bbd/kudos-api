package com.bbdgrads.kudos_api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("john doe", "john123", false);
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateUser() {
        User user = new User("jane doe", "JaneId12345", false);

        userRepository.save(user);

        var foundUser = userRepository.findById(user.getUserId());
        assertTrue(foundUser.isPresent());

        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getGoogleToken(), foundUser.get().getGoogleToken());
        assertEquals(user.isAdmin(), foundUser.get().isAdmin());
    }

    @Test
    public void testReadUser() {

        var foundUser = userRepository.findById(testUser.getUserId());
        assertTrue(foundUser.isPresent());

        assertEquals(testUser.getUsername(), foundUser.get().getUsername());
        assertEquals(testUser.getGoogleToken(), foundUser.get().getGoogleToken());
        assertEquals(testUser.isAdmin(), foundUser.get().isAdmin());
    }

    @Test
    public void testFindByUserId() {
        var foundUser = userRepository.findByUserId(testUser.getUserId());
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getUsername(), foundUser.get().getUsername());
        assertEquals(testUser.getGoogleToken(), foundUser.get().getGoogleToken());
        assertEquals(testUser.isAdmin(), foundUser.get().isAdmin());
    }

    @Test
    public void testFindByGoogleToken() {
        var foundUser = userRepository.findByGoogleToken("invalid_token");
        assertFalse(foundUser.isPresent());

        foundUser = userRepository.findByGoogleToken(testUser.getGoogleToken());
        assertTrue(foundUser.isPresent());

        assertEquals(testUser.getUsername(), foundUser.get().getUsername());
        assertEquals(testUser.getGoogleToken(), foundUser.get().getGoogleToken());
        assertEquals(testUser.isAdmin(), foundUser.get().isAdmin());
    }

    @Test
    public void testFindByUsername() {
        var foundUser = userRepository.findByGoogleToken("invalid_user_name");
        assertFalse(foundUser.isPresent());

        foundUser = userRepository.findByUsername(testUser.getUsername());
        assertTrue(foundUser.isPresent());

        assertEquals(testUser.getUsername(), foundUser.get().getUsername());
        assertEquals(testUser.getGoogleToken(), foundUser.get().getGoogleToken());
        assertEquals(testUser.isAdmin(), foundUser.get().isAdmin());
    }

    @Test
    public void testUpdateUserTeam() {
        var foundUser = userRepository.findByUserId(testUser.getUserId());
        assertTrue(foundUser.isPresent());

        var team = new Team();
        team.setName("xyz-team");
        teamRepository.save(team);

        userRepository.updateUserTeam(foundUser.get().getUserId(), team);

        entityManager.refresh(foundUser.get());

        var updatedUser = userRepository.findByUserId(foundUser.get().getUserId());
        assertTrue(updatedUser.isPresent());

        var updatedTeam = foundUser.get().getTeam();
        assertNotNull(updatedTeam);

        assertEquals(updatedTeam.getName(), "xyz-team");
    }

    @Test
    public void testUpdateUser() {

        var foundUser = userRepository.findById(testUser.getUserId());
        assertTrue(foundUser.isPresent());

        foundUser.get().setAdmin(true);
        userRepository.save(testUser);

        foundUser = userRepository.findById(testUser.getUserId());

        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get().isAdmin());
    }

    @Test
    public void testDeleteUser() {

        var foundUser = userRepository.findById(testUser.getUserId());
        assertTrue(foundUser.isPresent());

        userRepository.delete(foundUser.get());

        var deletedUser = userRepository.findById(testUser.getUserId());
        assertFalse(deletedUser.isPresent());
    }

}
