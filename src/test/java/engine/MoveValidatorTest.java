package engine;

import engine.board.Board;
import engine.moves.*;
import engine.pieces.Colour;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveValidatorTest
{
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

    static void makeMove(Board board, Colour colour, String input)
    {
        MoveParseResult mpr = MoveParser.parse(input);

        if (!mpr.isValid())
        {
            throw new AssertionError(input + " - " + mpr.getErrorMessage());
        }

        MoveRequest moveRequest = mpr.getMoveRequest();

        MoveResolutionResult mrr = MoveResolver.resolve(board, moveRequest, colour);

        if (!mrr.isSuccess())
        {
            throw new AssertionError(input + " - " + mrr.getErrorMessage());
        }

        Move move = mrr.getMove();

        MoveValidationResult mvr = MoveValidator.IsMoveLegal(board, colour, move);

        if (!mvr.isValid())
        {
            throw new AssertionError(input + " - " + mvr.getError());
        }

        board.MakeMove(move);

    }
}
