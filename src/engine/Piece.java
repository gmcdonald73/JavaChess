package engine;

// IMPORTANT : Pieces should not retain any state information. All state information should be
// stored in (or derived from) the cBoard class


public abstract class Piece
{
	protected final Colour _colour;
	protected final int _number;

	protected PieceRank _rank;
	protected long _materialValue;

	public Piece(Colour colour, int number)
	{
		_colour = colour;
		_number = number;
	}

	protected Piece(Piece other)
	{
		_colour = other._colour;
		_number = other._number;
		_rank = other._rank;
		_materialValue = other._materialValue;
	}

	public abstract Piece copy();

	// NOTE : This routine should NOT modify the board
	public abstract void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly);

	// NOTE : This routine should NOT modify the board
	public boolean CanPieceCaptureAt(Board board, Colour colour, int rx, int ry)
	{
		relPosType posMyCoords = new relPosType(); // The position to capture at in our co-ordinate system

		if(colour != _colour)
		{
			relPosType posEnemyCoords = new relPosType(); // The position to capture at in the enemy co-ordinate system

			posEnemyCoords._colour = colour;
			posEnemyCoords._rx = rx;
			posEnemyCoords._ry = ry;

			board.InvertCoords(posEnemyCoords, posMyCoords);
		}
		else
		{
			// The coords are already in my coordinate system
			posMyCoords._colour = colour;
			posMyCoords._rx = rx;
			posMyCoords._ry = ry;
		}

		MoveStack possibleMoves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

		GetMoves(board, possibleMoves, true);

		for(int i=0; i<possibleMoves.getNumMoves(); i++)
		{
			// !!! This won't work for en passant captures (Doesn't matter at the moment as
			// this method is currently only called for determining if king is in check )
			if( possibleMoves.getMove(i)._bIsCapture && 
				(possibleMoves.getMove(i)._endRX == posMyCoords._rx) &&
				(possibleMoves.getMove(i)._endRY == posMyCoords._ry))
			{
				// This piece can make a capture at the given square
				return true;
			}
		}

		return false;
	}


	public Colour Colour() { return _colour; }

	// NOTE : This routine should NOT modify the board
	// !!! fix this so I don't need to pass in board
	public PieceRank Rank(Board board) { return _rank; }

	public int Number() { return _number; }

	// NOTE : This routine should NOT modify the board
	public char Symbol(Board board)
	{
		return Rank(board).getSymbol();
	}

	public long MaterialValue() { return _materialValue; }

	public void AddMove(MoveStack moveStack, int startX, int startY, int endX, int endY, boolean bIsCapture, PieceRank promoteTo)
	{
		Move move = new Move();

		move._colour = _colour;
		move._number = _number;
		move._rank = _rank;
		move._startRX = startX;
		move._startRY = startY;
		move._endRX = endX;
		move._endRY = endY;
		move._bIsCapture = bIsCapture;
		move._promoteTo = promoteTo;
		move._bIsCheck = false;
		move._bIsMate = false;
		move._score = Constants.MS_UNDEFINED;
		move._bCastleQueenSide = false;
		move._bCastleKingSide = false;
		move._bEnPassantCapture = false;

		moveStack.AddMove(move);
	}

}




	





