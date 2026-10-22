package engine.game;

import engine.board.Board;
import engine.moves.*;
import engine.pieces.Colour;
import engine.players.BotPlayer;
import engine.players.ChessPlayer;
import engine.players.PlayerAction;
import ui.GraphicalChess;

import java.util.ArrayList;
import java.util.List;


public class ChessGame {

    private final ChessPlayer _white;
    private final ChessPlayer _black;
    private final Board board;
    private List<Move> moves = new ArrayList<>();
    private GameResult result;

    public ChessGame(ChessPlayer white, ChessPlayer black) {

        _white = white;
        _white.setColour(Colour.WHITE);

        _black = black;
        _black.setColour(Colour.BLACK);

        this.board = new Board();
        this.result = GameResult.UNFINISHED;
    }

    public ChessPlayer getWhitePlayer() { return _white; }
    public ChessPlayer getBlackPlayer() { return _black; }
    public GameResult getResult() {return result;}
    public List<Move> getMoves() { return moves; }

    public GameResult play(boolean showGUI, boolean showConsoleMessages)
    {

        GraphicalChess gui = null;

        if(showGUI)
        {
            gui = new GraphicalChess(board);
        }

        ChessPlayer currentPlayer = _white;
        int moveNum = 1;

        board.SetupForGame();

        boolean isGameOver = false;

        while (!isGameOver)
        {

            if(gui != null) gui.refresh();

            if(showConsoleMessages)
            {
                board.DisplayBoard();
                System.out.println((currentPlayer == _white ? "White" : "Black") + "'s move:");
            }

            long startTime = System.nanoTime();

            PlayerAction action = currentPlayer.getAction(board, moves);

            long elapsedTime = System.nanoTime() - startTime;
            double elapsedSeconds = elapsedTime / 1_000_000_000.0;

            if(showConsoleMessages && currentPlayer instanceof BotPlayer bot)
            {
                System.out.println("Num positions analysed: " + bot.getNumPositionsAnalysed());
                System.out.printf("Time taken: %.3f seconds%n", elapsedSeconds);
                // Sleep(100);
            }

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
                            if(showConsoleMessages)
                            {
                                if (currentPlayer == _white)
                                    System.out.println(moveNum + ": White: " + move.getSan());
                                else
                                    System.out.println(moveNum + ": Black: " + move.getSan());
                            }

                            if(currentPlayer == _black) moveNum++;

                            // Switch players - see if opponent is in check or mated
                            currentPlayer = (currentPlayer == _white ? _black : _white);

                            // Check for Checkmake, Stalemate, draw by repetition, 50 move rule, insufficient forces?
                            isGameOver = IsGameOver(currentPlayer, move);

                            // !!! fix this. simple hack to break out of infinite loop so we can save the game
                            if (moveNum > 100) {
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

        if(showConsoleMessages) board.DisplayBoard();

        // Should really display board, then wait for player to close game? (if human player)
        // gui.refresh();

        // !!! should also close on exceptions
        if(gui != null) gui.close();

        return result;

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
                // System.out.println("Checkmate!!!");
                move._bIsMate = true;
                result = currentColour == Colour.WHITE ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
            }
            else
            {
                // System.out.println("Stalemate!!!");
                result = GameResult.DRAW;
            }
        }

        return isGameOver;
    }

    private void Sleep(int ms)
    {
        // Pause before the next move.
        //if (currentPlayer instanceof BotPlayer)
        {
            try
            {
                Thread.sleep(ms);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                //break;
            }
        }
    }
}