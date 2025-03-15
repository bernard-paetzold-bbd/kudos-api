package com.bbdgrads.kudos_api;

import com.bbdgrads.kudos_api.dto.OAuthUserInfoResponse;
import com.bbdgrads.kudos_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class KudosApiApplication {

	@Autowired
	private AuthService authService;

	public static void main(String[] args) {
		SpringApplication.run(KudosApiApplication.class, args);
	}

// -- Used for testing auth service, this class must also implement CommandLineRunner if testing
//	@Override
//	public void run(String... args) throws Exception {
//		Optional<OAuthUserInfoResponse> userInfo = authService.runAuthFlow();
//		if(userInfo.isPresent()){
//			System.out.println(userInfo.get().toString());
//		}else{
//			System.err.println("Could not authenticate user!");
//		}
//	}
}
