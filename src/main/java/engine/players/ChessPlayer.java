package engine.players;

import engine.board.Board;
import engine.moves.Move;
import engine.pieces.Colour;

import java.util.List;

public abstract class ChessPlayer {

    private final String _name;
    private Colour _colour;

    protected ChessPlayer(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public Colour getColour() {
        return _colour;
    }
    public void setColour(Colour colour) { _colour = colour; }

    public abstract PlayerAction getAction(Board board, List<Move> moveHistory);
}