package com.bbdgrads.kudos_api;

import com.bbdgrads.kudos_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KudosApiApplication implements CommandLineRunner {

	@Autowired
	private AuthService authService;

	public static void main(String[] args) {
		SpringApplication.run(KudosApiApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println((authService.runAuthFlow("kylew0409@gmail.com")));
	}
}
