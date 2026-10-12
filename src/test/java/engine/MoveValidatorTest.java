package engine;

import engine.board.Board;
import engine.moves.*;
import engine.pieces.Colour;

import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static engine.ChessTestUtils.makeMove;
//import static engine.ChessTestUtils.makeMove2;
import static org.junit.jupiter.api.Assertions.*;

public class MoveValidatorTest
{
    /*
    Tests to add:
    check it recognises pinned pieces when determining legal moves
     */

    @Test
    void foolsMateTest()
    {
        Board board = new Board();

        board.SetupForGame();

        makeMove(board, Colour.WHITE, "f3");
        makeMove(board, Colour.BLACK, "e6");
        makeMove(board, Colour.WHITE, "g4");
        makeMove(board, Colour.BLACK, "Qh4");

        assertTrue(board.IsKingInCheck(Colour.WHITE));
        assertFalse(board.HasAnyLegalMoves(Colour.WHITE));

    }


}
