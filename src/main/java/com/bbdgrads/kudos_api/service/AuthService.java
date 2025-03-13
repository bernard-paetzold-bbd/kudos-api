package com.bbdgrads.kudos_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.awt.*;
import java.net.URI;
import java.util.Map;
import java.util.Scanner;
@Service
public class AuthService {
    //TODO secure secrets
    private static final String CLIENT_ID = "388099928445-s4d43ln7sgr9mpebqa31sp0qbvskmqr7.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private final static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private final static String BASE_URL = "https://oauth2.googleapis.com";
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth" +
            "?client_id=" + CLIENT_ID +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_type=code" +
            "&scope=openid%20email%20profile" +
            "&access_type=offline" +
            "&prompt=consent";

    private final WebClient webClient;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    // TODO implement better error handling and json handling
    public String getUserAccessToken(String email){
        try {
            String authCode = getAuthCodeFromUser();
            Map tokenResponse = webClient.post()
                    .uri("/token")
                    .bodyValue(Map.of(
                            "client_id", CLIENT_ID,
                            "client_secret", CLIENT_SECRET,
                            "code", authCode,
                            "redirect_uri", REDIRECT_URI,
                            "grant_type", "authorization_code"
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return tokenResponse.get("access_token").toString();

        } catch (Exception e){
            System.out.println("ERROR Response: " + e.toString());
            e.printStackTrace();
        }
        return "not found";
    }

    public String getAuthCodeFromUser() throws Exception{
        System.out.println("Open this URL to login...");
        System.out.println(AUTH_URL);

        if(Desktop.isDesktopSupported()){
            Desktop.getDesktop().browse(new URI(AUTH_URL));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the authorisation code: ");

        return scanner.nextLine();
    }

    public String getUserGoogleProfile(String accessToken){
        WebClient webClient = WebClient.create("https://www.googleapis.com");
        String userInfo = webClient.get()
                .uri("/oauth2/v3/userinfo")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return userInfo;
    }

    public String runAuthFlow(String email){
        String accessToken = getUserAccessToken(email);
        System.out.println(accessToken);
        return getUserGoogleProfile(accessToken);
    }
}
