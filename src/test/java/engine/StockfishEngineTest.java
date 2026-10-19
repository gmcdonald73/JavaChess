
package engine;

import engine.players.StockfishEngine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockfishEngineTest
{
    private static final Path STOCKFISH_PATH =
            Path.of(
                    "engines",
                    "stockfish",
                    "stockfish-windows-x86-64-universal.exe");

    @Test
    void shouldReturnLegalLookingMoveFromStartingPosition()
            throws IOException
    {
        assertTrue(
                Files.isRegularFile(STOCKFISH_PATH),
                "Stockfish executable not found: "
                        + STOCKFISH_PATH.toAbsolutePath());

        try (StockfishEngine engine =
                     new StockfishEngine(
                             STOCKFISH_PATH.toAbsolutePath().toString()))
        {
            String move = engine.getBestMove(List.of(), 5);

            assertNotNull(move);
            assertTrue(
                    move.matches("[a-h][1-8][a-h][1-8][qrbn]?"),
                    "Invalid UCI move returned: " + move);

            System.out.println(
                    "Stockfish starting-position move: " + move);
        }
    }

    @Test
    void shouldReturnMoveForPositionWithMoveHistory()
            throws IOException
    {
        try (StockfishEngine engine =
                     new StockfishEngine(
                             STOCKFISH_PATH.toAbsolutePath().toString()))
        {
            List<String> moves = List.of(
                    "e2e4",
                    "e7e5",
                    "g1f3");

            String move = engine.getBestMove(moves, 5);

            assertNotNull(move);
            assertTrue(
                    move.matches("[a-h][1-8][a-h][1-8][qrbn]?"),
                    "Invalid UCI move returned: " + move);

            System.out.println(
                    "Stockfish response after 1. e4 e5 2. Nf3 : " + move);
        }
    }
}
