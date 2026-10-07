package engine.moves;

public class MoveResolutionResult
{
    private final boolean _success;
    private final Move _move;
    private final String _errorMessage;

    private MoveResolutionResult(
            boolean success,
            Move move,
            String errorMessage)
    {
        _success = success;
        _move = move;
        _errorMessage = errorMessage;
    }

    public static MoveResolutionResult success(Move move)
    {
        if (move == null)
            throw new IllegalArgumentException("Move cannot be null");

        return new MoveResolutionResult(true, move, null);
    }

    public static MoveResolutionResult error(String errorMessage)
    {
        if (errorMessage == null || errorMessage.isBlank())
            throw new IllegalArgumentException("Error message cannot be empty");

        return new MoveResolutionResult(false, null, errorMessage);
    }

    public boolean isSuccess()
    {
        return _success;
    }

    public Move getMove()
    {
        return _move;
    }

    public String getErrorMessage()
    {
        return _errorMessage;
    }
}