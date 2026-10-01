package engine;

public abstract class ChessPlayer {

    private final String name;
    private final int colour;

    protected ChessPlayer(String name, int colour) {
        this.name = name;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public int getColour() {
        return colour;
    }

    public abstract Move getMove(Board board);
}