package ui;

import java.util.List;

import engine.game.Tournament;
import engine.players.BotPlayer;
import engine.players.ChessPlayer;

public class ConsoleChess {

    public static void main(String[] args) throws Exception
    {
        ChessPlayer playerA = new BotPlayer("Bot");
//        ChessPlayer playerA = new HumanPlayer("GMac");
//        ChessPlayer playerB = new HumanPlayer("Player2");
//        ChessPlayer playerB = new StockFishPlayer("StockFish");
        BotPlayer playerB = new BotPlayer("BotB");
//        playerB.setDepth(4);

        Tournament tournament = new Tournament(playerA, playerB, 1, true, true);

        List<Tournament.GameRecord> results = tournament.run();

        tournament.printStatistics();

    }

/*
    public static void main(String[] args) throws Exception {

        ChessGame game = new ChessGame(
                new BotPlayer("Bot"),
                new StockFishPlayer("StockFish")
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
*/
}
