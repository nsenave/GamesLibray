package fr.nsenave.gameslibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GamesLibraryApplication {

	public static void main(String[] args) {
		//SpringApplication.run(GamesLibraryApplication.class, args);

		System.exit(SpringApplication.exit(SpringApplication.run(GamesLibraryApplication.class, args)));
	}

}
