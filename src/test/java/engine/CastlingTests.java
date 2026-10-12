package engine;

import engine.board.Board;
import engine.moves.MoveValidationResult;
import engine.pieces.Colour;
import engine.pieces.Piece;
import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static engine.ChessTestUtils.makeMove;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CastlingTests
{
    /*
    Also test that castling fails if
    king would end up in check
    pawn attacks pass through sq
    pawn attacks end sq

    Also add some queenside tests?
     */

    @Test
    void castleSucceeds()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "0-0", false);

        assertTrue(mvr.isValid());
    }

    @Test
    void castleFailsIfKingNotAtStartPos()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.KING, "d1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void castleFailsIfRookNotAtStartPos()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "g1");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void castleFailsIfKingHasMoved()
    {
        Board board = new Board();
        Piece king = board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

        ChessTestUtils.makeMove(board, Colour.WHITE, "Kd1");
        ChessTestUtils.makeMove(board, Colour.WHITE, "Ke1");
        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void castleFailsIfRookHasMoved()
    {
        Board board = new Board();
        Piece king = board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

        ChessTestUtils.makeMove(board, Colour.WHITE, "Rg1");
        ChessTestUtils.makeMove(board, Colour.WHITE, "Rh1");
        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void castleFailsWhenPieceIsInTheWay()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "g1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

        MoveValidationResult mvr = ChessTestUtils.makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void castleFailsWhenKingIsInCheck()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");
        board.PlacePiece(Colour.BLACK, PieceRank.ROOK, "e8");

        MoveValidationResult mvr = makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }

    @Test
    void castleFailsWhenKingPassesThroughAttackedSquare()
    {
        Board board = new Board();
        board.PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
        board.PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

        // Set up a black piece that attacks f1.
        // Add the necessary pieces for your engine's attack detection.
        board.PlacePiece(Colour.BLACK, PieceRank.ROOK, "f8");

        MoveValidationResult mvr = makeMove(board, Colour.WHITE, "0-0", false);

        assertFalse(mvr.isValid());
    }


}
