package engine;

public class ChessGame {

    private final ChessPlayer white;
    private final ChessPlayer black;
    private final Board board;

    public ChessGame(ChessPlayer white, ChessPlayer black) {

        this.white = white;
        this.black = black;
        this.board = new Board();
    }

    public void play() {

        ChessPlayer currentPlayer = white;
        int moveNum = 1;

        board.SetupForGame();

        boolean isGameOver = false;

        while (!isGameOver) {

            board.DisplayBoard();

            System.out.println((currentPlayer == white ? "White" : "Black") + "'s move:");

            Move move = currentPlayer.getMove(board);

            MoveValidationResult mvr = board.IsMoveLegal(currentPlayer.getColour(), move);

            if (mvr.isValid()) {
                board.MakeMove(move);

                if (currentPlayer == white)
                    System.out.println("White: " + move.Format(moveNum));
                else {
                    System.out.println("Black: " + move.Format(moveNum));
                    moveNum++;
                }

                currentPlayer = (currentPlayer == white ? black : white);

                if(!board.HasAnyLegalMoves(currentPlayer.getColour()))
                {
                    isGameOver = true;

                    if(board.IsKingInCheck(currentPlayer.getColour())){
                        System.out.println("Checkmate!!!");
                    }
                    else {
                        System.out.println("Stalemate!!!");
                    }
                }
            }
            else {
                System.out.println(mvr.getError());
            }
        }
    }
}