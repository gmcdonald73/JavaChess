package engine.players;

import engine.board.Board;
import engine.moves.Move;
import engine.pieces.Colour;

import java.util.List;

public abstract class ChessPlayer {

    private final String name;
    private final Colour colour;

    protected ChessPlayer(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }

    public abstract PlayerAction getAction(Board board, List<Move> moveHistory);
}