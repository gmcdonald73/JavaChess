package engine;

import engine.board.Board;
import engine.board.Square;
import engine.moves.MoveValidationResult;
import engine.pieces.Colour;
import engine.pieces.Piece;
import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        // make sure the pawn got removed
        Piece piece = board.WhatIsAtSquare(Square.fromSAN("d5"));
        assertNull(piece);
    }

    @Test
    void enPassantCaptureAfterOtherMovesShouldFail()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "a2");
        board.PlacePiece(Colour.BLACK, PieceRank.PAWN, "a7");
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e5");
        board.PlacePiece(Colour.BLACK, PieceRank.PAWN, "d7");

        ChessTestUtils.makeMove(board, Colour.BLACK, "d5", false);
        ChessTestUtils.makeMove(board, Colour.WHITE, "a3", false);
        ChessTestUtils.makeMove(board, Colour.BLACK, "a6", false);

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "exd6", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void testEnPassantSquareIsCleared()
    {
        // enPassant should fail (but not throw exception) if subsequent moves are not pawn moves
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.PAWN, "e5");
        board.PlacePiece(Colour.BLACK, PieceRank.PAWN, "d7");
        board.PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "b4");
        board.PlacePiece(Colour.BLACK, PieceRank.ROOK, "a8");

        ChessTestUtils.makeMove(board, Colour.BLACK, "d5", false);
        ChessTestUtils.makeMove(board, Colour.WHITE, "Nxd5", false);
        ChessTestUtils.makeMove(board, Colour.BLACK, "Ra6", false);
        ChessTestUtils.makeMove(board, Colour.WHITE, "Nb4", false);
        ChessTestUtils.makeMove(board, Colour.BLACK, "Ra5", false);

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "exd6", false);

        assertFalse(mvr.isValid());
    }

}
