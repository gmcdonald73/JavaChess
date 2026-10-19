package engine.players;

import engine.board.Board;
import engine.moves.Move;
import engine.pieces.Colour;
import engine.moves.MoveParseResult;
import engine.moves.MoveParser;
import engine.moves.MoveRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

public class HumanPlayer extends ChessPlayer {

    BufferedReader reader;

    public HumanPlayer(String name, Colour colour) {
        super(name, colour);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public PlayerAction getAction(Board board, List<Move> moveHistory)
    {
        // Get move from console/UI
        MoveRequest moveRequest = null;
        String input = "";
        boolean isValidMove = false;

        do {
            System.out.print("> ");
            try {
                input = reader.readLine();
            }
            catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
            }

            if (input.equalsIgnoreCase("quit"))
                return PlayerAction.quit();

            if (input.equalsIgnoreCase("help"))
                return PlayerAction.help();

            if (input.equalsIgnoreCase("resign"))
                return PlayerAction.resign();

            //InterpretMove(board, input, move);
            MoveParseResult mpr = MoveParser.parse(input);

            if(mpr.isValid())
            {
                moveRequest = mpr.getMoveRequest();
                isValidMove = true;
            }
            else
            {
                System.out.println(mpr.getErrorMessage());
            }
        } while (!isValidMove);

        return PlayerAction.moveRequest(moveRequest);
    }
}