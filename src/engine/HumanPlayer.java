package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class HumanPlayer extends ChessPlayer {

    BufferedReader reader;

    public HumanPlayer(String name, Colour colour) {
        super(name, colour);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Move getMove(Board board) {
        // Get move from console/UI
        Move move = new Move();
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

            //InterpretMove(board, input, move);
            MoveParseResult mpr = Move.parse(getColour(), input);

            if(mpr.isValid())
            {
                move = mpr.getMove();
                isValidMove = true;
            }
            else {
                System.out.println(mpr.getErrorMessage());
            }
        } while (!isValidMove);

        return move;
    }
}