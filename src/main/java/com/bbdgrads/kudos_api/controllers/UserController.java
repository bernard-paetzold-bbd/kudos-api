package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.service.TeamServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bbdgrads.kudos_api.model.User;

import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TeamServiceImpl teamService;

    // TODO: Need to check the type of the google_id
    @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @RequestParam String name,
            @RequestParam String googleId) {

        Optional<User> existingUserToken = userService.findByUserGoogleId(googleId);
        Optional<User> existingUserUsername = userService.findByUsername(name);
        if (existingUserToken.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("A user with the ID %s already exists.", googleId));
        }
        else if (existingUserUsername.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(String.format("A user with the username %s already exists.", name));
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
    public ResponseEntity<String> addUserToTeam(@RequestParam String username, @RequestParam String team_name){
        var userOpt = userService.findByUsername(username);
        var teamOpt = teamService.findByName(team_name);
        if (userOpt.isEmpty() || teamOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Either the team does not exist or the user does not exist.");
        }
        boolean success = userService.updateUserTeam(userOpt.get().getUserId(), teamOpt.get().getTeamId());
        if (!success){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Either the team does not exist or the user does not exist.");
        }
        return ResponseEntity.ok("User's team updated successfully.");
    }

    // For when a user wants to delete themselves.
    @DeleteMapping("/{user_google_id}")
    public ResponseEntity<String> deleteUser(@PathVariable String userGoogleId) {
        // TODO: Validate the user id token here and map to correct user_id

        var user = userService.findByUserGoogleId(userGoogleId);
        if (user.isPresent()){
            userService.delete(user.get().getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(String.format("User %s has been deleted.",user.get().getUsername()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User with google ID %s does not exist.",userGoogleId));
    }

    // ADMIN permissions deletion of a user. Can only be performed by admin, admin status checked via token. Will be stored in session?
    @DeleteMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserByUsername(
            @PathVariable String username/*,
            @RequestParam String userGoogleId*/) {
//        Optional<User> adminUser = userService.findByUserGoogleId(userGoogleId);
//        if (adminUser.isEmpty() || !adminUser.get().isAdmin()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Only admins can delete users.");
//        }
        Optional<User> userToDelete = userService.findByUsername(username);
        if (userToDelete.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("User with username %s does not exist.", username));
        }
        userService.delete(userToDelete.get().getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(String.format("User %s has been deleted.", username));
    }
}
