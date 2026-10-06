package engine;

public class MoveParseResult
{
    private final boolean valid;
    private final MoveRequest moveRequest;
    private final String errorMessage;

    private MoveParseResult(boolean valid, MoveRequest moveRequest, String errorMessage)
    {
        this.valid = valid;
        this.moveRequest = moveRequest;
        this.errorMessage = errorMessage;
    }

    public static MoveParseResult success(MoveRequest moveRequest)
    {
        return new MoveParseResult(true, moveRequest, null);
    }

    public static MoveParseResult error(String errorMessage)
    {
        return new MoveParseResult(false, null, errorMessage);
    }

    public boolean isValid()
    {
        return valid;
    }

    public MoveRequest getMoveRequest()
    {
        return moveRequest;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }
}