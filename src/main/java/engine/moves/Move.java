package engine.moves;

import engine.*;
import engine.board.*;
import engine.pieces.Colour;
import engine.pieces.PieceRank;

import java.util.Objects;

public class Move
{
	private Colour _colour;
	private PieceRank _rank;
	private boolean _bIsCapture; // Is the move a capture?
	private PieceRank _promoteTo;
	private boolean _bCastleQueenSide;
	private boolean _bCastleKingSide;
	private boolean _bEnPassantCapture;

	// Store absolute co-ords
	private Square _startPosition;
	private Square _endPosition;

	// Does this belong in this class?
	public int _score;

	// These two are just for displaying the move. Do these belong in Move class?
	public boolean _bIsCheck;
	public boolean _bIsMate;

	public Colour getColour()
	{
		return _colour;
	}

	public PieceRank getRank()
	{
		return _rank;
	}

	public boolean isCapture()
	{
		return _bIsCapture;
	}

	public PieceRank getPromoteTo()
	{
		return _promoteTo;
	}

	public boolean isCastleQueenSide()
	{
		return _bCastleQueenSide;
	}

	public boolean isCastleKingSide()
	{
		return _bCastleKingSide;
	}

	public boolean isEnPassantCapture()
	{
		return _bEnPassantCapture;
	}

	public void setIsCapture(boolean bIsCapture)
	{
		this._bIsCapture = bIsCapture;
	}

	public void setPromoteTo(PieceRank promoteTo)
	{
		this._promoteTo = promoteTo;
	}

	public void setCastleQueenSide(boolean bCastleQueenSide)
	{
		this._bCastleQueenSide = bCastleQueenSide;
	}

	public void setCastleKingSide(boolean bCastleKingSide)
	{
		this._bCastleKingSide = bCastleKingSide;
	}

	public void setEnPassantCapture(boolean bEnPassantCapture)
	{
		this._bEnPassantCapture = bEnPassantCapture;
	}

	public Move()
	{
		_colour = Colour.NONE;
		_rank = null;
		_bIsCapture = false; // Is the move a capture?
		_promoteTo = null;
		_bIsCheck = false;
		_bIsMate = false;
		_bCastleQueenSide = false;
		_bCastleKingSide = false;
		_bEnPassantCapture = false;
		_score = -1;
	}

	public Move(Colour colour, PieceRank rank, Square startPosition, Square endPosition)
	{
		_colour = colour;
		_rank = rank;
		_startPosition = startPosition;
		_endPosition = endPosition;

		_bIsCapture = false; // Is the move a capture?
		_promoteTo = null;
		_bIsCheck = false;
		_bIsMate = false;
		_bCastleQueenSide = false;
		_bCastleKingSide = false;
		_bEnPassantCapture = false;
		_score = -1;
	}

	public Move(Move src)
	{
		_colour = src._colour;
		_rank = src._rank;
		_bIsCapture = src._bIsCapture;
		_promoteTo = src._promoteTo;
		_bIsCheck = src._bIsCheck;
		_bIsMate = src._bIsMate;
		_bCastleQueenSide = src._bCastleQueenSide;
		_bCastleKingSide = src._bCastleKingSide;
		_bEnPassantCapture = src._bEnPassantCapture;
		_score = src._score;

		// Square is immutable, so sharing references is safe
		_startPosition = src._startPosition;
		_endPosition = src._endPosition;
	}

	// !!! Remove this and just use copy constructor?
	public void Copy(Move src)
	{
		_colour = src._colour;
		_rank = src._rank;
		_bIsCapture = src._bIsCapture;
		_promoteTo = src._promoteTo;
		_bIsCheck = src._bIsCheck;
		_bIsMate = src._bIsMate;
		_bCastleQueenSide = src._bCastleQueenSide;
		_bCastleKingSide = src._bCastleKingSide;
		_bEnPassantCapture = src._bEnPassantCapture;
		_score = src._score;

		// Square is immutable, so sharing references is safe
		_startPosition = src._startPosition;
		_endPosition = src._endPosition;
	}

	public static Move fromMoveRequest(MoveRequest request, Colour colour)
	{
		Move move = new Move(colour, request.getRank(), request.getStartPosition(), request.getEndPosition());

		move._bIsCapture = request.isCapture(); // Is the move a capture?
		move._promoteTo = request.getPromoteTo();
		move._bCastleQueenSide = request.isCastleQueenSide();
		move._bCastleKingSide = request.isCastleKingSide();

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

/*
	public void UpdateAbsolutePositions()
	{
		setStartPosition( Square.fromRelCoords(_colour, _startRX, _startRY));
		setEndPosition( Square.fromRelCoords(_colour, _endRX, _endRY));
	}
*/


/*
	public String StartPos() {
		return FormatRelPos(_colour, _startRX, _startRY);
	}
	public String EndPos() {
		return FormatRelPos(_colour, _endRX, _endRY);
	}
*/

/*
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
*/

	public String getSan()
	{
		return getSan(false,false);
	}

	public String getSan(boolean showFileDisambiguation, boolean showRankDisambiguation)
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
				san += _rank.getSymbol();

				if(showFileDisambiguation)
					san += _startPosition.getFile();

				if(showRankDisambiguation)
					san += _startPosition.getRank();
			}

			if(_bIsCapture)
			{
				// pawn capture need to add source file
				if( _rank == PieceRank.PAWN )
				{
					san += _startPosition.getFile();
				}

				san += "x";
			}

			// Display destination
			san += _endPosition.toSAN();

			// If a pawn is being promoted, display the piece that it is promoted to
			if( (_rank == PieceRank.PAWN) && (_promoteTo != null) )
			{
				san += "=" + _promoteTo.getSymbol();
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


