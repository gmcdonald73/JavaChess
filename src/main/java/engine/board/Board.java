package engine.board;

import engine.*;
import engine.moves.Move;
import engine.moves.MoveStack;
import engine.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board
{

	public static final int MAX_X = 8;
	public static final int MAX_Y = 8;
	public static final int MAX_ARMIES = 2;

	private List<Piece> _whitePieces = new ArrayList<Piece>();
	private List<Piece> _blackPieces = new ArrayList<Piece>();

	private Piece _whiteKing;
	private Piece _blackKing;

	// The positions on the board, lookup what piece is there
	private Piece[][] _position = new Piece[Board.MAX_X][Board.MAX_Y];

	public List<Piece> getWhitePieces() {
		return _whitePieces;
	}
	public List<Piece> getBlackPieces() {
		return _blackPieces;
	}

	// public methods
	public Board()
	{
		EmptyBoard();
	}

	public void EmptyBoard()
	{
		int x, y;

		_whitePieces.clear();
		_blackPieces.clear();

		for(x=0; x<MAX_X; x++)
		{
			for(y=0; y<MAX_Y; y++)
			{
				_position[x][y] = null;
			}
		}
	}

	public void SetupForGame()
	{
		// Place white pieces
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "a2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "b2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "c2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "d2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "e2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "f2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "g2");
		PlacePiece(Colour.WHITE, PieceRank.PAWN, "h2");

		PlacePiece(Colour.WHITE, PieceRank.ROOK, "a1");
		PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "b1");
		PlacePiece(Colour.WHITE, PieceRank.BISHOP, "c1");
		PlacePiece(Colour.WHITE, PieceRank.QUEEN, "d1");
		PlacePiece(Colour.WHITE, PieceRank.KING, "e1");
		PlacePiece(Colour.WHITE, PieceRank.BISHOP, "f1");
		PlacePiece(Colour.WHITE, PieceRank.KNIGHT, "g1");
		PlacePiece(Colour.WHITE, PieceRank.ROOK, "h1");

		// Place black pieces
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "a7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "b7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "c7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "d7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "e7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "f7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "g7");
		PlacePiece(Colour.BLACK, PieceRank.PAWN, "h7");

		PlacePiece(Colour.BLACK, PieceRank.ROOK, "a8");
		PlacePiece(Colour.BLACK, PieceRank.KNIGHT, "b8");
		PlacePiece(Colour.BLACK, PieceRank.BISHOP, "c8");
		PlacePiece(Colour.BLACK, PieceRank.QUEEN, "d8");
		PlacePiece(Colour.BLACK, PieceRank.KING, "e8");
		PlacePiece(Colour.BLACK, PieceRank.BISHOP, "f8");
		PlacePiece(Colour.BLACK, PieceRank.KNIGHT, "g8");
		PlacePiece(Colour.BLACK, PieceRank.ROOK, "h8");
	}

	public Piece PlacePiece(Colour colour, PieceRank rank, String pos)
	{
		return PlacePiece(colour, rank, Square.fromSAN(pos));
	}

	public Piece PlacePiece(Colour colour, PieceRank rank, Square square)
	{
		if (colour == null)
			throw new IllegalArgumentException("Colour cannot be null");

		if (rank == null || rank == PieceRank.NONE)
			throw new IllegalArgumentException("Invalid piece rank");

		if (square == null)
			throw new IllegalArgumentException("Square cannot be null");

		int x = square.getX();
		int y = square.getY();

		if (x < 0 || x >= Board.MAX_X ||
				y < 0 || y >= Board.MAX_Y)
		{
			throw new IllegalArgumentException("Square is outside the board");
		}

		if (_position[x][y] != null)
			throw new IllegalArgumentException("Square is already occupied");

		Piece piece = createPiece(colour, rank, square);

		if (colour == Colour.WHITE)
		{
			if(rank == PieceRank.KING )
			{
				if(_whiteKing != null)
					throw new IllegalArgumentException("White already has a king");

				_whiteKing = piece;
			}
			_whitePieces.add(piece);
		}
		else
		{
			if(rank == PieceRank.KING )
			{
				if(_blackKing != null)
					throw new IllegalArgumentException("White already has a king");

				_blackKing = piece;
			}

			_blackPieces.add(piece);
		}
		_position[x][y] = piece;

		return piece;
	}

	private Piece createPiece(
			Colour colour,
			PieceRank rank,
			Square square)
	{
		return switch (rank)
		{
			case KING   -> new King(colour, 0, square);
			case QUEEN  -> new Queen(colour, 0, square);
			case ROOK   -> new Rook(colour, 0, square);
			case BISHOP -> new Bishop(colour, 0, square);
			case KNIGHT -> new Knight(colour, 0, square);
			case PAWN   -> new Pawn(colour, 0, square);
			default -> throw new IllegalArgumentException(
					"Unsupported piece rank: " + rank);
		};
	}


	public void MakeMove(Move move)
	{
		// Clear the PawnMovedTwoStepsLastTurn flags
/*
		int pieceNum;

		for(pieceNum=0; pieceNum<Constants.PIECES_PER_SIDE; pieceNum++)
		{
			_piece[move._colour.getValue()][pieceNum]._bPawnMovedTwoStepsLastTurn = false;
		}
*/

		// Move the piece
		MovePiece(move);

		// If the pawn moved two steps then flag it
/*
		if(move._rank == PieceRank.PAWN)
		{
			if((move._endRY - move._startRY) == 2)
			{
				_piece[move._colour.getValue()][move._number]._bPawnMovedTwoStepsLastTurn = true;
			}
		}
*/

		// Castling stuff
		if((move._bCastleQueenSide) || (move._bCastleKingSide))
		{
			Move rookMove = new Move(); // if castling
			Piece piece;

			// !!! Do I need to set all of these?
			rookMove._colour = move._colour;
			rookMove._rank = PieceRank.ROOK;
			rookMove._bIsCapture = false; // Is the move a capture?
			rookMove._promoteTo = PieceRank.NONE;
			rookMove._startRY = 1;
			rookMove._endRY = 1;
			rookMove._bIsCheck = false;
			rookMove._bIsMate = false;
			rookMove._bCastleQueenSide = false;
			rookMove._bCastleKingSide = false;
			rookMove._bEnPassantCapture = false;
			rookMove._score = Constants.MS_UNDEFINED;

			if(move._bCastleQueenSide)
			{
				if(move._colour == Colour.WHITE)
				{
					rookMove._startRX = 1; 
					rookMove._endRX = 4; 
				}
				else
				{
					rookMove._startRX = 8; 
					rookMove._endRX = 5; 
				}
			}
			else // if(move._bCastleKingSide)
			{
				if(move._colour == Colour.WHITE)
				{
					rookMove._startRX = 8; 
					rookMove._endRX = 6; 
				}
				else
				{
					rookMove._startRX = 1; 
					rookMove._endRX = 3; 
				}
			}

			piece = WhatIsAt(move._colour, rookMove._startRX, rookMove._startRY);

			if((piece == null) || (piece.Rank() != PieceRank.ROOK))
			{
				assert false;;
			}

			rookMove._number = piece.Number();

			MovePiece(rookMove);
		}
	}

	public boolean IsPieceAlive(Colour colour, int number)
	{
		// !!! remove this method
		return true;
	}

	public boolean HasPawnMovedTwoStepsLastTurn(Colour colour, int number)
	{
		// !!! Fix this
		return false;
	}

	public Piece WhatIsAtSquare(Square pos)
	{
		if(!IsAbsPositionOnBoard(pos.getX(), pos.getY()))
		{
			assert false;
		}

		return _position[pos.getX()][pos.getY()];
	}

	public Piece WhatIsAt(Colour colour, int rx, int ry)
	{
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		relPos._colour = colour;
		relPos._rx = rx;
		relPos._ry = ry;

		ConvertRelToAbsCoords(relPos, absPos);

		if(!IsAbsPositionOnBoard(absPos._x, absPos._y))
		{
			assert false;
		}

		return _position[absPos._x][absPos._y];
	}

	public void DisplayBoard()
	{
		Piece piece;
		int ax, ay;
		char symbol;

		System.out.print("\n");

		for(ay = MAX_Y-1; ay>=0; ay--)
		{
			// print row number
			System.out.print((ay + 1) + " ");

			for(ax = 0; ax<MAX_X; ax++)
			{
				piece = _position[ax][ay];

				if( piece == null)
				{
					symbol = '_';
				}
				else
				{
					symbol = piece.Rank().getSymbol();

					if( piece.Colour() == Colour.BLACK )
					{
						// Display black pieces in lower case
						symbol = (char)(symbol - 'A' + 'a');
					}
				}

				System.out.print(symbol + "|");

			}

			System.out.print("\n");
		}

		System.out.print("  a|b|c|d|e|f|g|h|\n\n");
	}

	public static void ConvertRelToAbsCoords(relPosType relPos, posType absPos)
	{
		if(relPos._colour == Colour.WHITE)
		{
			absPos._x = relPos._rx - 1;
			absPos._y = relPos._ry - 1;
		}
		else if(relPos._colour == Colour.BLACK)
		{
			absPos._x = MAX_X - relPos._rx;
			absPos._y = MAX_Y - relPos._ry;
		}
		else
		{
			assert false;
		}
	}

	public static void ConvertAbsToRelCoords(Colour colour, int ax, int ay, relPosType relPos)
	{
		relPos._colour = colour;

		if(colour == Colour.WHITE)
		{
			relPos._rx = ax + 1;
			relPos._ry = ay + 1;
		}
		else if(colour == Colour.BLACK)
		{
			relPos._rx = MAX_X - ax;
			relPos._ry = MAX_Y - ay;
		}
		else
		{
			assert false;
		}
	}

	public static void InvertCoords(relPosType posEnemyCoords, relPosType posMyCoords)
	{
		posType absPos  = new posType();

		ConvertRelToAbsCoords(posEnemyCoords, absPos);

		posMyCoords._colour = posEnemyCoords._colour.opposite();

		ConvertAbsToRelCoords(posMyCoords._colour, absPos._x, absPos._y, posMyCoords);

	}

	public static boolean IsAbsPositionOnBoard(int ax, int ay)
	{
		if(ax < 0)
		{
			return false;
		}

		if(ay < 0)
		{
			return false;
		}

		if(ax >= MAX_X)
		{
			return false;
		}

		if(ay >= MAX_Y)
		{
			return false;
		}

		return true;
	}

	public static boolean IsRelPositionOnBoard(Colour colour, int rx, int ry)
	{
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		relPos._colour = colour;
		relPos._rx = rx;
		relPos._ry = ry;

		ConvertRelToAbsCoords(relPos, absPos);

		return IsAbsPositionOnBoard(absPos._x, absPos._y);
	}

// private methods

// This routine assumes that the move has already been validated and is thus valid for the piece
	private	void MovePiece(Move move)
	{
		Square startPos = move.getStartPosition();
		int startX = startPos.getX();
		int startY = startPos.getY();

		Square endPos = move.getEndPosition();
		int endX = endPos.getX();
		int endY = endPos.getY();

		// Check start pos is on the board
        assert IsAbsPositionOnBoard(startX, startY);

		// Check end pos is on the board
        assert IsAbsPositionOnBoard(endX, endY);

		Piece piece = _position[startX][startY];

        assert piece != null;

		assert piece.Colour() == move._colour;
		assert piece.Rank() == move._rank;

		// Check if anything is at the end point
		Piece pieceAtEndPos = _position[endX][endY];

		if(pieceAtEndPos != null)
		{
			// If it is a friendly piece then the move is illegal
			assert pieceAtEndPos.Colour() != move._colour;

			// If it is an enemy then remove it
			if(pieceAtEndPos.Colour() == move._colour.opposite())
			{
				RemovePiece(pieceAtEndPos.Colour(), pieceAtEndPos.Rank(), endPos);
			}
		}

		// !!! Fix this later
/*
		// Handle en passant captures
		if( move._bEnPassantCapture )
		{
			relPosType epCaptureRelPos = new relPosType();
			posType epCapturePos = new posType();

			epCaptureRelPos._colour = move._colour;
			epCaptureRelPos._rx = move._endRX;
			epCaptureRelPos._ry = move._endRY - 1;

			ConvertRelToAbsCoords(epCaptureRelPos, epCapturePos);

			RemovePiece(_position[epCapturePos._x][epCapturePos._y].Colour(), _position[epCapturePos._x][epCapturePos._y].Number(), epCapturePos._x, epCapturePos._y);
		}
*/

		// Move piece to destination
		if(move._promoteTo != PieceRank.NONE)
		{
			RemovePiece(piece.Colour(), piece.Rank(), startPos);
			PlacePiece(piece.Colour(), move._promoteTo, endPos);
		}
		else
		{
			_position[endX][endY] = piece;
			piece.setPosition(endPos);
		}

		// Remove piece from starting square
		_position[startX][startY] = null;

	}

	private	void RemovePiece(Colour colour, PieceRank rank, Square position)
	{
		int ax = position.getX();
		int ay = position.getY();
		Piece piece;

		// Check position is on the board
		if(!IsAbsPositionOnBoard(ax, ay))
		{
			assert false;
		}

		piece = _position[ax][ay];

		assert piece != null;
		assert piece.Colour() == colour;
		assert piece.Rank() == rank;

		// Remove the piece from the board
		_position[ax][ay] = null;

		// Remove piece from the list of pieces
		if (piece.Colour() == Colour.WHITE)
			_whitePieces.remove(piece);
		else
			_blackPieces.remove(piece);
	}

	public Board(Board other)
	{
		_whitePieces = new ArrayList<>();
		_blackPieces = new ArrayList<>();

		_position = new Piece[Board.MAX_X][Board.MAX_Y];

		// Copy white pieces.
		for (Piece piece : other._whitePieces)
		{
			Piece copy = piece.copy();
			_whitePieces.add(copy);

			if(copy.Rank() == PieceRank.KING)
				_whiteKing = copy;

			Square square = copy.getPosition();
			_position[square.getX()][square.getY()] = copy;
		}

		// Copy black pieces.
		for (Piece piece : other._blackPieces)
		{
			Piece copy = piece.copy();
			_blackPieces.add(copy);

			if(copy.Rank() == PieceRank.KING)
				_blackKing = copy;

			Square square = copy.getPosition();
			_position[square.getX()][square.getY()] = copy;
		}
	}

	public boolean IsKingInCheck(Colour colour)
	{
		Piece king;
		boolean isInCheck = false;

		king = colour == Colour.WHITE ? _whiteKing : _blackKing;

		if(king != null)
		{
			relPosType relPos = new relPosType();

			ConvertAbsToRelCoords(colour, king.getPosition().getX(), king.getPosition().getY(), relPos);
			isInCheck = CanCaptureAt(colour.opposite(), colour, relPos._rx, relPos._ry);
		}

		return isInCheck;
	}

	// Can the playerColour army capture a piece at the relative co-ordinate (rColour, rx, ry)?
	public boolean CanCaptureAt(Colour playerColour, Colour rColour, int rx, int ry)
	{
		List<Piece> pieceList;

		if(playerColour == Colour.WHITE)
			pieceList = _whitePieces;
		else
			pieceList = _blackPieces;

		for (Piece piece : pieceList)
		{
			if (IsPieceAlive(playerColour, piece.Number()))
			{
				if (piece.CanPieceCaptureAt(this, rColour, rx, ry))
				{
					return true;
				}
			}
		}

		return false;
	}

	public long MaterialValue(Colour playerColour)
	{
		long materialValue = 0;

		List<Piece> pieceList;

		if(playerColour == Colour.WHITE)
			pieceList = _whitePieces;
		else
			pieceList = _blackPieces;

		for (Piece piece : pieceList)
		{
			if (IsPieceAlive(playerColour, piece.Number()))
			{
				materialValue += piece.MaterialValue();
			}
		}

		return materialValue;
	}

	// This routine assume that the move is a valid move for the piece,
	// It determines whether the move would put the king in check
	public boolean WillMovePutKingInCheck(Move move, Colour colour)
	{
		Board testBoard = new Board(this);

		// Make the move
		testBoard.MakeMove(move);

		if( testBoard.IsKingInCheck(colour) )
		{
			return true;
		}

		return false;
	}

	// NOTE : This routine should NOT modify the board
	// !!! Modify this (or GetMoves on Piece) to exclude moves that would place king in check?
	public void GetMoves(MoveStack moves, Colour playerColour)
	{
		List<Piece> pieceList;

		if(playerColour == Colour.WHITE)
			pieceList = _whitePieces;
		else
			pieceList = _blackPieces;

		// Empty the movestack
		moves.Clear();

		for (Piece piece : pieceList)
		{
			if (IsPieceAlive(playerColour, piece.Number()))
			{
				piece.GetMoves(this, moves, false);
			}
		}
	}

	// Returns true if the army has one or more legal moves
	// Returns false if the army is checkmated or stalemated
	public boolean HasAnyLegalMoves(Colour playerColour)
	{
		List<Piece> pieceList;

		if(playerColour == Colour.WHITE)
			pieceList = _whitePieces;
		else
			pieceList = _blackPieces;

		int idxMove;
		MoveStack possibleMoves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

		for (Piece piece : pieceList)
		{
			if (IsPieceAlive(playerColour, piece.Number()))
			{
				possibleMoves.Clear();
				piece.GetMoves(this, possibleMoves, false);

				for(idxMove=0; idxMove<possibleMoves.getNumMoves(); idxMove++)
				{
					// Is the move legal?
					if( !WillMovePutKingInCheck(possibleMoves.getMove(idxMove), playerColour) )
					{
						return true;
					}
				}
			}
		}

		return false;
	}
}

