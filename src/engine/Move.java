package engine;

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

	// !!! Need to store absolute co-ords


	// Move the stuff below out of this class?

	// These are mainly for bot calculations
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
		_number = PieceNumber.NONE;
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


	public void Print(int moveNum)
	{
		String sMove;

		sMove = Format(moveNum);
		System.out.println(sMove);
	}

	public String StartPos() {
		return FormatRelPos(_colour, _startRX, _startRY);
	}

	public String EndPos() {
		return FormatRelPos(_colour, _endRX, _endRY);
	}

	public String FormatRelPos(Colour colour, int rx, int ry)
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

	public String Format(int moveNum)
	{
		String sMove = "";
		int absStartX = 0;
		int absStartY = 0;
		int absEndX = 0;
		int absEndY = 0;

		if(_colour == Colour.NONE)
		{
			sMove = "Null Move";
			return sMove;
		}

		sMove = moveNum + ": ";

		if(_colour == Colour.WHITE)
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
			if( _rank != PieceRank.PAWN )
			{
				sMove = sMove + _rank.getSymbol(); //Board.ConvertRankToSymbol(_rank);
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
			if( (_rank == PieceRank.PAWN) && (_endRY == 8) )
			{
				sMove = sMove + _promoteTo.getSymbol(); //Board.ConvertRankToSymbol(_promoteTo);
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

	public static MoveParseResult parse(Colour colour, String input)
	{
		int sourceAX = 0;
		int sourceAY = 0;
		int destAX = 0;
		int destAY = 0;

		if (input == null || input.isBlank())
			return MoveParseResult.failure("No move was entered");

		// Set default values
		Move move = new Move();
		move._colour = colour;

		Piece piece = null;

/*
		if(input.compareToIgnoreCase("quit") == 0)
		{
			move._bLegalMove = true;
			return true;
		}
*/
		// Check for castling moves
		if( (input.compareToIgnoreCase("O-O") == 0) || (input.compareToIgnoreCase("O-O-O") == 0))
		{
			move._rank = PieceRank.KING;

			move._startRY = 1;
			move._endRY = 1;

			if(colour == Colour.WHITE)
			{
				move._startRX = 5;

				if(input.compareToIgnoreCase("O-O") == 0)
				{
					move._endRX = 7;
					move._bCastleKingSide = true;
				}
				else
				{
					move._endRX = 3;
					move._bCastleQueenSide = true;
				}
			}
			else
			{
				move._startRX = 4;

				if(input.compareToIgnoreCase("O-O") == 0)
				{
					move._endRX = 2;
					move._bCastleKingSide = true;
				}
				else
				{
					move._endRX = 6;
					move._bCastleQueenSide = true;
				}
			}
		}
		else
		{
			int moveStringLen;

			moveStringLen = input.length();

			if( moveStringLen < 6)
			{
				return MoveParseResult.failure("Illegal move - move string is too short");
			}

			// Breakdown move into component characters
			move._rank = PieceRank.fromSymbol(input.charAt(0)); //Board.ConvertSymbolToRank(input.charAt(0));
			sourceAX = input.charAt(1) - 'a';
			sourceAY = input.charAt(2) - '1';

			if(!Board.IsAbsPositionOnBoard(sourceAX, sourceAY))
			{
				return MoveParseResult.failure("Illegal move - source position is not on the board");
			}

			if( input.charAt(3) == 'x' )
			{
				move._bIsCapture = true;
			}
			else
			{
				move._bIsCapture = false;
			}

			destAX = input.charAt(4) - 'a';
			destAY = input.charAt(5) - '1';

			if(!Board.IsAbsPositionOnBoard(destAX, destAY))
			{
				return MoveParseResult.failure("Illegal move - destination position is not on the board");
			}

			relPosType startPos = new relPosType();
			Board.ConvertAbsToRelCoords(colour, sourceAX, sourceAY, startPos);
			move._startRX = startPos._rx;
			move._startRY = startPos._ry;

			relPosType endPos = new relPosType();
			Board.ConvertAbsToRelCoords(colour, destAX, destAY, endPos);
			move._endRX = endPos._rx;
			move._endRY = endPos._ry;

			if( moveStringLen == 7 )
			{
				move._promoteTo = PieceRank.fromSymbol(input.charAt(6)); // Board.ConvertSymbolToRank(input.charAt(6));
			}
			else
			{
				move._promoteTo = PieceRank.NONE;
			}
		}
/*
		// Determine piece
		pieceDescriptionType pieceDescrip;
		pieceDescrip = board.WhatIsAt(colour, move._startRX, move._startRY);

		if (pieceDescrip._rank == pieceRankType.PR_NONE )
		{
			errorMessage = "Illegal move - there is no piece at " + sMove.charAt(1) + sMove.charAt(2);
			return false;
		}

		if (pieceDescrip._rank != move._rank )
		{
			errorMessage = "Illegal move - piece at " + sMove.charAt(1) + sMove.charAt(2) + " is not a " + sMove.charAt(0);
			return false;
		}

		if (pieceDescrip._colour != colour )
		{
			errorMessage = "Illegal move - piece at " + sMove.charAt(1) + sMove.charAt(2) + " is not yours";
			return false;
		}

		move._number = pieceDescrip._number;
*/
/*
        piece = _piece[pieceDescrip._number];

        if(piece == null)
        {
            // Couldn't find piece that move is refering to
            errorMessage = "Internal error - null piece pointer";
            return false;
        }

        int numMoves = 0;
        MoveStack moves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

        // Get the moves for the piece
        piece.GetMoves(board, moves, false);
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
                Board testBoard = new Board();
                testBoard.Copy(board);

                // Make the move
                testBoard.MakeMove(move);

                if( IsKingInCheck(testBoard) )
                {
                    errorMessage = "Illegal move - this move would place the king in check";
                }
                else
                {
                    move._bLegalMove = true;
                }

                return false;
            }
        }

        errorMessage = "Illegal move - this move is not valid for this piece";
*/
		return MoveParseResult.success(move);
	}
}


