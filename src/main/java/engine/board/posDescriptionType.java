package engine.board;

public class posDescriptionType
{
	boolean _bIsAlive;
	int _ax;
	int _ay;
	boolean _bHasMovedThisGame;
	boolean _bPawnMovedTwoStepsLastTurn;

	public posDescriptionType()
	{

	}

	public posDescriptionType(posDescriptionType other)
	{
		this._bIsAlive = other._bIsAlive;
		this._ax = other._ax;
		this._ay = other._ay;
		this._bHasMovedThisGame = other._bHasMovedThisGame;
		this._bPawnMovedTwoStepsLastTurn = other._bPawnMovedTwoStepsLastTurn;
	}
}
