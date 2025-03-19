package com.bbdgrads.kudos_api;

import com.bbdgrads.kudos_api.dto.OAuthUserInfoResponse;
import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Team;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.KudoRepository;
import com.bbdgrads.kudos_api.repository.TeamRepository;
import com.bbdgrads.kudos_api.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class KudosApiApplication {

	// @Autowired
	// private AuthService authService;

	@Autowired
	private KudoRepository kudoRepository;

	@Autowired
	private TeamRepository teamRepository;

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

	@Bean
	CommandLineRunner initData(UserServiceImpl userService, TeamServiceImpl teamService, KudoServiceImpl kudoService) {
		return args -> {
			// Create dummy teams
			// Team teamA = new Team(null, "Alpha Team");
			// Team teamB = new Team(null, "Beta Team");
			// teamService.save(teamA);
			// teamService.save(teamB);

			// // Create dummy users
			// User adminUser = new User("AdminUser", "admin_google_ID", true);
			// User greg = new User("Gregory_Maselle", "111907463772066977914", true);
			// User normalUser = new User("RegularUser", "user_google_ID", false);
			// User kyle = new User("Kyle_Wilkins", "104939578726951606234", false);

			// adminUser.setTeam(teamA);
			// normalUser.setTeam(teamB);
			// userService.save(adminUser);
			// userService.save(normalUser);
			// userService.save(greg);
			// userService.save(kyle);

			// // Create dummy kudos
			// Kudo kudo = new Kudo("Jolly good show bro! *Fist bump*", normalUser, greg);
			// Kudo kudo1 = new Kudo("Test 1", greg, kyle);
			// Kudo kudo2 = new Kudo("Test 2", normalUser, adminUser);

			// kudoService.save(kudo);
			// kudoService.save(kudo1);
			// kudoService.save(kudo2);

			// System.out.println("Dummy data initialized!");
		};
	}

	// -- Used for testing auth service, this class must also implement
	// CommandLineRunner if testing
	// @Override
	// public void run(String... args) throws Exception {
	// Optional<OAuthUserInfoResponse> userInfo = authService.runAuthFlow();
	// if(userInfo.isPresent()){
	// System.out.println(userInfo.get().toString());
	// }else{
	// System.err.println("Could not authenticate user!");
	// }
	// }
}