package engine;

public class MoveParseResult
{
    private final boolean valid;
    private final Move move;
    private final String errorMessage;

    private MoveParseResult(boolean valid, Move move, String errorMessage)
    {
        this.valid = valid;
        this.move = move;
        this.errorMessage = errorMessage;
    }

    public static MoveParseResult success(Move move)
    {
        return new MoveParseResult(true, move, null);
    }

    public static MoveParseResult failure(String errorMessage)
    {
        return new MoveParseResult(false, null, errorMessage);
    }

    public boolean isValid()
    {
        return valid;
    }

    public Move getMove()
    {
        return move;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}