package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.service.JwtService;

import java.util.Optional;

public class ProtectedController {

    public Optional<String> verifyApiRequest(JwtService jwtService, String apiJwt){
        return Optional.ofNullable(jwtService.verifyApiJwt(apiJwt).getSubject());
    }
}
