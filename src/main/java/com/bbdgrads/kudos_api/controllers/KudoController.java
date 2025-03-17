package com.bbdgrads.kudos_api.controllers;

import java.util.List;
import java.util.Optional;

import com.bbdgrads.kudos_api.service.KudoServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

@Controller
@RequestMapping(path = "/kudos")
public class KudoController {
    @Autowired
    private KudoServiceImpl kudoService;
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/create")
    public ResponseEntity<Kudo> createNewUser(@RequestParam Long user_id, @RequestParam String name) {
        var newKudo = new Kudo();

        Kudo kudo = kudoService.save(newKudo);

        return ResponseEntity.status(HttpStatus.CREATED).body(kudo);
    }

    // Get all kudos directed at requester
    @GetMapping("/user-kudos")
    public ResponseEntity<List<Kudo>> getKudo(@RequestParam String target_id_token) {
        // TODO: Validate the user id token here and map to correct user_id
        var user = new User();

        var kudos = kudoService.findByTargetUser(user);
        if (kudos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(kudos);
    }

    //Get all kudos of a user by their username.
    @GetMapping("/{username}")
    public ResponseEntity<?> getKudosOfUsername(@PathVariable String username){
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist.");
        }
        List<Kudo> kudos = kudoService.findByTargetUser(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(kudos);
    }

    // Update the message of a kudo. Can be used in the case where message needs to be censored or corrected after the fact.
    @PatchMapping("/{kudoId}/message")
    public ResponseEntity<?> updateKudoMessage(
            @PathVariable Long kudoId,
            @RequestParam String newMessage,
            @RequestParam String username) { // change to google token

        Optional<User> user = userService.findByUsername(username);

        if (user.isEmpty() || !user.get().isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admins can update kudos.");
        }

        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        Kudo kudo = kudoOpt.get();
        kudo.setMessage(newMessage);
        kudoService.save(kudo);

        return ResponseEntity.status(HttpStatus.OK).body(kudo);
    }

    // Get a kudo from its ID.
    @GetMapping("/{kudoId}")
    public ResponseEntity<?> getKudoById(@PathVariable Long kudoId) {
        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(kudoOpt.get());
    }

}
