package com.bbdgrads.kudos_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.bbdgrads.kudos_api.model.User;
import com.google.auth.oauth2.TokenVerifier;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    @Autowired
    UserServiceImpl userService;

    @Value("${google.client.id}")
    private String CLIENT_ID;
    @Value("${kudos.api.secret}")
    private String API_SECRET;
    private final String KUDOS_ISSUER = "kudos-api";

    public boolean verifyGoogleJwt(String jwtToken) {
        try {
            String GOOGLE_ISSUER = "https://accounts.google.com";
            TokenVerifier verifier = TokenVerifier.newBuilder()
                    .setAudience(CLIENT_ID)
                    .setIssuer(GOOGLE_ISSUER)
                    .build();

            verifier.verify(jwtToken);

            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            // Could potentially get user info here
            // System.out.println("User ID: " + decodedJWT.getSubject());
            // System.out.println("Email: " + decodedJWT.getClaim("email").asString());
            return true;

        } catch (JWTVerificationException | TokenVerifier.VerificationException e) {
            System.err.println("JWT Verification failed: " + e.getMessage());
            return false;
        }
    }

    // Get the google Id from the JWT
    public String extractGoogleIdFromJwt(String jwtToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            return decodedJWT.getSubject(); // sub = subject ~= google Id
        } catch (JWTVerificationException e) {
            System.err.println("JWT Decoding failed: " + e.getMessage());
            return null;
        }
    }

    public String generateApiJwt(String userId) {
        String KUDOS_ISSUER = "kudos-api";
        long EXPIRATION_TIME = 86400000;
        return JWT.create()
                .withSubject(userId)
                .withIssuer(KUDOS_ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(API_SECRET));
    }

    public DecodedJWT verifyApiJwt(String apiJwt) {
        try {
            System.out.println("THIS IS BEING CALLED!!!!!");
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(API_SECRET))
                    .withIssuer(KUDOS_ISSUER)
                    .build();
            return verifier.verify(apiJwt);
        } catch (JWTVerificationException e) {
            System.err.println("JWT Verification failed: " + e.getMessage());
            return null;
        }
    }

    public User getUserFromHeader(String bearer) throws AccessDeniedException, EntityNotFoundException {

        Optional<String> jwtOptional = this.verifyApiRequest(bearer);

        String token = jwtOptional.orElseThrow(
                () -> new AccessDeniedException(String.format("Invalid token")));

        var user = userService.findByUserGoogleId(token).orElseThrow(
                () -> new EntityNotFoundException(String.format("User does not exist.")));
        return user;
    }

    public Optional<String> verifyApiRequest(String apiJwt) {
        DecodedJWT decodedJWT = verifyApiJwt(apiJwt);
        //return Optional.ofNullable(verifyApiJwt(apiJwt).getSubject());
        return (decodedJWT != null) ? Optional.ofNullable(decodedJWT.getSubject()) : Optional.empty();
    }
}
