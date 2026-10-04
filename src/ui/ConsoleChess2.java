package ui;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import engine.*;
import io.PgnWriter;

public class ConsoleChess2 {

    public static void main(String[] args) throws Exception {

        ChessGame game = new ChessGame(
                new BotPlayer("Graeme", Colour.WHITE),
                new HumanPlayer("GMac", Colour.BLACK)
        );

        game.play();

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        String fileName = "game_" + now.format(formatter) + ".pgn";

        Path file = Path.of("games", fileName);
        System.out.println("Saving game to " + file.toAbsolutePath());
        PgnWriter.save(game,  file);
    }
}
