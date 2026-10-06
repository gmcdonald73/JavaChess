package engine;

public enum GameResult
{
    WHITE_WIN("1-0"),
    BLACK_WIN("0-1"),
    DRAW("1/2-1/2"),
    UNFINISHED("*");

    private final String pgnValue;

    GameResult(String pgnValue)
    {
        this.pgnValue = pgnValue;
    }

    public String getPgnValue()
    {
        return pgnValue;
    }
}
