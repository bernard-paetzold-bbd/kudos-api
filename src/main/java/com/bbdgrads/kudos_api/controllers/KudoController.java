package com.bbdgrads.kudos_api.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import com.bbdgrads.kudos_api.service.AuthService;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.KudoServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.bbdgrads.kudos_api.exceptions.SelfMessageException;
import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

@RestController
@RequestMapping(path = "/kudos")
public class KudoController extends ProtectedController {

    private final JwtService jwtService;

    private final OAuthController OAuthController;
    @Autowired
    private KudoServiceImpl kudoService;
    @Autowired
    private UserServiceImpl userService;

    KudoController(OAuthController OAuthController, JwtService jwtService) {
        this.OAuthController = OAuthController;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public ResponseEntity<Kudo> createNewKudo(@RequestParam Long targetUserId,
            @RequestParam String message, @RequestHeader String bearer) {

        User sendingUser;
        try {
            sendingUser = jwtService.getUserFromHeader(bearer);
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (sendingUser.getUserId() == targetUserId) {
            throw new SelfMessageException(sendingUser.getUserId());
        }

        var targetUser = userService.findByUserId(targetUserId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %s does not exist.", targetUserId)));

        var kudo = new Kudo(message, sendingUser, targetUser);
        Kudo newKudo = kudoService.save(kudo);

        return ResponseEntity.status(HttpStatus.CREATED).body(newKudo);
    }

    // Get all kudos directed at requester
    @GetMapping("/user-kudos")
    public ResponseEntity<List<Kudo>> getKudo(@RequestParam String target_id_token, @RequestHeader String bearer) {

        User user;
        try {
            user = jwtService.getUserFromHeader(bearer);
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var kudos = kudoService.findByTargetUser(user);
        if (kudos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(kudos);
    }

    // Get all kudos of a user by their username.
    @GetMapping("/getKudoByUsername/{username}")
    public ResponseEntity<?> getKudosOfUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist.");
        }
        List<Kudo> kudos = kudoService.findByTargetUser(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(kudos);
    }

    // Update the message of a kudo. Can be used in the case where message needs to
    // be censored or corrected after the fact.
    @PatchMapping("/{kudoId}/message")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateKudoMessage(
            @PathVariable Long kudoId,
            @RequestBody String newMessage/*
                                           * ,
                                           * 
                                           * @RequestParam String username
                                           */) { // change to google token

        // Optional<User> user = userService.findByUsername(username);
        //
        // if (user.isEmpty() || !user.get().isAdmin()) {
        // return ResponseEntity.status(HttpStatus.FORBIDDEN)
        // .body("Only admins can update kudos.");
        // }

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
    @GetMapping("/getKudoById/{kudoId}")
    public ResponseEntity<?> getKudoById(@PathVariable Long kudoId) {
        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(kudoOpt.get());
    }

    @GetMapping("/getAllKudos")
    public ResponseEntity<List<Kudo>> getAllKudos() {
        List<Kudo> kudos = kudoService.findAllKudos();
        return ResponseEntity.status(HttpStatus.OK).body(kudos);
    }

}
