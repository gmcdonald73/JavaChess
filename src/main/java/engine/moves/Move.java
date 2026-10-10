package engine.moves;

import engine.*;
import engine.board.*;
import engine.pieces.Colour;
import engine.pieces.PieceRank;

import java.util.Objects;

public class Move
{
	public Colour _colour;
	public int _number;
	public PieceRank _rank;
	public boolean _bIsCapture; // Is the move a capture?
	public PieceRank _promoteTo;
	public boolean _bCastleQueenSide;
	public boolean _bCastleKingSide;
	public boolean _bEnPassantCapture;

	// Store absolute co-ords
	private Square _startPosition;
	private Square _endPosition;

	// Move the stuff below out of this class?

	// These are mainly for bot & piece calculations
	public int _startRX; // These are relative for each army
	public int _startRY;
	public int _endRX; // These are relative for each army
	public int _endRY;
	public int _score;

	// These two are just for displaying the move. Do these belong in Move class?
	public boolean _bIsCheck;
	public boolean _bIsMate;

	public Move()
	{
		_colour = Colour.NONE;
		_number = 0; //PieceNumber.NONE;
		_rank = PieceRank.NONE;
		_startRX = -1; // These are relative for each army
		_startRY = -1;
		_endRX = -1; // These are relative for each army
		_endRY = -1;
		_bIsCapture = false; // Is the move a capture?
		_promoteTo = PieceRank.NONE;
		_bIsCheck = false;
		_bIsMate = false;
		_bCastleQueenSide = false;
		_bCastleKingSide = false;
		_bEnPassantCapture = false;
		_score = Constants.MS_UNDEFINED;
	}

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
	}

	public static Move fromMoveRequest(MoveRequest request, Colour colour)
	{
		Move move = new Move();
		move._colour = colour;
		move._rank = request.getRank();
		move._bIsCapture = request.isCapture(); // Is the move a capture?
		move._promoteTo = request.getPromoteTo();
		move._bCastleQueenSide = request.isCastleQueenSide();
		move._bCastleKingSide = request.isCastleKingSide();
		move._startPosition = request.getStartPosition();
		move._endPosition = request.getEndPosition();
		return move;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (!(obj instanceof Move other))
			return false;

		return _startPosition.equals(other._startPosition)
				&& _endPosition.equals(other._endPosition)
				&& _promoteTo == other._promoteTo;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(_startPosition, _endPosition, _promoteTo);
	}

	public void setStartPosition(Square startPosition) {_startPosition = startPosition; }
	public Square getStartPosition() {return _startPosition; }

	public void setEndPosition(Square endPosition) {_endPosition = endPosition; }
	public Square getEndPosition() { return _endPosition; }

	public String StartPos() {
		return FormatRelPos(_colour, _startRX, _startRY);
	}

	public String EndPos() {
		return FormatRelPos(_colour, _endRX, _endRY);
	}

	private String FormatRelPos(Colour colour, int rx, int ry)
	{
		String pos = "";
		int absX = 0;
		int absY = 0;
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		relPos._colour = colour;
		relPos._rx = rx;
		relPos._ry = ry;

		Board.ConvertRelToAbsCoords(relPos, absPos);

		absX = absPos._x;
		absY = absPos._y;

		// Display source
		pos = pos + ((char)('a' + absX)) + (absY + 1);

		return pos;
	}

	public String getSan()
	{
		String san = "";
		int absStartX = 0;
		int absStartY = 0;
		int absEndX = 0;
		int absEndY = 0;

		if(_colour == Colour.NONE)
		{
			san = "Null Move";
			return san;
		}

		if( _bCastleKingSide )
		{
			san += "O-O";
		}
		else if( _bCastleQueenSide )
		{
			san += "O-O-O";
		}
		else
		{
			if( _rank != PieceRank.PAWN )
			{
				san += _rank.getSymbol(); // Board.ConvertRankToSymbol(_rank);

				// !!! Also need to add stuff for disambiguation here
			}

			// san += StartPos();

			if(_bIsCapture)
			{
				// !!! pawn capture need to add source file
				if( _rank == PieceRank.PAWN )
				{
					san += StartPos().charAt(0);
				}

				san += "x";
			}

			// Display destination
			san += EndPos();

			// If a pawn is being promoted, display the piece that it is promoted to
			if( (_rank == PieceRank.PAWN) && (_endRY == 8) )
			{
				san += "=" + _promoteTo.getSymbol();  //Board.ConvertRankToSymbol(_promoteTo);
			}
		}

		if( _bIsMate )
		{
			san += "#";
		}
		else if( _bIsCheck )
		{
			san += "+";
		}

		return san;
	}
}


