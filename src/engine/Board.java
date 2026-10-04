package engine;

class pieceDescriptionType
{
	PieceRank _rank;
	Colour _colour;
	int _number;

	public pieceDescriptionType()
	{
	}

	public pieceDescriptionType(pieceDescriptionType other)
	{
		this._rank = other._rank;
		this._colour = other._colour;
		this._number = other._number;
	}
}

class posDescriptionType
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

public class Board
{

	public static final int MAX_X = 8;
	public static final int MAX_Y = 8;
	public static final int MAX_ARMIES = 2;

	private Piece[] _whitePieces = new Piece[Constants.PIECES_PER_SIDE];
	private Piece[] _blackPieces = new Piece[Constants.PIECES_PER_SIDE];

	// The positions on the board, lookup what piece is there
	private	pieceDescriptionType[][] _position = new pieceDescriptionType[Board.MAX_X][Board.MAX_Y];

	// The pieces on either side, lookup where it is, or if it is dead
	private	posDescriptionType[][] _piece =  new posDescriptionType[Board.MAX_ARMIES][Constants.PIECES_PER_SIDE];


	// public methods
	public Board()
	{
		int x, y;

		for(x=0; x<MAX_X; x++)
		{
			for(y=0; y<MAX_Y; y++)
			{
				_position[x][y] = new pieceDescriptionType();
			}
		}

		int armyNum, pieceNum;

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<Constants.PIECES_PER_SIDE; pieceNum++)
			{
				_piece[armyNum][pieceNum] = new posDescriptionType();
			}
		}

		EmptyBoard();

		// !!! Need to setup board for game

	}

	public void EmptyBoard()
	{
		int x, y;

		for(x=0; x<MAX_X; x++)
		{
			for(y=0; y<MAX_Y; y++)
			{
				_position[x][y]._colour = Colour.NONE;
				_position[x][y]._rank = PieceRank.NONE;
				_position[x][y]._number = PieceNumber.NONE;
			}
		}

		int armyNum, pieceNum;

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<Constants.PIECES_PER_SIDE; pieceNum++)
			{
				_piece[armyNum][pieceNum]._bIsAlive = false;
				_piece[armyNum][pieceNum]._ax = -1;
				_piece[armyNum][pieceNum]._ay = -1;
				_piece[armyNum][pieceNum]._bHasMovedThisGame = false; 
				_piece[armyNum][pieceNum]._bPawnMovedTwoStepsLastTurn = false;
			}
		}
	}

	public void SetupForGame()
	{
		Colour colour = Colour.WHITE;

		_whitePieces[PieceNumber.PAWN_1] = new Pawn( colour, PieceNumber.PAWN_1);
		_whitePieces[PieceNumber.PAWN_2] = new Pawn( colour, PieceNumber.PAWN_2);
		_whitePieces[PieceNumber.PAWN_3] = new Pawn( colour, PieceNumber.PAWN_3);
		_whitePieces[PieceNumber.PAWN_4] = new Pawn( colour, PieceNumber.PAWN_4);
		_whitePieces[PieceNumber.PAWN_5] = new Pawn( colour, PieceNumber.PAWN_5);
		_whitePieces[PieceNumber.PAWN_6] = new Pawn( colour, PieceNumber.PAWN_6);
		_whitePieces[PieceNumber.PAWN_7] = new Pawn( colour, PieceNumber.PAWN_7);
		_whitePieces[PieceNumber.PAWN_8] = new Pawn( colour, PieceNumber.PAWN_8);
		_whitePieces[PieceNumber.ROOK_1] = new Rook( colour, PieceNumber.ROOK_1);
		_whitePieces[PieceNumber.ROOK_2] = new Rook( colour, PieceNumber.ROOK_2);
		_whitePieces[PieceNumber.KNIGHT_1] = new Knight( colour, PieceNumber.KNIGHT_1);
		_whitePieces[PieceNumber.KNIGHT_2] = new Knight( colour, PieceNumber.KNIGHT_2);
		_whitePieces[PieceNumber.BISHOP_1] = new Bishop( colour, PieceNumber.BISHOP_1);
		_whitePieces[PieceNumber.BISHOP_2] = new Bishop( colour, PieceNumber.BISHOP_2);
		_whitePieces[PieceNumber.QUEEN] = new Queen( colour, PieceNumber.QUEEN);
		_whitePieces[PieceNumber.KING] = new King( colour, PieceNumber.KING);

		colour = Colour.BLACK;

		_blackPieces[PieceNumber.PAWN_1] = new Pawn( colour, PieceNumber.PAWN_1);
		_blackPieces[PieceNumber.PAWN_2] = new Pawn( colour, PieceNumber.PAWN_2);
		_blackPieces[PieceNumber.PAWN_3] = new Pawn( colour, PieceNumber.PAWN_3);
		_blackPieces[PieceNumber.PAWN_4] = new Pawn( colour, PieceNumber.PAWN_4);
		_blackPieces[PieceNumber.PAWN_5] = new Pawn( colour, PieceNumber.PAWN_5);
		_blackPieces[PieceNumber.PAWN_6] = new Pawn( colour, PieceNumber.PAWN_6);
		_blackPieces[PieceNumber.PAWN_7] = new Pawn( colour, PieceNumber.PAWN_7);
		_blackPieces[PieceNumber.PAWN_8] = new Pawn( colour, PieceNumber.PAWN_8);
		_blackPieces[PieceNumber.ROOK_1] = new Rook( colour, PieceNumber.ROOK_1);
		_blackPieces[PieceNumber.ROOK_2] = new Rook( colour, PieceNumber.ROOK_2);
		_blackPieces[PieceNumber.KNIGHT_1] = new Knight( colour, PieceNumber.KNIGHT_1);
		_blackPieces[PieceNumber.KNIGHT_2] = new Knight( colour, PieceNumber.KNIGHT_2);
		_blackPieces[PieceNumber.BISHOP_1] = new Bishop( colour, PieceNumber.BISHOP_1);
		_blackPieces[PieceNumber.BISHOP_2] = new Bishop( colour, PieceNumber.BISHOP_2);
		_blackPieces[PieceNumber.QUEEN] = new Queen( colour, PieceNumber.QUEEN);
		_blackPieces[PieceNumber.KING] = new King( colour, PieceNumber.KING);

		// White
		PlacePiece(_whitePieces[PieceNumber.PAWN_1], 'a', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_2], 'b', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_3], 'c', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_4], 'd', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_5], 'e', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_6], 'f', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_7], 'g', 2);
		PlacePiece(_whitePieces[PieceNumber.PAWN_8], 'h', 2);
		PlacePiece(_whitePieces[PieceNumber.ROOK_1], 'a', 1);
		PlacePiece(_whitePieces[PieceNumber.ROOK_2], 'h', 1);
		PlacePiece(_whitePieces[PieceNumber.KNIGHT_1], 'b', 1);
		PlacePiece(_whitePieces[PieceNumber.KNIGHT_2], 'g', 1);
		PlacePiece(_whitePieces[PieceNumber.BISHOP_1], 'c', 1);
		PlacePiece(_whitePieces[PieceNumber.BISHOP_2], 'f', 1);
		PlacePiece(_whitePieces[PieceNumber.QUEEN], 'd', 1);
		PlacePiece(_whitePieces[PieceNumber.KING], 'e', 1);

		// Black
		PlacePiece(_blackPieces[PieceNumber.PAWN_1], 'h', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_2], 'g', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_3], 'f', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_4], 'e', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_5], 'd', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_6], 'c', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_7], 'b', 7);
		PlacePiece(_blackPieces[PieceNumber.PAWN_8], 'a', 7);
		PlacePiece(_blackPieces[PieceNumber.ROOK_1], 'h', 8);
		PlacePiece(_blackPieces[PieceNumber.ROOK_2], 'a', 8);
		PlacePiece(_blackPieces[PieceNumber.KNIGHT_1], 'g', 8);
		PlacePiece(_blackPieces[PieceNumber.KNIGHT_2], 'b', 8);
		PlacePiece(_blackPieces[PieceNumber.BISHOP_1], 'f', 8);
		PlacePiece(_blackPieces[PieceNumber.BISHOP_2], 'c', 8);
		PlacePiece(_blackPieces[PieceNumber.QUEEN], 'd', 8);
		PlacePiece(_blackPieces[PieceNumber.KING], 'e', 8);

	}


	public void PlacePiece(Piece piece, char x, int y)
	{
		Colour colour;
		PieceRank rank;
		int number;

		colour = piece.Colour();
		rank = piece.Rank(this);
		number = piece.Number();

		int ax, ay;

		ax = x - 'a';
		ay = y - 1;

		if( IsAbsPositionOnBoard(ax, ay) )
		{
			// Make sure that there is nothing already there
			if (_position[ax][ay]._colour != Colour.NONE )
			{
				assert false;
			}

			_position[ax][ay]._colour = colour;
			_position[ax][ay]._number = number;
			_position[ax][ay]._rank = rank;

			int colourVal = colour.getValue();

			_piece[colourVal][number]._bIsAlive = true;
			_piece[colourVal][number]._ax = ax;
			_piece[colourVal][number]._ay = ay;
			_piece[colourVal][number]._bPawnMovedTwoStepsLastTurn = false;

			// Set has piece moved flag
			// This only really matters for the rooks and the king to check if castling is allowed,
			// but might as well do it correctly for all pieces
			_piece[colourVal][number]._bHasMovedThisGame = true;

			int rx = 0, ry = 0;
			relPosType relPos = new relPosType();

			ConvertAbsToRelCoords(colour, ax, ay, relPos);
			rx = relPos._rx;
			ry = relPos._ry;

			if( rank == PieceRank.PAWN)
			{
				if( ry == 2)
				{
					_piece[colourVal][number]._bHasMovedThisGame = false;
				}
			}
			else if( rank == PieceRank.ROOK)
			{
				if( (ry == 1) && ((rx==1) || (rx==8)) )
				{
					_piece[colourVal][number]._bHasMovedThisGame = false;
				}
			}
			else if( rank == PieceRank.KNIGHT)
			{
				if( (ry == 1) && ((rx==2) || (rx==7)) )
				{
					_piece[colourVal][number]._bHasMovedThisGame = false;
				}
			}
			else if( rank == PieceRank.BISHOP)
			{
				if( (ry == 1) && ((rx==3) || (rx==6)) )
				{
					_piece[colourVal][number]._bHasMovedThisGame = false;
				}
			}
			else if( rank == PieceRank.QUEEN)
			{
				if( (ry == 1) && (rx==(4 + colourVal)) )
				{
					_piece[colourVal][number]._bHasMovedThisGame = false;
				}
			}
			else if( rank == PieceRank.KING)
			{
				if( (ry == 1) && (rx==(5 - colourVal)) )
				{
					_piece[colourVal][number]._bHasMovedThisGame = false;
				}
			}
			else
			{
				assert false;;
			}
		}
		else
		{
			assert false;
		}
	}

	public MoveValidationResult IsMoveLegal(Colour colour, Move move)
	{
		// Determine piece
		pieceDescriptionType pieceDescrip;
		pieceDescrip = WhatIsAt(colour, move._startRX, move._startRY);
		String errorMessage;

        if (pieceDescrip._rank == PieceRank.NONE )
		{
			errorMessage = "Illegal move - there is no piece at " + move.StartPos();
			return new MoveValidationResult(false, errorMessage);
		}

		if (pieceDescrip._rank != move._rank )
		{
			errorMessage = "Illegal move - piece at " + move.StartPos() + " is not a " + move._rank.getSymbol(); // ConvertRankToSymbol(move._rank);
			return new MoveValidationResult(false, errorMessage);
		}

		if (pieceDescrip._colour != colour )
		{
			errorMessage = "Illegal move - piece at " + move.StartPos() + " is not yours";
			return new MoveValidationResult(false, errorMessage);
		}

		move._number = pieceDescrip._number;

		Piece piece;

		if(colour == Colour.WHITE)
		{
			piece = _whitePieces[pieceDescrip._number];
		}
		else
		{
			piece = _blackPieces[pieceDescrip._number];
		}

        if(piece == null)
        {
            // Couldn't find piece that move is refering to
            errorMessage = "Internal error - null piece pointer";
			return new MoveValidationResult(false, errorMessage);
        }

        int numMoves = 0;
        MoveStack moves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

        // Get the moves for the piece
        piece.GetMoves(this, moves, false);
        numMoves = moves.getNumMoves();

        // Find out if the entered move is one of them
        int i;

        for(i=0; i<numMoves; i++)
        {
            Move currMove;

            currMove = moves.getMove(i);

            if( (move._startRX == currMove._startRX) &&
                    (move._startRY == currMove._startRY) &&
                    (move._endRX == currMove._endRX) &&
                    (move._endRY == currMove._endRY) &&
                    (move._promoteTo == currMove._promoteTo))
            {
                // Found the move in the list of possible moves for the piece

                // Update the move with the one from the moveStack so other flags eg bIsCapture etc.
                // are set correctly
                move.Copy(currMove);

                // Now check if the move would place our king in check

                // Copy the board to a test board
                // Make a copy so that the original board is not modified
                Board testBoard = this.copy();

                // Make the move
                testBoard.MakeMove(move);

                if( testBoard.IsKingInCheck(colour) )
                {
                    errorMessage = "Illegal move - this move would place the king in check";
					return new MoveValidationResult(false, errorMessage);
                }

				return new MoveValidationResult(true, "");
            }
        }

        errorMessage = "Illegal move - this move is not valid for this piece";
		return new MoveValidationResult(false, errorMessage);

	}

	public void MakeMove(Move move)
	{
		// Clear the PawnMovedTwoStepsLastTurn flags
		int pieceNum;

		for(pieceNum=0; pieceNum<Constants.PIECES_PER_SIDE; pieceNum++)
		{
			_piece[move._colour.getValue()][pieceNum]._bPawnMovedTwoStepsLastTurn = false;
		}

		// Move the piece
		MovePiece(move);

		// If the pawn moved two steps then flag it
		if(move._rank == PieceRank.PAWN)
		{
			if((move._endRY - move._startRY) == 2)
			{
				_piece[move._colour.getValue()][move._number]._bPawnMovedTwoStepsLastTurn = true;
			}
		}

		// Castling stuff
		if((move._bCastleQueenSide) || (move._bCastleKingSide))
		{
			Move rookMove = new Move(); // if castling
			pieceDescriptionType pieceDescrip;

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

			pieceDescrip = WhatIsAt(move._colour, rookMove._startRX, rookMove._startRY);

			if(pieceDescrip._rank != PieceRank.ROOK)
			{
				assert false;;
			}

			rookMove._number = pieceDescrip._number;

			MovePiece(rookMove);
		}
	}

	// Change the rank of a pawn. 
	// Note that the pawn doesn't have to be on the 8th row, as we may be loading a position
	public void PromotePawn(Colour colour, int number, PieceRank promoteTo)
	{
		// Get the current position of the pawn
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		WhereIs(colour, number, relPos);

		if( IsRelPositionOnBoard(colour, relPos._rx, relPos._ry) )
		{
			ConvertRelToAbsCoords(relPos, absPos);

			_position[absPos._x][absPos._y]._rank = promoteTo;
		}
	}

	public boolean IsPieceAlive(Colour colour, int number)
	{
		return _piece[colour.getValue()][number]._bIsAlive;
	}

	public boolean HasPieceMovedThisGame(Colour colour, int number)
	{
		return _piece[colour.getValue()][number]._bHasMovedThisGame;
	}

	public boolean HasPawnMovedTwoStepsLastTurn(Colour colour, int number)
	{
		return _piece[colour.getValue()][number]._bPawnMovedTwoStepsLastTurn;
	}

	public void WhereIs(Colour colour, int number, relPosType relPos)
	{
		ConvertAbsToRelCoords(colour, _piece[colour.getValue()][number]._ax, _piece[colour.getValue()][number]._ay, relPos);
	}

	public pieceDescriptionType WhatIsAt(Colour colour, int rx, int ry)
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
		pieceDescriptionType pieceDescrip;
		int ax, ay;
		char symbol;

		System.out.print("\n");

		for(ay = MAX_Y-1; ay>=0; ay--)
		{
			// print row number
			System.out.print((ay + 1) + " ");

			for(ax = 0; ax<MAX_X; ax++)
			{
				pieceDescrip = _position[ax][ay];

				if( pieceDescrip._rank == PieceRank.NONE)
				{
					symbol = '_';
				}
				else
				{
					symbol = pieceDescrip._rank.getSymbol();  // ConvertRankToSymbol(pieceDescrip._rank);

					if( pieceDescrip._colour == Colour.BLACK )
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

	public static void SavePosition(String sFileName)
	{
/*
		int armyNum;
		int pieceNum;
		FILE * fptr;
		pieceDescriptionType pieceDescrip;

		fptr = fopen(sFileName,"w");

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<PIECES_PER_SIDE; pieceNum++)
			{
				if( _piece[armyNum][pieceNum]._bIsAlive )
				{
					if( armyNum == 0 )
					{
						fprintf.out.print(fptr,"W");
					}
					else
					{
						fprintf(fptr,"B");
					}

					pieceDescrip = WhatIsAt(pieceColourType.PC_WHITE, _piece[armyNum][pieceNum]._ax + 1, _piece[armyNum][pieceNum]._ay + 1);

					fprintf(fptr,"%c", ConvertRankToSymbol(pieceDescrip._rank));


					fprintf(fptr,"%c%d\n", _piece[armyNum][pieceNum]._ax + 'a', _piece[armyNum][pieceNum]._ay + 1 ); 
				}
			}
		}

		fclose(fptr);
*/
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
		relPosType startRelPos = new relPosType();
		relPosType endRelPos = new relPosType();
		posType startPos = new posType();
		posType endPos = new posType();

		startRelPos._colour = move._colour;
		startRelPos._rx = move._startRX;
		startRelPos._ry = move._startRY;

		ConvertRelToAbsCoords(startRelPos, startPos);

		endRelPos._colour = move._colour;
		endRelPos._rx = move._endRX;
		endRelPos._ry = move._endRY;

		ConvertRelToAbsCoords(endRelPos, endPos);

		// Check start pos is on the board
		if(!IsAbsPositionOnBoard(startPos._x, startPos._y))
		{
			assert false;
		}

		// Check end pos is on the board
		if(!IsAbsPositionOnBoard(endPos._x, endPos._y))
		{
			assert false;
		}

		// Check start pos matches where the piece currently is
		if(startPos._x != _piece[move._colour.getValue()][move._number]._ax)
		{
			assert false;	
		}

		if(startPos._y != _piece[move._colour.getValue()][move._number]._ay)
		{
			assert false;	
		}

		// Check if anything is at the end point
		// If it is a friendly piece then the move is illegal
		if(_position[endPos._x][endPos._y]._colour == move._colour)
		{
			assert false;
		}

		// If it is an enemy then remove it
		if(_position[endPos._x][endPos._y]._colour != Colour.NONE)
		{
			RemovePiece(_position[endPos._x][endPos._y]._colour, _position[endPos._x][endPos._y]._number, endPos._x, endPos._y);
		}

		// Handle en passant captures
		if( move._bEnPassantCapture )
		{
			relPosType epCaptureRelPos = new relPosType();
			posType epCapturePos = new posType();

			epCaptureRelPos._colour = move._colour;
			epCaptureRelPos._rx = move._endRX;
			epCaptureRelPos._ry = move._endRY - 1;

			ConvertRelToAbsCoords(epCaptureRelPos, epCapturePos);

			RemovePiece(_position[epCapturePos._x][epCapturePos._y]._colour, _position[epCapturePos._x][epCapturePos._y]._number, epCapturePos._x, epCapturePos._y);
		}

		// Move piece to destination
		Colour colour;
		int number;
		PieceRank rank;

		colour = _position[startPos._x][startPos._y]._colour;
		number = _position[startPos._x][startPos._y]._number;
		rank = _position[startPos._x][startPos._y]._rank;

		_position[endPos._x][endPos._y]._colour = colour;
		_position[endPos._x][endPos._y]._number = number;

		if(move._promoteTo != PieceRank.NONE)
		{
			_position[endPos._x][endPos._y]._rank = move._promoteTo;
		}
		else
		{
			_position[endPos._x][endPos._y]._rank = _position[startPos._x][startPos._y]._rank;
		}

		_position[startPos._x][startPos._y]._colour = Colour.NONE;
		_position[startPos._x][startPos._y]._rank = PieceRank.NONE;
		_position[startPos._x][startPos._y]._number = PieceNumber.NONE;

		int colourVal = move._colour.getValue();

		_piece[colourVal][move._number]._ax = endPos._x;
		_piece[colourVal][move._number]._ay = endPos._y;
		_piece[colourVal][move._number]._bHasMovedThisGame = true;
	}

	private	void RemovePiece(Colour colour, int number, int ax, int ay)
	{
		// Check position is on the board
		if(!IsAbsPositionOnBoard(ax, ay))
		{
			assert false;
		}

		// Check the piece we think is there is actually there
		if(colour != _position[ax][ay]._colour)
		{
			assert false;
		}

		if(number != _position[ax][ay]._number)
		{
			assert false;
		}

		int colourVal = colour.getValue();

		if(_piece[colourVal][number]._ax != ax)
		{
			assert false;
		}

		if(_piece[colourVal][number]._ay != ay)
		{
			assert false;
		}

		// Check that the piece is alive
		if(_piece[colourVal][number]._bIsAlive == false)
		{
			assert false;
		}

		// Remove the piece
		_position[ax][ay]._colour = Colour.NONE;
		_position[ax][ay]._rank = PieceRank.NONE;

		_piece[colourVal][number]._bIsAlive = false;
		_piece[colourVal][number]._ax = -1;
		_piece[colourVal][number]._ay = -1;
	}

	public Board copy()
	{
		Board copy = new Board();

		// Copy white and black pieces
		for (int i = 0; i < Constants.PIECES_PER_SIDE; i++)
		{
			if (_whitePieces[i] != null)
				copy._whitePieces[i] = _whitePieces[i].copy();

			if (_blackPieces[i] != null)
				copy._blackPieces[i] = _blackPieces[i].copy();
		}

		// Copy board positions
		for (int x = 0; x < Board.MAX_X; x++)
		{
			for (int y = 0; y < Board.MAX_Y; y++)
			{
				if (_position[x][y] != null)
					copy._position[x][y] =
							new pieceDescriptionType(_position[x][y]);
			}
		}

		// Copy piece positions/status
		for (int side = 0; side < Board.MAX_ARMIES; side++)
		{
			for (int piece = 0; piece < Constants.PIECES_PER_SIDE; piece++)
			{
				if (_piece[side][piece] != null)
					copy._piece[side][piece] =
							new posDescriptionType(_piece[side][piece]);
			}
		}

		return copy;
	}

	public boolean IsKingInCheck(Colour colour)
	{
		// Get the current position of the king
		relPosType relPos = new relPosType();

		WhereIs(colour, PieceNumber.KING, relPos);

		return CanCaptureAt(colour.opposite(), colour, relPos._rx, relPos._ry);
	}

	// Can the playerColour army capture a piece at the relative co-ordinate (rColour, rx, ry)?
	public boolean CanCaptureAt(Colour playerColour, Colour rColour, int rx, int ry)
	{
		int i;

		for(i=0; i<Constants.PIECES_PER_SIDE; i++)
		{
			if(IsPieceAlive(playerColour, i))
			{
				if(playerColour == Colour.WHITE)
				{
					if( _whitePieces[i].CanPieceCaptureAt(this, rColour, rx, ry) )
					{
						return true;
					}
				}
				else if(playerColour == Colour.BLACK)
				{
					if( _blackPieces[i].CanPieceCaptureAt(this, rColour, rx, ry) )
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	public long MaterialValue(Colour colour)
	{
		int i;
		long materialValue = 0;

		for(i=0; i<PieceNumber.MAX_PIECES; i++)
		{
			if(IsPieceAlive(colour, i))
			{
				if(colour == Colour.WHITE)
				{
					materialValue += _whitePieces[i].MaterialValue();
				}
				else if(colour == Colour.BLACK)
				{
					materialValue += _blackPieces[i].MaterialValue();
				}
			}
		}

		return materialValue;
	}

	// This routine assume that the move is a valid move for the piece,
	// It determines whether the move would put the king in check
	public boolean WillMovePutKingInCheck(Move move, Colour colour)
	{
		Board testBoard = this.copy();

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
	public void GetMoves(MoveStack moves, Colour colour)
	{
		int idxPiece;

		// Empty the movestack
		moves.Clear();

		for(idxPiece=0; idxPiece<Constants.PIECES_PER_SIDE; idxPiece++)
		{
			if(IsPieceAlive(colour, idxPiece))
			{
				if(colour == Colour.WHITE)
				{
					_whitePieces[idxPiece].GetMoves(this, moves, false);
				}
				else if (colour == Colour.BLACK)
				{
					_blackPieces[idxPiece].GetMoves(this, moves, false);
				}
			}
		}
	}

	// Returns true if the army has one or more legal moves
	// Returns false if the army is checkmated or stalemated
	public boolean HasAnyLegalMoves(Colour colour)
	{
		int idxPiece;
		MoveStack possibleMoves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

		// Loop through all pieces on my side
		for(idxPiece=0; idxPiece<Constants.PIECES_PER_SIDE; idxPiece++)
		{
			int idxMove;

			possibleMoves.Clear();

			if(IsPieceAlive(colour, idxPiece))
			{
				if(colour == Colour.WHITE)
				{
					_whitePieces[idxPiece].GetMoves(this, possibleMoves, false);
				}
				else if (colour == Colour.BLACK)
				{
					_blackPieces[idxPiece].GetMoves(this, possibleMoves, false);
				}

				// Loop through all possible moves for the piece
				for(idxMove=0; idxMove<possibleMoves.getNumMoves(); idxMove++)
				{
					// Is the move legal?
					if( !WillMovePutKingInCheck(possibleMoves.getMove(idxMove), colour) )
					{
						return true;
					}
				}
			}
		}

		return false;
	}

/*
	public posDescriptionType getPosDescription(int colour, int pieceNum)
	{
		return _piece[colour][pieceNum];
	}
*/
}

