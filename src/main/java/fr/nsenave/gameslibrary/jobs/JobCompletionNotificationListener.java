package fr.nsenave.gameslibrary.jobs;

import fr.nsenave.gameslibrary.buisness.Game;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job finished");
            jdbcTemplate.query("SELECT name, console, release FROM game",
                    (rs, row) -> new Game(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3))
            ).forEach(game -> {
                log.info("Found <" + game + "> in the database.");
            });
        }
    }
}
