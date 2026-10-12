package engine;

import engine.board.Board;
import engine.moves.MoveValidationResult;
import engine.pieces.Colour;
import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PromotionTests
{
    /*
    Promote to knight
    Promote to rook
    Promote to bishop
    Promote to anything else fails
    Capture and promote
    Promote and checkmate
    Capture and promote and checkmate
     */

    @Test
    void promoteToQueen()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=Q", false);

        assertTrue(mvr.isValid());
    }

    @Test
    void promoteToKnight()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=N", false);

        assertTrue(mvr.isValid());
    }

    @Test
    void promoteToBishop()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=B", false);

        assertTrue(mvr.isValid());
    }

    @Test
    void promoteToRook()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=R", false);

        assertTrue(mvr.isValid());
    }

    @Test
    void promoteToKingShouldFail()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=K", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void promoteToPawnShouldFail()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=P", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void promoteToInvalidShouldFail()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8=Z", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void noPromotionPieceSpecifiedShouldFail()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "e8", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void promoteAndCapture()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e7");
        board.PlacePiece(Colour.BLACK, PieceRank.ROOK, "f8");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "exf8=Q", false);

        assertTrue(mvr.isValid());
    }

}
