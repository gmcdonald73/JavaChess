package engine;

public class BotPlayer extends ChessPlayer {

    public BotPlayer(String name, int colour) {
        super(name, colour);
    }

    @Override
    public Move getMove(Board board) {
        // Calculate move
        return new Move();
    }
}