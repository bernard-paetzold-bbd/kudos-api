package com.bbdgrads.kudos_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    // TODO: Need to check the type of the google_id
    @PostMapping("/create")
    public @ResponseBody String createUser(@RequestParam Long userId, @RequestParam String name) {
        var newUser = new User(userId, name, false, new Team());

        userService.save(newUser);

        return "User saved";
    }

    @GetMapping("/{user_id_token}")
    public String getUser(@PathVariable String userIdToken) {
        // TODO: Validate the user id token here and map to correct user_id
        // https://developers.google.com/identity/sign-in/web/backend-auth

        return new String();
    }

    @DeleteMapping("/{user_id_token}")
    public String deleteUser(@PathVariable String userIdToken) {
        // TODO: Validate the user id token here and map to correct user_id

        userService.delete(0L);

        return "User deleted";
    }
}
