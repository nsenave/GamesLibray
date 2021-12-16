package fr.nsenave.gameslibrary;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application.properties")
public class GamesLibraryApplication implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job processGamesJob;

	@Value("${fr.nsenave.gameslibrary.singleFile}") String jsonFilePath;
	@Value("${fr.nsenave.gameslibrary.inDirectory}") String inDirectory;

	public static void main(String[] args) {
		SpringApplication.run(GamesLibraryApplication.class, args);

		//System.exit(SpringApplication.exit(SpringApplication.run(GamesLibraryApplication.class, args)));
	}

	@Override
	public void run(String... args) throws Exception {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		paramsBuilder.addString("jsonFilePath", jsonFilePath);
		paramsBuilder.addString("directoryPath", inDirectory);
		jobLauncher.run(processGamesJob, paramsBuilder.toJobParameters());
	}

}
