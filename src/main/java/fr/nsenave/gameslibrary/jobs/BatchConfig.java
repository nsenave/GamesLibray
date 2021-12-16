package fr.nsenave.gameslibrary.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.nsenave.gameslibrary.buisness.Game;
import fr.nsenave.gameslibrary.processors.GameItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfig {

    public static String SAMPLE_DATA = "sample.json";

    private static JsonItemReader<Game> getItemReader(Resource resource) {
        ObjectMapper mapper = new ObjectMapper();
        JsonObjectReader<Game> jsonObjectReader = new JacksonJsonObjectReader<Game>(mapper, Game.class);
        return new JsonItemReader<Game>(
                resource,
                jsonObjectReader);
    }

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    
    @Bean(destroyMethod="") // https://stackoverflow.com/a/23089536/13425151
    public JsonItemReader<Game> classpathReader() {
        return getItemReader(new ClassPathResource(SAMPLE_DATA));
    }

    @Bean
    @StepScope
    public JsonItemReader<Game> singleFileReader(
            @Value("#{jobParameters['jsonFilePath']}") String jsonFilePath) {
        return getItemReader(new FileSystemResource(jsonFilePath));
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Game> multipleFileReader(
            @Value("#{jobParameters['directoryPath']}") String directoryPath) throws IOException {
        // thanks to https://github.com/mashariqk/springBatchMultipleFiles
        FileSystemXmlApplicationContext patternResolver = new FileSystemXmlApplicationContext();
        Resource[] inputResources = patternResolver.getResources(directoryPath + "/*.json");
        //
        MultiResourceItemReader<Game> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setResources(inputResources);
        multiResourceItemReader.setDelegate(singleFileReader(null));
        return multiResourceItemReader;
    }

    @Bean
    public GameItemProcessor processor() {
        return new GameItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Game> databaseWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Game>()
                .itemSqlParameterSourceProvider(
                        new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO game (name, console, release) VALUES (:name, :console, :release)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job processGamesJob(JobCompletionNotificationListener listener,
                             Step step1, Step step2, Step step3) {
        return jobBuilderFactory.get("processGamesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .next(step2)
                .next(step3)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Game> writer) {
        return stepBuilderFactory.get("step1")
                .<Game, Game> chunk(10)
                .reader(classpathReader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Step step2(JdbcBatchItemWriter<Game> writer) {
        return stepBuilderFactory.get("step2")
                .<Game, Game> chunk(10)
                .reader(singleFileReader(null))
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Step step3(JdbcBatchItemWriter<Game> writer) throws IOException {
        return stepBuilderFactory.get("step3")
                .<Game, Game> chunk(10)
                .reader(multipleFileReader(null))
                .processor(processor())
                .writer(writer)
                .build();
    }

}