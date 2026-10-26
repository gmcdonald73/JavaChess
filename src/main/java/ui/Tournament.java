package ui;

import engine.game.ChessGame;
import engine.game.GameResult;
import engine.players.ChessPlayer;
import io.PgnWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tournament {

    private final ChessPlayer playerA;
    private final ChessPlayer playerB;
    private final int numberOfGames;
    private final boolean showGUI;
    private final boolean showConsoleMessages;

    private final List<GameRecord> results = new ArrayList<>();

    public Tournament(
            ChessPlayer playerA,
            ChessPlayer playerB,
            int numberOfGames,
            boolean showGUI,
            boolean showConsoleMessages) {

        if (playerA == null || playerB == null) {
            throw new IllegalArgumentException(
                    "Players cannot be null");
        }

        if (playerA == playerB) {
            throw new IllegalArgumentException(
                    "Players must be separate instances");
        }

        if (numberOfGames < 1) {
            throw new IllegalArgumentException(
                    "Number of games must be at least 1");
        }

        this.playerA = playerA;
        this.playerB = playerB;
        this.numberOfGames = numberOfGames;
        this.showGUI = showGUI;
        this.showConsoleMessages = showConsoleMessages;
    }

    public List<GameRecord> run() throws IOException
    {
        results.clear();

        for (int gameNumber = 1;
             gameNumber <= numberOfGames;
             gameNumber++)
        {

            boolean playerAIsWhite = gameNumber % 2 == 1;

            ChessPlayer white = playerAIsWhite
                    ? playerA : playerB;

            ChessPlayer black = playerAIsWhite
                    ? playerB : playerA;

            long start = System.nanoTime();

            ChessGame game = new ChessGame(white, black);
            GameResult result = game.play(showGUI, showConsoleMessages);

            long elapsedMillis =
                    (System.nanoTime() - start) / 1_000_000;

            GameRecord record = new GameRecord(
                    gameNumber,
                    white.getName(),
                    black.getName(),
                    result,
                    elapsedMillis);

            results.add(record);

            System.out.printf(
                    "Game %d: White=%s, Black=%s, Result=%s, Time=%dms%n",
                    record.getGameNumber(),
                    record.getWhitePlayer(),
                    record.getBlackPlayer(),
                    record.getResult(),
                    record.getElapsedMillis());

            // Save game to file
            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

            String fileName = "game_" + now.format(formatter) + ".pgn";

            Path file = Path.of("games", fileName);
            System.out.println("Saving game to " + file.toAbsolutePath());
            PgnWriter.save(game,  file);


        }

        return Collections.unmodifiableList(
                new ArrayList<>(results));
    }

    public void printStatistics() {
        int playerAWins = 0;
        int playerBWins = 0;
        int draws = 0;

        for (GameRecord record : results) {
            switch (record.getResult()) {
                case WHITE_WIN ->
                {
                    if (record.getWhitePlayer()
                            .equals(playerA.getName())) {
                        playerAWins++;
                    } else {
                        playerBWins++;
                    }
                }
                case BLACK_WIN ->
                {
                    if (record.getBlackPlayer()
                            .equals(playerA.getName())) {
                        playerAWins++;
                    } else {
                        playerBWins++;
                    }
                }
                case DRAW -> draws++;
                case UNFINISHED -> draws++;
            }
        }

        int playerADraws = draws;
        int playerBDraws = draws;

        double playerAScore = playerAWins + 0.5 * playerADraws;
        double playerBScore = playerBWins + 0.5 * playerBDraws;

        System.out.printf("%nTournament results (%d games)%n",
                results.size());
        System.out.printf("%-15s %5s %5s %7s%n",
                "Player", "Wins", "Draws", "Score");
        System.out.printf("%-15s %5d %5d %7.1f%n",
                playerA.getName(), playerAWins,
                playerADraws, playerAScore);
        System.out.printf("%-15s %5d %5d %7.1f%n",
                playerB.getName(), playerBWins,
                playerBDraws, playerBScore);
    }

    public List<GameRecord> getResults() {
        return Collections.unmodifiableList(results);
    }

    public static class GameRecord {

        private final int gameNumber;
        private final String whitePlayer;
        private final String blackPlayer;
        private final GameResult result;
        private final long elapsedMillis;

        public GameRecord(
                int gameNumber,
                String whitePlayer,
                String blackPlayer,
                GameResult result,
                long elapsedMillis) {

            this.gameNumber = gameNumber;
            this.whitePlayer = whitePlayer;
            this.blackPlayer = blackPlayer;
            this.result = result;
            this.elapsedMillis = elapsedMillis;
        }

        public int getGameNumber() {
            return gameNumber;
        }

        public String getWhitePlayer() {
            return whitePlayer;
        }

        public String getBlackPlayer() {
            return blackPlayer;
        }

        public GameResult getResult() {
            return result;
        }

        public long getElapsedMillis() {
            return elapsedMillis;
        }
    }
}