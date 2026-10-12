package engine;

import engine.board.*;
import engine.moves.*;
import engine.pieces.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveResolverTest {

    /*
    tests to add:
    resolve move using rank disambiguation
    when rank or file disambiguation fails and you need both
     */

    @Test
    void resolveFullySpecifiedMove()
    {
        Board board = new Board();

        MoveRequest request = new MoveRequest(
                PieceRank.KNIGHT,
                Square.fromSAN("f3")
        );

        request.setStartPosition(Square.fromSAN("g1"));

        MoveResolutionResult result = MoveResolver.resolve(board, request, Colour.WHITE);

        assertTrue(result.isSuccess());

        Move move = result.getMove();

        assertEquals(Square.fromSAN("g1"), move.getStartPosition());
        assertEquals(Square.fromSAN("f3"), move.getEndPosition());
    }

    @Test
    void resolveFailsForAmbiguousMove()
    {
        // White knights on b1 and f3.
        // "Nd2" should fail.

        Board board = new Board();

        board.PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "b1");
        board.PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "f3");

        MoveRequest request = new MoveRequest(
                PieceRank.KNIGHT,
                Square.fromSAN("d2")
        );

        MoveResolutionResult result = MoveResolver.resolve(board, request, Colour.WHITE);

        assertFalse(result.isSuccess());
    }

    @Test
    void resolveMoveUsingFileDisambiguation()
    {
        // White knights on b1 and f3.
        // "Nbd2" should select the knight on b1.

        Board board = new Board();

        board.PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "b1");
        board.PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "f3");

        MoveRequest request = new MoveRequest(
                PieceRank.KNIGHT,
                Square.fromSAN("d2")
        );

        request.setDisambiguationFile('b');

        MoveResolutionResult result = MoveResolver.resolve(board, request, Colour.WHITE);

        assertTrue(result.isSuccess());

/*
        Move move = result.getMove();

        assertEquals(new Square(1, 0), move.getStartPosition());
        assertEquals(new Square(3, 1), move.getEndPosition());
*/
    }


}
