package com.bbdgrads.kudos_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // TODO: Need to check the type of the google_id
    @PostMapping("/create")
    public @ResponseBody String createNewUser(@RequestParam Long user_id, @RequestParam String name) {
        var newUser = new User(user_id, name, name, false, name);

        userRepository.save(newUser);

        return "Saved";
    }

    @GetMapping("/{user_id_token}")
    public String getUser(@PathVariable String user_id_token) {
        // TODO: Validate the user id token here and map to correct user_id
        // https://developers.google.com/identity/sign-in/web/backend-auth

        return new String();
    }

}
