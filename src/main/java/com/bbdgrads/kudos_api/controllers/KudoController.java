package com.bbdgrads.kudos_api.controllers;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.bbdgrads.kudos_api.security.JwtAuthFilter;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.KudoServiceImpl;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;

@RestController
@RequestMapping(path = "/kudos")
public class KudoController extends ProtectedController {

    private final JwtAuthFilter jwtAuthFilter;

    private final JwtService jwtService;

    private final OAuthController OAuthController;
    @Autowired
    private KudoServiceImpl kudoService;
    @Autowired
    private UserServiceImpl userService;

    KudoController(OAuthController OAuthController, JwtService jwtService, JwtAuthFilter jwtAuthFilter) {
        this.OAuthController = OAuthController;
        this.jwtService = jwtService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @PostMapping("/create")
    public ResponseEntity<Kudo> createNewKudo(@RequestParam String targetUsername,
            @RequestParam String message, HttpServletRequest req) {

        User sendingUser;
        try {
            sendingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (sendingUser.getUsername() == targetUsername) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var targetUser = userService.findByUsername(targetUsername);

        if (!targetUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var kudo = new Kudo(message, sendingUser, targetUser.get());
        Kudo newKudo = kudoService.save(kudo);

        return ResponseEntity.status(HttpStatus.CREATED).body(newKudo);
    }

    // Get all kudos directed at requester
    @GetMapping("/user-kudos")
    public ResponseEntity<List<KudoDto>> getKudo(HttpServletRequest req) {

        User user;
        try {
            user = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var kudos = kudoService.findByTargetUser(user);
        if (kudos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertKudosToDto(kudos));
    }

    // Get all kudos of a user by their username.
    @GetMapping("/getKudoByUsername/{username}")
    public ResponseEntity<?> getKudosOfUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist.");
        }
        List<Kudo> kudos = kudoService.findByTargetUser(user.get());
        return ResponseEntity.status(HttpStatus.OK).body(convertKudosToDto(kudos));
    }

    // Update the message of a kudo. Can be used in the case where message needs to
    // be censored or corrected after the fact.
    @PatchMapping("/{kudoId}/message")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateKudoMessage(
            @PathVariable Long kudoId,
            @RequestBody String newMessage,
            HttpServletRequest req
    /*
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

        User actingUser;
        try {
            actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        kudoService.setMessage(kudo, newMessage, actingUser);

        return ResponseEntity.status(HttpStatus.OK).body(convertKudoToDto(kudo));
    }

    @PatchMapping("/{kudoId}/read/{read}")
    public ResponseEntity<?> updateKudoRead(@PathVariable Long kudoId, @PathVariable boolean read,
            HttpServletRequest req) {

        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        User actingUser;
        try {
            actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        kudoService.setRead(kudoOpt.get(), read, actingUser);

        return ResponseEntity.status(HttpStatus.OK).body(convertKudoToDto(kudoOpt.get()));
    }

    @PatchMapping("/{kudoId}/flag/{flagged}")
    public ResponseEntity<?> updateKudoFlagged(@PathVariable Long kudoId, @PathVariable boolean flagged,
            HttpServletRequest req) {

        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        User actingUser;
        try {
            actingUser = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        kudoService.setFlagged(kudoOpt.get(), flagged, actingUser);

        return ResponseEntity.status(HttpStatus.OK).body(convertKudoToDto(kudoOpt.get()));
    }

    // Get a kudo from its ID.
    @GetMapping("/getKudoById/{kudoId}")
    public ResponseEntity<?> getKudoById(@PathVariable Long kudoId) {
        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(convertKudoToDto(kudoOpt.get()));
    }

    @GetMapping("/getAllKudos")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Kudo>> getAllKudos() {
        List<Kudo> kudos = kudoService.findAllKudos();
        return ResponseEntity.status(HttpStatus.OK).body(kudos);
    }

    @GetMapping("/delete/{kudoId}")
    public ResponseEntity<String> deleteKudo(@PathVariable Long kudoId, HttpServletRequest req) {
        Optional<Kudo> kudoOpt = kudoService.findByKudoId(kudoId);

        User user;
        try {
            user = jwtService.getUserFromHeader(jwtAuthFilter.extractToken(req));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (kudoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kudo not found.");
        }

        if (user == kudoOpt.get().getSendingUser() || user == kudoOpt.get().getTargetUser() || !user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User does not have permission to delete this kudo");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Kudo deleted");
    }

    private List<KudoDto> convertKudosToDto(List<Kudo> kudos) {
        return kudos.stream()
                .map(kudo -> new KudoDto(
                        kudo.getKudoId(),
                        kudo.getMessage() != null ? kudo.getMessage() : null,
                        kudo.getSendingUser() != null ? kudo.getSendingUser().getUsername() : null,
                        kudo.getTargetUser() != null ? kudo.getTargetUser().getUsername() : null,
                        kudo.getCreated_at() != null ? kudo.getCreated_at() : null,
                        kudo.getFlagged() != null ? kudo.getFlagged() : null,
                        kudo.getRead() != null ? kudo.getRead() : null))
                .toList();
    }

    private KudoDto convertKudoToDto(Kudo kudo) {
        return new KudoDto(
                kudo.getKudoId(),
                kudo.getMessage() != null ? kudo.getMessage() : null,
                kudo.getSendingUser() != null ? kudo.getSendingUser().getUsername() : null,
                kudo.getTargetUser() != null ? kudo.getTargetUser().getUsername() : null,
                kudo.getCreated_at() != null ? kudo.getCreated_at() : null,
                kudo.getFlagged() != null ? kudo.getFlagged() : null,
                kudo.getRead() != null ? kudo.getRead() : null);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class KudoDto {
        private Long kudoId;
        private String message;
        private String sendingUsername;
        private String targetUsername;
        private LocalDateTime created_at;
        private Boolean flagged;
        private Boolean read;
    }
}
