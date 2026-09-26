import javax.swing.*;


// IMPORTANT : Pieces should not retain any state information. All state information should be
// stored in (or derived from) the cBoard class


public abstract class Piece
{

	public Piece(Army pArmy, int colour, int number)
	{
		_pArmy = pArmy;
		_colour = colour;
		_number = number;
	}

	// NOTE : This routine should NOT modify the board
	public abstract void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly);

	// NOTE : This routine should NOT modify the board
	public boolean CanPieceCaptureAt(Board board, int colour, int rx, int ry)
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

		MoveStack possibleMoves = new MoveStack(JavaChess.MAX_PIECE_POSSIBLE_MOVES);

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


	public int Colour() { return _colour; }

	// NOTE : This routine should NOT modify the board
	public int Rank(Board board) { return _rank; }

	public int Number() { return _number; }

	// NOTE : This routine should NOT modify the board
	public char Symbol(Board board)
	{
		return Board.ConvertRankToSymbol(Rank(board));
	}

	public long MaterialValue() { return _materialValue; }

	public void AddMove(MoveStack moveStack, int startX, int startY, int endX, int endY, boolean bIsCapture, int promoteTo)
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
		move._score = JavaChess.MS_UNDEFINED;
		move._bCastleQueenSide = false;
		move._bCastleKingSide = false;
		move._bEnPassantCapture = false;

		moveStack.AddMove(move);
	}

	protected int _colour;
	protected int _rank;
	protected int _number;
	protected long _materialValue;
	protected Army _pArmy;
}




	





