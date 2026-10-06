package ui;

import engine.*;

public class TestEngine {

    public static void main(String[] args) throws Exception {

        Board board = new Board();
        BotPlayer white = new BotPlayer("whitePlayer",Colour.WHITE);
        BotPlayer black = new BotPlayer("blackPlayer",Colour.BLACK);

        board.SetupForGame();

        makeMove(Colour.WHITE, "i4", board);
        makeMove(Colour.WHITE, "e9", board);
        makeMove(Colour.WHITE, "Kd0", board);

        makeMove(Colour.WHITE, "e4", board);
        makeMove(Colour.BLACK, "e5", board);

        board.DisplayBoard();

        long whiteArmyRating1 = white.RateArmy(board, Colour.WHITE);
        long blackArmyRating1 = black.RateArmy(board, Colour.BLACK);
        int whiteSituation1 = white.RateSituation(board, Colour.WHITE);
        int blackSituation1 = black.RateSituation(board, Colour.BLACK);

    }

    static boolean makeMove(Colour colour, String input, Board board)
    {

        MoveParseResult mpr = MoveParser.parse(input);

        if(!mpr.isValid())
        {
            System.out.println(input + " - " + mpr.getErrorMessage());
            return false;
        }

        MoveRequest moveRequest = mpr.getMoveRequest();

        MoveResolutionResult mrr = MoveResolver.resolve(board, moveRequest, colour);

        if(!mrr.isSuccess())
        {
            System.out.println(input + " - " + mrr.getErrorMessage());
            return false;
        }

        Move move = mrr.getMove();
        MoveValidationResult mvr = MoveValidator.IsMoveLegal(board, colour, move);
        if(!mvr.isValid())
        {
            System.out.println(input + " - " + mvr.getError());
            return false;
        }

        board.MakeMove(move);

        return true;
    }
}
