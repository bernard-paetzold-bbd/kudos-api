package com.bbdgrads.kudos_api.controllers;

import java.util.List;

import com.bbdgrads.kudos_api.service.KudoServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.service.KudoService;

@Controller
@RequestMapping(path = "/kudos")
public class KudoController {
    @Autowired
    private KudoServiceImpl kudoService;

    // Why use ResponseEntity instead of @ResponseBody?
    // ResponseEntity more likely to be used in practice. It is more flexible but more verbose.
    @PostMapping("/create")
    public ResponseEntity<Kudo> createNewKudo(@RequestBody Kudo kudo) {
        Kudo savedKudo = kudoService.save(kudo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedKudo);
    }

    // Get all kudos directed at requester
    @GetMapping("/my-kudos")
    public ResponseEntity<List<Kudo>> getAllKudos(@PathVariable String target_id_token) {
        // TODO: Validate the user id token here and map to correct user_id
        var user = new User();

        var kudos = kudoService.findByTargetUser(user);
        if (kudos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(kudos);
        }
    }

}
