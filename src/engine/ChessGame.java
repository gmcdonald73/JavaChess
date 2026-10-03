package engine;

import java.util.ArrayList;
import java.util.List;


public class ChessGame {

    private final ChessPlayer white;
    private final ChessPlayer black;
    private final Board board;
    private List<Move> moves = new ArrayList<>();
    private GameResult result;

    public ChessGame(ChessPlayer white, ChessPlayer black) {

        this.white = white;
        this.black = black;
        this.board = new Board();
        this.result = GameResult.UNFINISHED;
    }

    public ChessPlayer getWhitePlayer() { return white; }
    public ChessPlayer getBlackPlayer() { return black; }
    public GameResult getResult() {return result;}
    public List<Move> getMoves() { return moves; }

    public void play() {

        ChessPlayer currentPlayer = white;
        Colour currentColour;
        int moveNum = 1;

        board.SetupForGame();

        boolean isGameOver = false;

        while (!isGameOver)
        {

            board.DisplayBoard();

            System.out.println((currentPlayer == white ? "White" : "Black") + "'s move:");

            Move move = currentPlayer.getMove(board);

            MoveValidationResult mvr = board.IsMoveLegal(currentPlayer.getColour(), move);

            if (mvr.isValid())
            {
                board.MakeMove(move);

                // Display move
                if (currentPlayer == white)
                    System.out.println(moveNum + ": White: " + move.getSan());
                else
                {
                    System.out.println(moveNum + ": Black: " + move.getSan());
                    moveNum++;
                }

                // Switch players - see if opponent is in check or mated
                currentPlayer = (currentPlayer == white ? black : white);
                currentColour = currentPlayer.getColour();

                move._bIsCheck = false;
                move._bIsMate = false;

                if(board.IsKingInCheck(currentColour))
                {
                    move._bIsCheck = true;
                }

                if(!board.HasAnyLegalMoves(currentColour))
                {
                    isGameOver = true;

                    if(move._bIsCheck)
                    {
                        System.out.println("Checkmate!!!");
                        move._bIsMate = true;
                        result = currentColour == Colour.WHITE ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
                    }
                    else
                    {
                        System.out.println("Stalemate!!!");
                        result = GameResult.DRAW;
                    }
                }

                moves.add(move);

                // !!! fix this. simple hack to break out of infinite loop so we can save the game
                if(moveNum > 50)
                {
                    isGameOver = true;
                }

            }
            else
            {
                System.out.println(mvr.getError());
            }
        }
    }
}