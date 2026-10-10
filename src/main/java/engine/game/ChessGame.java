package engine.game;

import engine.board.Board;
import engine.moves.*;
import engine.pieces.Colour;
import engine.players.ChessPlayer;
import engine.players.PlayerAction;

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
        int moveNum = 1;

        board.SetupForGame();

        boolean isGameOver = false;

        while (!isGameOver)
        {

            board.DisplayBoard();

            System.out.println((currentPlayer == white ? "White" : "Black") + "'s move:");

            PlayerAction action = currentPlayer.getAction(board);

            switch (action.getType())
            {
                case MOVE:
                    MoveRequest moveRequest = action.getMoveRequest();

                    MoveResolutionResult mrr = MoveResolver.resolve(board, moveRequest, currentPlayer.getColour());

                    if(!mrr.isSuccess())
                    {
                        System.out.println(mrr.getErrorMessage());
                    }
                    else
                    {
                        Move move = mrr.getMove();

                        // Validate the move and update the board
                        MoveValidationResult mvr = MoveValidator.IsMoveLegal(board, currentPlayer.getColour(), move);

                        if (!mvr.isValid())
                        {
                            System.out.println(mvr.getError());
                        }
                        else
                        {
                            board.MakeMove(move);
                            moves.add(move);

                            // Display move
                            if (currentPlayer == white)
                                System.out.println(moveNum + ": White: " + move.getSan());
                            else {
                                System.out.println(moveNum + ": Black: " + move.getSan());
                                moveNum++;
                            }

                            // Switch players - see if opponent is in check or mated
                            currentPlayer = (currentPlayer == white ? black : white);

                            // Check for Checkmake, Stalemate, draw by repetition, 50 move rule, insufficient forces?
                            isGameOver = IsGameOver(currentPlayer, move);

                            // !!! fix this. simple hack to break out of infinite loop so we can save the game
                            if (moveNum > 50) {
                                isGameOver = true;
                            }
                        }
                    }
                    break;

                case QUIT:
                    // End the game
                    isGameOver = true;
                    break;

                case HELP:
                    // Display help
                    break;

                case RESIGN:
                    // End the game with the current player resigning
                    isGameOver = true;
                    break;

                case DRAW_OFFER:
                    // Handle draw offer
                    break;
            }

        }

        board.DisplayBoard();

    }

    boolean IsGameOver(ChessPlayer currentPlayer, Move move)
    {
        boolean isGameOver = false;

        Colour currentColour = currentPlayer.getColour();

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

        return isGameOver;
    }
}