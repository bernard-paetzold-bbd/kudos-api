package com.bbdgrads.kudos_api;

import com.bbdgrads.kudos_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KudosApiApplication {

	public static void main(String[] args) {
		System.out.println("\n\n" +
				"\t\t\t\t██╗  ██╗ ██╗   ██╗ ██████╗   ██████╗  ███████╗\n" +
				"\t\t\t\t██║ ██╔╝ ██║   ██║ ██╔══██╗ ██╔═══██╗ ██╔════╝\n" +
				"\t\t\t\t█████╔╝  ██║   ██║ ██║  ██║ ██║   ██║ ███████╗\n" +
				"\t\t\t\t██╔═██╗  ██║   ██║ ██║  ██║ ██║   ██║ ╚════██║\n" +
				"\t\t\t\t██║  ██╗ ╚██████╔╝ ██████╔╝ ╚██████╔╝ ███████║\n" +
				"\t\t\t\t╚═╝  ╚═╝  ╚═════╝  ╚═════╝   ╚═════╝  ╚══════╝\n\n" +
				"                                          ");
		SpringApplication.run(KudosApiApplication.class, args);
	}

}