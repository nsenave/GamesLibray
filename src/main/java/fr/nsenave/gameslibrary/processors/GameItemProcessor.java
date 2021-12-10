package fr.nsenave.gameslibrary.processors;

import fr.nsenave.gameslibrary.buisness.Game;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;

@Log4j2
public class GameItemProcessor implements ItemProcessor<Game, Game> {

    @Override
    public Game process(Game game) {
        log.info("Processing game: " + game);
        game.setName(game.getName().toUpperCase());
        game.setConsole(game.getConsole().toUpperCase());
        log.info("Transformed game: " + game);
        return game;
    }
}
