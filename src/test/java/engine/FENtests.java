package engine;

import engine.board.Board;
import engine.pieces.Colour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FENtests
{
    @Test
    void saveStartingPositionToFEN()
    {
        Board board = new Board();

        board.SetupForGame();

        String fen = board.saveToFen(Colour.WHITE, 1);

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen);
    }

    @Test
    void saveOtherPos()
    {
        Board board = new Board();

        board.setupFromFEN("r3k1r1/8/3q4/8/8/5N2/8/1R2K2R b Kq - 0 1");

        String fen = board.saveToFen(Colour.BLACK, 1);
        assertEquals("r3k1r1/8/3q4/8/8/5N2/8/1R2K2R b Kq - 0 1", fen);
    }
}
