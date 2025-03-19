package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.security.JwtAuthFilter;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.TeamServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtService jwtService;

    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private UserServiceImpl userService;

    TeamController(JwtService jwtService, JwtAuthFilter jwtAuthFilter) {
        this.jwtService = jwtService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Create a new team
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(
            @RequestParam String name,
            @RequestParam String googleId, HttpServletRequest req) {
        Optional<User> user = userService.findByUserGoogleId(googleId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid user token.");
        }
        if (!user.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to create teams.");
        }
        Optional<Team> existingTeam = teamService.findByName(name);
        if (existingTeam.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("A team with the name '%s' already exists.", name));
        }
        Team newTeam = new Team(null, name);

        User actingUser;
        try {
            actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Team savedTeam = teamService.save(newTeam, actingUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeam);
    }

    // Get a team's name by their ID.
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamById(@PathVariable Long teamId) {
        Optional<Team> team = teamService.findByTeamId(teamId);
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(team.get());
    }

    // Get all the teams
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(teams);
    }

    // Update a team name, only possible if you're an admin.
    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(
            @PathVariable Long teamId,
            @RequestParam String newName,
            @RequestParam String googleId,
            HttpServletRequest req) {

        Optional<User> user = userService.findByUserGoogleId(googleId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid user token.");
        }
        if (!user.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to update team names.");
        }

        Optional<Team> teamOpt = teamService.findByTeamId(teamId);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found.");
        }
        if (teamService.findByName(newName).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A team with this name already exists.");
        }

        User actingUser;
        try {
            actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Team team = teamOpt.get();
        team.setName(newName);
        teamService.save(team, actingUser);

        return ResponseEntity.status(HttpStatus.OK).body(team);
    }

    // Delete a team
    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId, @RequestParam String googleId,
            HttpServletRequest req) {
        Optional<User> user = userService.findByUserGoogleId(googleId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid user token.");
        }
        if (!user.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to delete teams.");
        }
        Optional<Team> team = teamService.findByTeamId(teamId);
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found.");
        }

        User actingUser;
        try {
            actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        teamService.delete(teamId, actingUser);
        return ResponseEntity.ok(String.format("Team '%s' has been deleted.", team.get().getName()));
    }

}
