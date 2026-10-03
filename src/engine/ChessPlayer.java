package engine;

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

    public abstract Move getMove(Board board);
}