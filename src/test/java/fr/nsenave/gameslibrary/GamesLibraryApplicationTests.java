package fr.nsenave.gameslibrary;

import fr.nsenave.gameslibrary.jobs.BatchConfig;
import fr.nsenave.gameslibrary.jobs.JobCompletionNotificationListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {
		JobCompletionNotificationListener.class,
		BatchConfig.class})
/*@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class})*/
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class GamesLibraryApplicationTests {

	public static final String SINGLE_FILE = "src/test/resources/games_test.json";
	public static final String DATA_DIRECTORY = "src/test/resources/test_files";

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@AfterEach
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		paramsBuilder.addString("jsonFilePath", SINGLE_FILE);
		paramsBuilder.addString("directoryPath", DATA_DIRECTORY);
		return paramsBuilder.toJobParameters();
	}

	@Test
	public void testStep2() {
		JobExecution jobExecution = jobLauncherTestUtils.launchStep(
				"step2", defaultJobParameters());
		ExitStatus actualExitStatus = jobExecution.getExitStatus();
		assertEquals(actualExitStatus.getExitCode(), BatchStatus.COMPLETED.name());
	}

	@Test
	public void testStep3() throws IOException {
		JobExecution jobExecution = jobLauncherTestUtils.launchStep(
				"step3", defaultJobParameters());
		ExitStatus actualExitStatus = jobExecution.getExitStatus();
		assertEquals(actualExitStatus.getExitCode(), BatchStatus.COMPLETED.name());
	}

}
