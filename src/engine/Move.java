package engine;

public class Move
{
	public int _colour;
	public int _number;
	public int _rank;
	public int _startRX; // These are relative for each army
	public int _startRY;
	public int _endRX; // These are relative for each army
	public int _endRY;
	public boolean _bIsCapture; // Is the move a capture?
	public int _promoteTo;
	public boolean _bIsCheck;
	public boolean _bIsMate;
	public boolean _bCastleQueenSide;
	public boolean _bCastleKingSide;
	public boolean _bEnPassantCapture;
	public int _score;
	public boolean _bLegalMove;
	public boolean _bIsDevelopingMove; // !!! What to do with this?

	public void Copy(Move src)
	{
		_colour = src._colour;
		_number = src._number;
		_rank = src._rank;
		_startRX = src._startRX; 
		_startRY = src._startRY;
		_endRX = src._endRX; 
		_endRY = src._endRY;
		_bIsCapture = src._bIsCapture; 
		_promoteTo = src._promoteTo;
		_bIsCheck = src._bIsCheck;
		_bIsMate = src._bIsMate;
		_bCastleQueenSide = src._bCastleQueenSide;
		_bCastleKingSide = src._bCastleKingSide;
		_bEnPassantCapture = src._bEnPassantCapture;
		_score = src._score;
		_bLegalMove = src._bLegalMove;
	}


	public void Print(int moveNum)
	{
		String sMove;

		sMove = Format(moveNum);
		System.out.println(sMove);
	}


	public String Format(int moveNum)
	{
		String sMove = "";
		int absStartX = 0;
		int absStartY = 0;
		int absEndX = 0;
		int absEndY = 0;

		if(_colour == pieceColourType.PC_NONE)
		{
			sMove = "Null Move";
			return sMove;
		}

		sMove = moveNum + ": ";

		if(_colour == pieceColourType.PC_WHITE)
		{
			sMove = sMove + "White ";
		}
		else
		{
			sMove = sMove + "Black ";
		}

		if( _bCastleKingSide )
		{
			sMove = sMove + "O-O";
		}
		else if( _bCastleQueenSide )
		{
			sMove = sMove + "O-O-O";
		}
		else
		{
			if( _rank != pieceRankType.PR_PAWN )
			{
				sMove = sMove + Board.ConvertRankToSymbol(_rank);
			}

			// Convert source and destination to absolute co-ords
			relPosType startRelPos = new relPosType();
			relPosType endRelPos = new relPosType();
			posType startPos = new posType();
			posType endPos = new posType();

			startRelPos._colour = _colour;
			startRelPos._rx = _startRX;
			startRelPos._ry = _startRY;

			Board.ConvertRelToAbsCoords(startRelPos, startPos);

			absStartX = startPos._x;
			absStartY = startPos._y;

			endRelPos._colour = _colour;
			endRelPos._rx = _endRX;
			endRelPos._ry = _endRY;

			Board.ConvertRelToAbsCoords(endRelPos, endPos);

			absEndX = endPos._x;
			absEndY = endPos._y;

			// Display source
			sMove = sMove + ((char)('a' + absStartX)) + (absStartY + 1);

			if(_bIsCapture)
			{
				sMove = sMove + "x";
			}
			else
			{
				sMove = sMove + "-";
			}

			// Display destination
			sMove = sMove + ((char)('a' + absEndX)) + (absEndY + 1);

			// If a pawn is being promoted, display the piece that it is promoted to
			if( (_rank == pieceRankType.PR_PAWN) && (_endRY == 8) )
			{
				sMove = sMove + Board.ConvertRankToSymbol(_promoteTo);
			}
		}

		// If check then display '+'
		if( _bIsCheck )
		{
			sMove = sMove + "+";
		}

		// If it is mate then display another one
		if( _bIsMate )
		{
			sMove = sMove + "+";
		}

		// Display score
//		sMove = sMove + " : " + _score;

		return sMove;
	}
}


