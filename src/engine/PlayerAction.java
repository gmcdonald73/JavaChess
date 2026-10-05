package engine;

public class PlayerAction
{
    public enum ActionType
    {
        MOVE,
        QUIT,
        HELP,
        RESIGN,
        DRAW_OFFER
    }

    private final ActionType _type;
    private final Move _move;

    private PlayerAction(ActionType type, Move move)
    {
        _type = type;
        _move = move;
    }

    public static PlayerAction move(Move move)
    {
        if (move == null)
            throw new IllegalArgumentException("Move cannot be null");

        return new PlayerAction(ActionType.MOVE, move);
    }

    public static PlayerAction quit()
    {
        return new PlayerAction(ActionType.QUIT, null);
    }

    public static PlayerAction help()
    {
        return new PlayerAction(ActionType.HELP, null);
    }

    public static PlayerAction resign()
    {
        return new PlayerAction(ActionType.RESIGN, null);
    }

    public static PlayerAction drawOffer()
    {
        return new PlayerAction(ActionType.DRAW_OFFER, null);
    }

    public ActionType getType()
    {
        return _type;
    }

    public Move getMove()
    {
        return _move;
    }

    public boolean isMove()
    {
        return _type == ActionType.MOVE;
    }

    public boolean isQuit()
    {
        return _type == ActionType.QUIT;
    }

    public boolean isHelp()
    {
        return _type == ActionType.HELP;
    }

    public boolean isResign()
    {
        return _type == ActionType.RESIGN;
    }

    public boolean isDrawOffer()
    {
        return _type == ActionType.DRAW_OFFER;
    }
}