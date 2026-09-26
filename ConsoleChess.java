import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleChess {

    public static void main(String[] args) throws Exception {

        Board board = new Board();

        Army[] army = new Army[2];
        army[0] = new Army();
        army[1] = new Army();

        // Initialise the two sides
        army[0].Initialise(
                pieceColourType.PC_WHITE,
                board,
                army[1]);

        army[1].Initialise(
                pieceColourType.PC_BLACK,
                board,
                army[0]);

        // Set up the starting position
        army[0].SetupForGame(board);
        army[1].SetupForGame(board);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        Move[] moveStack =
                new Move[JavaChess.MAX_MOVES_LOOKAHEAD];

        for (int i = 0; i < moveStack.length; i++) {
            moveStack[i] = new Move();
        }

        int armyNum = 0;
        int moveNum = 1;
        boolean gameOver = false;

        board.DisplayBoard();

        while (!gameOver) {

            System.out.println(
                    (armyNum == 0 ? "White" : "Black") + "'s move:");

            boolean legalMove = false;

            if (armyNum == 0) {
                // Human player
                while (!legalMove) {

                    System.out.print("> ");
                    String input = reader.readLine();

                    if (input == null ||
                            input.equalsIgnoreCase("quit")) {
                        gameOver = true;
                        break;
                    }

                    Move move = new Move();

                    army[armyNum].InterpretMove(
                            board,
                            input,
                            null,
                            move);

                    legalMove = move._bLegalMove;

                    if (!legalMove) {
                        System.out.println(
                                army[armyNum].GetErrMsg());
                    } else {
                        board.MakeMove(move);

                        System.out.println(
                                "White: " + move.Format(moveNum));

                        board.DisplayBoard();
                    }
                }

            } else {
                // Computer player
                Move[] engineMoves =
                        new Move[JavaChess.MAX_MOVES_LOOKAHEAD];

                for (int i = 0; i < engineMoves.length; i++) {
                    engineMoves[i] = new Move();
                }

                legalMove = army[armyNum].FindBestMove(
                        moveNum,
                        board,
                        JavaChess.NUM_MOVES_LOOKAHEAD,
                        engineMoves);

                // View the best line calculated by the engine
/*
                for (var move : engineMoves) {
                    System.out.println("Enginemoves : " + move.Format(0) + " score = " + move._score);
                }
*/

                if (legalMove) {

                    Move move = engineMoves[0];

                    System.out.println(
                            "Black: " + move.Format(moveNum));

                    board.MakeMove(move);

                    board.DisplayBoard();
                }
            }

            if (gameOver) {
                break;
            }

            if (!legalMove) {
                if (army[armyNum].IsKingInCheck(board)) {
                    System.out.println("Checkmate!");
                } else {
                    System.out.println("Stalemate!");
                }

                break;
            }

            // Change sides
            armyNum++;

            if (armyNum > 1) {
                armyNum = 0;
                moveNum++;
            }
        }

        System.out.println("Game over.");
    }
}