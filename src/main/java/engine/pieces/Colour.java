package engine.pieces;

public enum Colour
{
	WHITE(0),
	BLACK(1),
	NONE(2);

	private final int value;

	Colour(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public Colour opposite()
	{
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
            default -> NONE;
        };
	}
}
