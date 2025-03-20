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

import com.bbdgrads.kudos_api.model.Team;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    private Team team;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void setUp() {
        team = new Team();
        team.setName("team-xyz");
        teamRepository.save(team);
    }

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    public void testCreateTeam() {
        var team = new Team();
        team.setName("team-rst");

        teamRepository.save(team);

        var foundTeam = teamRepository.findById(team.getTeamId());
        assertTrue(foundTeam.isPresent());

        assertEquals(team.getName(), foundTeam.get().getName());
    }

    @Test
    public void testReadTeam() {
        var foundTeam = teamRepository.findById(team.getTeamId());
        assertTrue(foundTeam.isPresent());

        assertEquals(team.getName(), foundTeam.get().getName());
    }

    @Test
    public void testFindByName() {
        var foundTeam = teamRepository.findByName(team.getName());
        assertTrue(foundTeam.isPresent());

        assertEquals(team.getName(), foundTeam.get().getName());
    }

    @Test
    public void testUpdateTeam() {

        var foundTeam = teamRepository.findById(team.getTeamId());
        assertTrue(foundTeam.isPresent());

        foundTeam.get().setName("Updated description");
        ;

        teamRepository.save(team);

        var updatedTeam = teamRepository.findById(team.getTeamId());
        assertTrue(updatedTeam.isPresent());

        assertTrue(foundTeam.isPresent());
        assertEquals(foundTeam.get().getName(), updatedTeam.get().getName());
    }

    @Test
    public void testDeleteTeam() {

        var foundTeam = teamRepository.findById(team.getTeamId());
        assertTrue(foundTeam.isPresent());

        teamRepository.delete(foundTeam.get());

        var deletedUser = teamRepository.findById(team.getTeamId());
        assertFalse(deletedUser.isPresent());
    }

}
