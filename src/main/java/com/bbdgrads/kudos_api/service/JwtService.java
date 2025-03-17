package com.bbdgrads.kudos_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.auth.oauth2.TokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${google.client.id}")
    private String CLIENT_ID;
    @Value("${kudos.api.secret}")
    private String API_SECRET;
    private final String KUDOS_ISSUER = "kudos-api";

    public boolean verifyGoogleJwt(String jwtToken){
        try {
            String GOOGLE_ISSUER = "https://accounts.google.com";
            TokenVerifier verifier = TokenVerifier.newBuilder()
                    .setAudience(CLIENT_ID)
                    .setIssuer(GOOGLE_ISSUER)
                    .build();

            verifier.verify(jwtToken);

            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            // Could potentially get user info here
//            System.out.println("User ID: " + decodedJWT.getSubject());
//            System.out.println("Email: " + decodedJWT.getClaim("email").asString());
            return true;

        } catch (JWTVerificationException | TokenVerifier.VerificationException e){
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


    public String generateApiJwt(String userId){
        String KUDOS_ISSUER = "kudos-api";
        long EXPIRATION_TIME = 86400000;
        return JWT.create()
                .withSubject(userId)
                .withIssuer(KUDOS_ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(API_SECRET));
    }

    public DecodedJWT verifyApiJwt(String apiJwt){
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(API_SECRET))
                    .withIssuer(KUDOS_ISSUER)
                    .build();
            return  verifier.verify(apiJwt);
        } catch (JWTVerificationException e){
            System.err.println("JWT Verification failed: " + e.getMessage());
            return null;
        }

    }
}
