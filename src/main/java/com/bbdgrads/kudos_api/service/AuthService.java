package com.bbdgrads.kudos_api.service;

import com.bbdgrads.kudos_api.dto.OAuthAccessTokenResponse;
import com.bbdgrads.kudos_api.dto.OAuthUserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Value;
import java.awt.*;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Service
public class AuthService {

    // TODO secure secrets and get new keys

    @Value("${google.client.id}")
    private String CLIENT_ID;
    @Value("${google.client.secret}")
    private String CLIENT_SECRET;
    private final String REDIRECT_URI = "http://localhost:8090/auth_code";

    private final WebClient webClient;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder) {
        String BASE_URL = "https://oauth2.googleapis.com";
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Optional<OAuthAccessTokenResponse> getUserAccessToken(String authCode) {
        try {
            OAuthAccessTokenResponse tokenResponse = webClient.post()
                    .uri("/token")
                    .bodyValue(getRequestBody(authCode))
                    .retrieve()
                    .bodyToMono(OAuthAccessTokenResponse.class)
                    .block();

            return Optional.ofNullable(tokenResponse);

        } catch (WebClientResponseException e) {
            System.err.println("Error exchanging auth code: " + e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("ERROR Response: " + e.toString());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Map<String, String> getRequestBody(String authCode) {
        return Map.of(
                "client_id", CLIENT_ID,
                "client_secret", CLIENT_SECRET,
                "code", authCode,
                "redirect_uri", REDIRECT_URI,
                "grant_type", "authorization_code");
    }

    // This needs to happen on CLI/FE
    private String getAuthCodeFromUser() throws Exception {
        String AUTH_URL = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&access_type=offline" +
                "&prompt=consent";

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(AUTH_URL));
        } else {
            System.out.println("Open this URL to login...");
            System.out.println(AUTH_URL);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the authorisation code: ");

        return scanner.nextLine();
    }

    public Optional<OAuthUserInfoResponse> getUserGoogleProfile(String accessToken) {
        WebClient webClient = WebClient.create("https://www.googleapis.com");
        try {
            OAuthUserInfoResponse response = webClient.get()
                    .uri("/oauth2/v3/userinfo")
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .bodyToMono(OAuthUserInfoResponse.class)
                    .block();
            return Optional.ofNullable(response);
        } catch (WebClientResponseException e) {
            System.err.println("Error fetching user info: " + e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // public Optional<OAuthUserInfoResponse> runAuthFlow(){
    // Optional<OAuthAccessTokenResponse> accessToken = getUserAccessToken();
    // if(accessToken.isPresent()){
    // return getUserGoogleProfile(accessToken.get().getAccessToken());
    // } else{
    // return Optional.empty();
    // }
    // }
}
