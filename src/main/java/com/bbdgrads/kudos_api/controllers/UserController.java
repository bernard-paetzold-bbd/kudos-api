package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.TeamServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.security.JwtAuthFilter;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {
        private final JwtAuthFilter jwtAuthFilter;

        private final JwtService jwtService;

        @Autowired
        private UserServiceImpl userService;
        @Autowired
        private TeamServiceImpl teamService;

        public UserController(JwtAuthFilter jwtAuthFilter, JwtService jwtService) {
                this.jwtAuthFilter = jwtAuthFilter;
                this.jwtService = jwtService;
        }

        @PostMapping("/create")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> createUser(
                        @RequestParam String name,
                        @RequestParam String googleId) {

                Optional<User> existingUserToken = userService.findByUserGoogleId(googleId);
                Optional<User> existingUserUsername = userService.findByUsername(name);
                if (existingUserToken.isPresent()) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                        .body(String.format("A user with the ID %s already exists.", googleId));
                }

                User newUser = new User(name, googleId, false);
                User savedUser = userService.save(newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }

        @GetMapping("/{user_id_token}")
        public String getUser(@PathVariable String userIdToken) {
                // TODO: Validate the user id token here and map to correct user_id
                // https://developers.google.com/identity/sign-in/web/backend-auth

                return new String();
        }

        @PutMapping("/addUserToTeam")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<String> addUserToTeam(@RequestParam String username, @RequestParam String team_name) {
                var userOpt = userService.findByUsername(username);
                var teamOpt = teamService.findByName(team_name);
                if (userOpt.isEmpty() || teamOpt.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body("Either the team does not exist or the user does not exist.");
                }
                boolean success = userService.updateUserTeam(userOpt.get().getUserId(), teamOpt.get().getTeamId());
                if (!success) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body("Either the team does not exist or the user does not exist.");
                }
                return ResponseEntity.ok("User's team updated successfully.");
        }

        // For when a user wants to delete themselves.
        @DeleteMapping("/delete")
        public ResponseEntity<String> deleteUser(HttpServletRequest req) {
                User actingUser;
                try {
                        actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
                } catch (AccessDeniedException ex) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                } catch (EntityNotFoundException ex) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                var user = userService.findByUserGoogleId(actingUser.getGoogleId());
                if (user.isPresent()) {
                        userService.delete(user.get().getUserId());
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(String.format("User %s has been deleted.", user.get().getUsername()));
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(String.format("User with google ID %s does not exist.",
                                                actingUser.getGoogleId()));
        }

        // ADMIN permissions deletion of a user. Can only be performed by admin, admin
        // status checked via token. Will be stored in session?
        @DeleteMapping("/username/{username}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<String> deleteUserByUsername(
                        @PathVariable String username/*
                                                      * ,
                                                      * 
                                                      * @RequestParam String userGoogleId
                                                      */) {
                // Optional<User> adminUser = userService.findByUserGoogleId(userGoogleId);
                // if (adminUser.isEmpty() || !adminUser.get().isAdmin()) {
                // return ResponseEntity.status(HttpStatus.FORBIDDEN)
                // .body("Only admins can delete users.");
                // }
                Optional<User> userToDelete = userService.findByUsername(username);
                if (userToDelete.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(String.format("User with username %s does not exist.", username));
                }
                userService.delete(userToDelete.get().getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(String.format("User %s has been deleted.", username));
        }
}
