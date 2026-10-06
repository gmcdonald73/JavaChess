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
    private final MoveRequest _moveRequest;

    private PlayerAction(ActionType type, MoveRequest moveRequest)
    {
        _type = type;
        _moveRequest = moveRequest;
    }

    public static PlayerAction moveRequest(MoveRequest moveRequest)
    {
        if (moveRequest == null)
            throw new IllegalArgumentException("MoveRequest cannot be null");

        return new PlayerAction(ActionType.MOVE, moveRequest);
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

    public MoveRequest getMoveRequest()
    {
        return _moveRequest;
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