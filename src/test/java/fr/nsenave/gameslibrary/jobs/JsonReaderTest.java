package fr.nsenave.gameslibrary.jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.nsenave.gameslibrary.buisness.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonReaderTest {

    /**
     * just to be sure that we use Jackson's mapper correctly
     */
    @Test
    public void testMapJsonFile() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"name\": \"n1\", \"console\": \"c1\", \"release\": 2000}";
        Game game = mapper.readValue(jsonString, Game.class);
        assertEquals("n1", game.getName());
        assertEquals("c1", game.getConsole());
        assertEquals(2000, game.getRelease());
    }
}
