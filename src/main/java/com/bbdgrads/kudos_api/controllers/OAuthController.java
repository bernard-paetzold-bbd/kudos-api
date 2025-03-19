package com.bbdgrads.kudos_api.controllers;

import com.bbdgrads.kudos_api.dto.OAuthAccessTokenResponse;
import com.bbdgrads.kudos_api.dto.OAuthCodeRequest;
import com.bbdgrads.kudos_api.dto.OAuthUserInfoResponse;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.service.AuthService;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OAuthController extends ProtectedController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    public OAuthController(AuthService authService, JwtService jwtService, UserService userService){
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Value("${google.client.id}")
    private String CLIENT_ID;

    @GetMapping("/client_id")
    public Map<String, String> getGoogleClientId(){
        return Map.of("client_id", CLIENT_ID);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> getUserAuthCode(@RequestBody OAuthCodeRequest oAuthCodeRequest){
        String authCode = oAuthCodeRequest.getAuthCode();
        Optional<OAuthAccessTokenResponse> accessTokenResponse = authService.getUserAccessToken(authCode);

        var authResponse = new Object(){boolean authenticated = false; String apiJwt = "";String errorMsg = "";};

        accessTokenResponse.ifPresentOrElse(
                oAuthAccessTokenResponse -> {
                    String googleJwt = oAuthAccessTokenResponse.getIdToken();
                    if(jwtService.verifyGoogleJwt(googleJwt)){
                        Optional<OAuthUserInfoResponse> userInfoResponse = authService.getUserGoogleProfile(oAuthAccessTokenResponse.getAccessToken());
                        userInfoResponse.ifPresentOrElse(
                                oAuthUserInfoResponse ->
                                {
                                    Optional<User> user = userService.findByGoogleId(oAuthUserInfoResponse.getSub());
                                    if(user.isEmpty()){
                                        System.out.println("User Signed Up!");
                                        userService.save(new User(oAuthUserInfoResponse.getName(), oAuthUserInfoResponse.getSub(),false));
                                    } else{
                                        System.out.println("User logged in");
                                    }
                                    authResponse.apiJwt = jwtService.generateApiJwt(oAuthUserInfoResponse.getSub());
                                    authResponse.authenticated = true;
                                }

                        , () -> authResponse.errorMsg = "Could not not fetch user info!");
                    } else {
                        authResponse.errorMsg = "JWT not valid!";
                    }
                }
        , () -> authResponse.errorMsg = "Auth code could not exchanged for access token!");

        if(authResponse.authenticated){
            return ResponseEntity.ok(Map.of("API-KEY", authResponse.apiJwt));
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", authResponse.errorMsg));
        }
    }

    @GetMapping("/test-auth")
    public String testAuth(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        System.out.println(token);
        Optional<String> test = this.verifyApiRequest(jwtService, token);
        if (test.isPresent()){
            System.out.println(test.get());
            return "success";
        } else {
            return "fail";
        }
    }
}
