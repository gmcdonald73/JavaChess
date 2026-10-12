package engine;

import engine.board.Board;
import engine.moves.MoveValidationResult;
import engine.pieces.Colour;
import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnPassantTests
{
    @Test
    void enPassantCapture()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e5");
        board.PlacePiece(Colour.BLACK, PieceRank.PAWN, "d7");

        ChessTestUtils.makeMove(board, Colour.BLACK, "d5", false);

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "exd6", false);

        assertTrue(mvr.isValid());
    }

}
