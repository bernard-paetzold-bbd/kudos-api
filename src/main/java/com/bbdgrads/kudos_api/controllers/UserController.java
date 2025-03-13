package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /* TODO: Decide if we want to create a controller advice for this controller and all controllers in general,
     or if we want to check for each fail case.
     EXAMPLE: What if we try to create a team with a name that already exists (team names should be unique.) How does
     the error get thrown? */

    // TODO: Need to check the type of the google_id
    @PostMapping("/create")
    public ResponseEntity<User> createNewUser(@RequestParam Long userId, @RequestParam String name) {
        var newUser = new User(userId, name, false, new Team());

        User confirmedUser = userService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(confirmedUser);
    }

    @GetMapping("/{user_id_token}")
    public ResponseEntity<User> getUser(@PathVariable String userIdToken) {
        // TODO: Validate the user id token here and map to correct user_id
        // https://developers.google.com/identity/sign-in/web/backend-auth

        // Dummy example for consistency. Will change when we deal with oauth shenanigans.
        Optional<User> user = userService.findByUserId(Long.parseLong(userIdToken));
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        }
    }

    @DeleteMapping("/{user_id_token}")
    public String deleteUser(@PathVariable String userIdToken) {
        // TODO: Validate the user id token here and map to correct user_id

        userService.delete(0L);

        return "User deleted";
    }
}
