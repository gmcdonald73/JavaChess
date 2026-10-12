package engine;

import engine.board.Board;
import engine.moves.*;
import engine.pieces.Colour;

public class ChessTestUtils
{
    static MoveValidationResult makeMove(Board board, Colour colour, String input)
    {
        return makeMove(board, colour, input, true);
    }

    static MoveValidationResult makeMove(Board board, Colour colour, String input, boolean throwError)
    {
        MoveParseResult mpr = MoveParser.parse(input);

        if (!mpr.isValid())
        {
            if(throwError)
                throw new AssertionError(input + " - " + mpr.getErrorMessage());
            else
            {
                MoveValidationResult mvr = new MoveValidationResult(false, mpr.getErrorMessage());
                return mvr;
            }
        }

        MoveRequest moveRequest = mpr.getMoveRequest();

        MoveResolutionResult mrr = MoveResolver.resolve(board, moveRequest, colour);

        if (!mrr.isSuccess())
        {
            if(throwError)
                throw new AssertionError(input + " - " + mrr.getErrorMessage());
            else
            {
                MoveValidationResult mvr = new MoveValidationResult(false, mrr.getErrorMessage());
                return mvr;
            }
        }

        Move move = mrr.getMove();

        MoveValidationResult mvr = MoveValidator.IsMoveLegal(board, colour, move);

        if (!mvr.isValid())
        {
            if(throwError) throw new AssertionError(input + " - " + mvr.getError());
        }
        else
        {
            board.MakeMove(move);
        }

        return mvr;

    }

}
