package ui;

import engine.*;

public class TestEngine {

    public static void main(String[] args) throws Exception {

        Board board = new Board();
        BotPlayer white = new BotPlayer("whitePlayer",Colour.WHITE);
        BotPlayer black = new BotPlayer("blackPlayer",Colour.BLACK);

        board.SetupForGame();

        makeMove(Colour.WHITE, "Pe2-e4", board);
        makeMove(Colour.BLACK, "Pe7-e5", board);

        board.DisplayBoard();

        long whiteArmyRating1 = white.RateArmy(board, Colour.WHITE);
        long blackArmyRating1 = black.RateArmy(board, Colour.BLACK);
        int whiteSituation1 = white.RateSituation(board, Colour.WHITE);
        int blackSituation1 = black.RateSituation(board, Colour.BLACK);

    }

    static boolean makeMove(Colour colour, String input, Board board)
    {

        MoveParseResult mpr = Move.parse(colour, input);
        boolean success = false;

        if(mpr.isValid())
        {
            Move move = mpr.getMove();
            MoveValidationResult mvr = board.IsMoveLegal(colour, move); // this also determines piece number

            if(mvr.isValid())
            {
                board.MakeMove(move);
                success = true;
            }
        }

        return success;
    }
}
