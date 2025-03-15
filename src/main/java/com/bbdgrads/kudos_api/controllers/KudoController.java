package com.bbdgrads.kudos_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.service.KudoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(path = "/kudos")
public class KudoController {
    @Autowired
    private KudoService kudoService;

    @PostMapping("/create")
    public @ResponseBody String createNewUser(@RequestParam Long user_id, @RequestParam String name) {
        var newKudo = new Kudo();

        kudoService.save(newKudo);

        return "Saved";
    }

    // Get all kudos directed at requestor
    @GetMapping("/user-kudos")
    public List<Kudo> getKudo(@PathVariable String target_id_token) {
        // TODO: Validate the user id token here and map to correct user_id
        var user = new User();

        var kudos = kudoService.findByTargetUser(user);
        return kudos;
    }

}
