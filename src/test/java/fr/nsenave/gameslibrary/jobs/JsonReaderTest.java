package fr.nsenave.gameslibrary.jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.nsenave.gameslibrary.buisness.Game;
import org.junit.jupiter.api.Test;

public class JsonReaderTest {

    @Test
    public void testMapJsonFile() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"name\": \"n1\", \"console\": \"c1\", \"release\": 2000}";
        Game game = mapper.readValue(jsonString, Game.class);
        System.out.println(game);
    }
}
