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

    @Test
    void enPassantSqWithLegalCapture()
    {
        Board board = new Board();

        board.SetupForGame();

        ChessTestUtils.makeMove(board, Colour.WHITE, "e4");
        ChessTestUtils.makeMove(board, Colour.BLACK, "a6");
        ChessTestUtils.makeMove(board, Colour.WHITE, "e5");
        ChessTestUtils.makeMove(board, Colour.BLACK, "d5");

        String fen = board.saveToFen(Colour.WHITE, 1);
        assertEquals("rnbqkbnr/1pp1pppp/p7/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 1", fen);

    }

    @Test
    void enPassantSqWithNoLegalCapture()
    {
        // This should still include enPassant target if pawn advance two squares last move
        Board board = new Board();

        board.SetupForGame();

        ChessTestUtils.makeMove(board, Colour.WHITE, "e4");

        String fen = board.saveToFen(Colour.BLACK, 1);
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", fen);
    }
}
