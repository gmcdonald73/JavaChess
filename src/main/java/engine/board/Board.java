package engine.board;

import engine.moves.Move;
import engine.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board
{

	public static final int MAX_X = 8;
	public static final int MAX_Y = 8;

	private List<Piece> _whitePieces = new ArrayList<Piece>();
	private List<Piece> _blackPieces = new ArrayList<Piece>();

	public List<Piece> getPiecesForColour(Colour colour) { return colour == Colour.WHITE ? _whitePieces : _blackPieces; }

	private Piece _whiteKing;
	private Piece _blackKing;

	public Piece getKing(Colour colour) { return colour == Colour.WHITE ? _whiteKing : _blackKing; }

	private Square _enPassantTarget;

	public Square getEnPassantTarget() { return _enPassantTarget; }

	// The positions on the board, lookup what piece is there
	private Piece[][] _position = new Piece[Board.MAX_X][Board.MAX_Y];

	private int _halfMoveClock;
	public int getHalfMoveClock() { return _halfMoveClock; }

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

		_whiteKing = null;
		_blackKing = null;
		_enPassantTarget = null;
		_halfMoveClock = 0;

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
		setupFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	}

	public void setupFromFEN(String fen)
	{
		if (fen == null || fen.isBlank())
		{
			throw new IllegalArgumentException("FEN cannot be empty");
		}

		String[] fields = fen.trim().split("\\s+");

		if (fields.length != 6)
		{
			throw new IllegalArgumentException(
					"FEN must contain six fields");
		}

		// 1. Clear the current position.
		EmptyBoard();

		// 2. Set up piece placement.
		String[] ranks = fields[0].split("/");

		if (ranks.length != 8)
		{
			throw new IllegalArgumentException(
					"FEN must contain eight ranks");
		}

		for (int fenRank = 0; fenRank < 8; fenRank++)
		{
			int file = 0;
			int y = 7 - fenRank;

			for (char c : ranks[fenRank].toCharArray())
			{
				if (Character.isDigit(c))
				{
					file += Character.digit(c, 10);
				} else
				{
					Colour colour =
							Character.isUpperCase(c)
									? Colour.WHITE
									: Colour.BLACK;

					PieceRank rank = switch (
							Character.toLowerCase(c)) {
						case 'k' -> PieceRank.KING;
						case 'q' -> PieceRank.QUEEN;
						case 'r' -> PieceRank.ROOK;
						case 'b' -> PieceRank.BISHOP;
						case 'n' -> PieceRank.KNIGHT;
						case 'p' -> PieceRank.PAWN;
						default -> throw new IllegalArgumentException(
								"Invalid FEN piece: " + c);
					};

					if (file >= 8) {
						throw new IllegalArgumentException(
								"Too many squares in rank");
					}

					PlacePiece(colour, rank, new Square(file, y));
					file++;
				}
			}

			if (file != 8)
			{
				throw new IllegalArgumentException(
						"Each FEN rank must describe eight squares");
			}
		}

/*
		// 3. Active colour.
		sideToMove = switch (fields[1]) {
			case "w" -> PieceColour.WHITE;
			case "b" -> PieceColour.BLACK;
			default -> throw new IllegalArgumentException(
					"Invalid active colour: " + fields[1]);
		};

		// 4. Castling rights.
		castlingRights = fields[2];

		// 5. En passant target square.
		enPassantTarget = fields[3].equals("-")
				? null
				: parseSquare(fields[3]);

		// 6. Halfmove clock and fullmove number.
		halfmoveClock = Integer.parseInt(fields[4]);
		fullmoveNumber = Integer.parseInt(fields[5]);
*/
	}

	public String saveToFen(Colour currPlayerColour, int moveNum)
	{
		StringBuilder fen = new StringBuilder();

		// 1. Piece placement: rank 8 down to rank 1
		for (int y = 7; y >= 0; y--)
		{
			int emptySquares = 0;

			for (int x = 0; x < 8; x++)
			{
				Piece piece = WhatIsAtAbsPos(x, y);

				if (piece == null)
				{
					emptySquares++;
				} else
				{
					if (emptySquares > 0)
					{
						fen.append(emptySquares);
						emptySquares = 0;
					}

					char symbol = piece.Symbol();

					// Black pieces are lowercase in FEN
					if (piece.Colour() == Colour.BLACK)
					{
						symbol = Character.toLowerCase(symbol);
					}

					fen.append(symbol);
				}
			}

			if (emptySquares > 0)
			{
				fen.append(emptySquares);
			}

			if (y > 0)
			{
				fen.append('/');
			}
		}

		// 2. Active colour
		fen.append(' ').append(currPlayerColour == Colour.WHITE ? 'w' : 'b');

		// 3. Castling availability
		fen.append(' ').append(getCastlingRightsFen());

		// 4. En passant target square
		fen.append(' ').append(getEnPassantTargetFen());

		// 5. Halfmove clock
		fen.append(' ').append(getHalfMoveClock());

		// 6. Fullmove number
		fen.append(' ').append(moveNum);

		return fen.toString();
	}

	public String getCastlingRightsFen()
	{
		String castlingRights = "";
		Piece rook;

		if(_whiteKing != null && !_whiteKing.HasMovedThisGame())
		{

			// Check white kingside
			rook = WhatIsAtSquare(Square.fromSAN("h1"));
			if(rook != null && rook.Rank() == PieceRank.ROOK && !rook.HasMovedThisGame())
				castlingRights += "K";

			// Check white queenside
			rook = WhatIsAtSquare(Square.fromSAN("a1"));
			if(rook != null && rook.Rank() == PieceRank.ROOK && !rook.HasMovedThisGame())
				castlingRights += "Q";
		}

		if(_blackKing != null && !_blackKing.HasMovedThisGame())
		{
			// Check black kingside
			rook = WhatIsAtSquare(Square.fromSAN("h8"));
			if(rook != null && rook.Rank() == PieceRank.ROOK && !rook.HasMovedThisGame())
				castlingRights += "k";

			// Check black queenside
			rook = WhatIsAtSquare(Square.fromSAN("a8"));
			if(rook != null && rook.Rank() == PieceRank.ROOK && !rook.HasMovedThisGame())
				castlingRights += "q";
		}

		if(castlingRights == "")
			castlingRights = "-";

		return castlingRights;
	}

	public String getEnPassantTargetFen()
	{
		// !!! fix this
		return "-";
	}

	public String getPositionKey(Colour currPlayerColour)
	{
		String[] fields = saveToFen(currPlayerColour, 0).split("\\s+");

		// Piece placement, active colour, castling rights,
		// and en passant target square.
		return String.join(" ", fields[0], fields[1], fields[2], fields[3]);
	}

	public Piece PlacePiece(Colour colour, PieceRank rank, String pos)
	{
		return PlacePiece(colour, rank, Square.fromSAN(pos));
	}

	public Piece PlacePiece(Colour colour, PieceRank rank, Square square)
	{
		if (colour == null)
			throw new IllegalArgumentException("Colour cannot be null");

		if (rank == null)
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
					throw new IllegalArgumentException("Black already has a king");

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
			case KING   -> new King(colour, square);
			case QUEEN  -> new Queen(colour, square);
			case ROOK   -> new Rook(colour, square);
			case BISHOP -> new Bishop(colour, square);
			case KNIGHT -> new Knight(colour, square);
			case PAWN   -> new Pawn(colour, square);
			default -> throw new IllegalArgumentException(
					"Unsupported piece rank: " + rank);
		};
	}

	private void updateHalfMoveClock(Move move)
	{
		if (move.getRank() == PieceRank.PAWN || move.isCapture())
		{
			_halfMoveClock = 0;
		}
		else
		{
			_halfMoveClock++;
		}
	}

	public void MakeMove(Move move)
	{
		// Move the piece
		MovePiece(move);

		updateHalfMoveClock(move);

		// Castling stuff
		if((move.isCastleQueenSide()) || (move.isCastleKingSide()))
		{
			Move rookMove;

			if(move.isCastleQueenSide())
			{
				if(move.getColour() == Colour.WHITE)
				{
					rookMove = new Move(move.getColour(), PieceRank.ROOK, Square.fromSAN("a1"), Square.fromSAN("d1"));
				}
				else
				{
					rookMove = new Move(move.getColour(), PieceRank.ROOK, Square.fromSAN("a8"), Square.fromSAN("d8"));
				}
			}
			else
			{
				if(move.getColour() == Colour.WHITE)
				{
					rookMove = new Move(move.getColour(), PieceRank.ROOK, Square.fromSAN("h1"), Square.fromSAN("f1"));
				}
				else
				{
					rookMove = new Move(move.getColour(), PieceRank.ROOK, Square.fromSAN("h8"), Square.fromSAN("f8"));
				}
			}

			Piece piece = WhatIsAtSquare(rookMove.getStartPosition());

			if((piece == null) || (piece.Rank() != PieceRank.ROOK))
			{
				assert false;;
			}

			MovePiece(rookMove);
		}
	}

	public Piece WhatIsAtSquare(Square pos)
	{
		return WhatIsAtAbsPos(pos.getX(), pos.getY());
	}

	public Piece WhatIsAtAbsPos(int x, int y)
	{
		assert IsAbsPositionOnBoard(x, y);

		return _position[x][y];
	}

	public Piece WhatIsAtRelPos(Colour colour, int rx, int ry)
	{
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		relPos._colour = colour;
		relPos._rx = rx;
		relPos._ry = ry;

		ConvertRelToAbsCoords(relPos, absPos);

		return WhatIsAtAbsPos(absPos._x, absPos._y);
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
		return ax >= 0 && ax < MAX_X &&
				ay >= 0 && ay < MAX_Y;
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
		assert piece.Colour() == move.getColour();
		assert piece.Rank() == move.getRank();

		// Check if anything is at the end point
		Piece pieceAtEndPos = _position[endX][endY];

		if(pieceAtEndPos != null)
		{
			// If it is a friendly piece then the move is illegal
			assert pieceAtEndPos.Colour() != move.getColour();

			// If it is an enemy then remove it
			if(pieceAtEndPos.Colour() == move.getColour().opposite())
			{
				RemovePiece(pieceAtEndPos.Colour(), pieceAtEndPos.Rank(), endPos);
			}
		}

		// Handle en passant captures
		if( move.isEnPassantCapture() )
		{
			// This is the square the pawn moves to
			int endPosX = move.getEndPosition().getX();
			int endPosY = move.getEndPosition().getY();

			// Determine tne square of the pawn being captured
			if(move.getColour() == Colour.WHITE)
				endPosY--;
			else
				endPosY++;

			Square epPawnSq = new Square(endPosX, endPosY);

			Piece epCapturePiece = WhatIsAtSquare(epPawnSq);

			assert epCapturePiece != null;
			assert epCapturePiece.Colour() != move.getColour();
			assert epCapturePiece.Rank() == PieceRank.PAWN;

			RemovePiece(epCapturePiece.Colour(), epCapturePiece.Rank(), epPawnSq);
		}

		if(piece.Rank() == PieceRank.PAWN && (Math.abs(endY - startY) == 2))
		{
			// Pawn has moved two squares then set en passant target square
			_enPassantTarget = new Square(endX, (startY + endY) / 2);
		}
		else
		{
			_enPassantTarget = null;
		}

		// Move piece to destination
		if(move.getPromoteTo() != null)
		{
			RemovePiece(piece.Colour(), piece.Rank(), startPos);
			PlacePiece(piece.Colour(), move.getPromoteTo(), endPos);
		}
		else
		{
			_position[endX][endY] = piece;
			piece.setPosition(endPos);
		}

		// Remove piece from starting square
		_position[startX][startY] = null;

	}

	public void RemovePiece(Colour colour, PieceRank rank, String pos)
	{
		RemovePiece(colour, rank, Square.fromSAN(pos));
	}

	public void RemovePiece(Colour colour, PieceRank rank, Square position)
	{
		int ax = position.getX();
		int ay = position.getY();
		Piece piece;

		// Check position is on the board
        assert IsAbsPositionOnBoard(ax, ay);

		piece = _position[ax][ay];

		assert piece != null;
		assert piece.Colour() == colour;
		assert piece.Rank() == rank;

		// Remove the piece from the board
		_position[ax][ay] = null;

		// Remove piece from the list of pieces
		if (piece.Colour() == Colour.WHITE)
		{
			_whitePieces.remove(piece);
			if(rank == PieceRank.KING) _whiteKing = null;
		}
		else
		{
			_blackPieces.remove(piece);
			if(rank == PieceRank.KING) _blackKing = null;
		}
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

		// Also copy _enPassantTarget
		_enPassantTarget = other._enPassantTarget;
	}

	public boolean IsKingInCheck(Colour colour)
	{
		Piece king;
		boolean isInCheck = false;

		king = getKing(colour);

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
		List<Piece> pieceList = getPiecesForColour(playerColour);

		for (Piece piece : pieceList)
		{
			if (piece.CanPieceCaptureAt(this, rColour, rx, ry))
			{
				return true;
			}
		}

		return false;
	}

	public long MaterialValue(Colour playerColour)
	{
		long materialValue = 0;

		List<Piece> pieceList = getPiecesForColour(playerColour);

		for (Piece piece : pieceList)
		{
			materialValue += piece.MaterialValue();
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

		return testBoard.IsKingInCheck(colour);
	}

	// NOTE : This routine should NOT modify the board
	// !!! Modify this (or GetMoves on Piece) to exclude moves that would place king in check?
	public void GetMoves(List<Move> moves, Colour playerColour)
	{
		List<Piece> pieceList = getPiecesForColour(playerColour);

		// Empty the movestack
		moves.clear();

		for (Piece piece : pieceList)
		{
			piece.GetMoves(this, moves, false);
		}
	}

	// Returns true if the army has one or more legal moves
	// Returns false if the army is checkmated or stalemated
	public boolean HasAnyLegalMoves(Colour playerColour)
	{
		List<Piece> pieceList = getPiecesForColour(playerColour);
		List<Move> possibleMoves = new ArrayList<>();

		for (Piece piece : pieceList)
		{
			possibleMoves.clear();
			piece.GetMoves(this, possibleMoves, false);

			for(Move move : possibleMoves)
			{
				// Is the move legal?
				if( !WillMovePutKingInCheck(move, playerColour) )
				{
					return true;
				}
			}
		}

		return false;
	}
}

