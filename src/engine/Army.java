package engine;

// This does not (and should not) contain any state information. All state information is
// stored in Board

public class Army
{
	// !!! remove
	public Army()
	{
		int i;

		for(i=0; i<Constants.PIECES_PER_SIDE; i++)
		{
			_piece[i] = null;
		}
	}

	// !!! Move to board
	public void Initialise(int colour, Board board, Army pEnemy)
	{
		_colour = colour;
		_pEnemy = pEnemy;

		_piece[pieceNumberType.PN_PAWN_1] = new Pawn( colour, pieceNumberType.PN_PAWN_1);
		_piece[pieceNumberType.PN_PAWN_2] = new Pawn( colour, pieceNumberType.PN_PAWN_2);
		_piece[pieceNumberType.PN_PAWN_3] = new Pawn( colour, pieceNumberType.PN_PAWN_3);
		_piece[pieceNumberType.PN_PAWN_4] = new Pawn( colour, pieceNumberType.PN_PAWN_4);
		_piece[pieceNumberType.PN_PAWN_5] = new Pawn( colour, pieceNumberType.PN_PAWN_5);
		_piece[pieceNumberType.PN_PAWN_6] = new Pawn( colour, pieceNumberType.PN_PAWN_6);
		_piece[pieceNumberType.PN_PAWN_7] = new Pawn( colour, pieceNumberType.PN_PAWN_7);
		_piece[pieceNumberType.PN_PAWN_8] = new Pawn( colour, pieceNumberType.PN_PAWN_8);
		_piece[pieceNumberType.PN_ROOK_1] = new Rook( colour, pieceNumberType.PN_ROOK_1);
		_piece[pieceNumberType.PN_ROOK_2] = new Rook( colour, pieceNumberType.PN_ROOK_2);
		_piece[pieceNumberType.PN_KNIGHT_1] = new Knight( colour, pieceNumberType.PN_KNIGHT_1);
		_piece[pieceNumberType.PN_KNIGHT_2] = new Knight( colour, pieceNumberType.PN_KNIGHT_2);
		_piece[pieceNumberType.PN_BISHOP_1] = new Bishop( colour, pieceNumberType.PN_BISHOP_1);
		_piece[pieceNumberType.PN_BISHOP_2] = new Bishop( colour, pieceNumberType.PN_BISHOP_2);
		_piece[pieceNumberType.PN_QUEEN] = new Queen( colour, pieceNumberType.PN_QUEEN);
		_piece[pieceNumberType.PN_KING] = new King( colour, pieceNumberType.PN_KING);
	}

	// !! Move to board
	public void SetupForGame(Board board)
	{
		if (_colour == pieceColourType.PC_WHITE)
		{
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_1], 'a', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_2], 'b', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_3], 'c', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_4], 'd', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_5], 'e', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_6], 'f', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_7], 'g', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_8], 'h', 2);
			board.PlacePiece(_piece[pieceNumberType.PN_ROOK_1], 'a', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_ROOK_2], 'h', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_KNIGHT_1], 'b', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_KNIGHT_2], 'g', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_BISHOP_1], 'c', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_BISHOP_2], 'f', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_QUEEN], 'd', 1);
			board.PlacePiece(_piece[pieceNumberType.PN_KING], 'e', 1);
		}
		else
		{
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_1], 'h', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_2], 'g', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_3], 'f', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_4], 'e', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_5], 'd', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_6], 'c', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_7], 'b', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_PAWN_8], 'a', 7);
			board.PlacePiece(_piece[pieceNumberType.PN_ROOK_1], 'h', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_ROOK_2], 'a', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_KNIGHT_1], 'g', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_KNIGHT_2], 'b', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_BISHOP_1], 'f', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_BISHOP_2], 'c', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_QUEEN], 'd', 8);
			board.PlacePiece(_piece[pieceNumberType.PN_KING], 'e', 8);
		}
	}

	// !!! Move to BotPlayer
	// Do a breadth first search, look 2 moves ahead for win or lose, then 4 moves ahead etc.
	public boolean FindBestMove(int moveNum, Board board, int lookAheadMax, Move moveStack[])
	{
		// Sneaky stuff, take a copy so that the original board is not modified.
		// This effectively simulates pass by value for board.
		// !!! do we need to do this here, or just in RecursiveFindBestMove?
		Board testBoard = board.copy();

		int numMovesToLookAhead = 2; 

		while(numMovesToLookAhead <= lookAheadMax)
		{
			if( !RecursiveFindBestMove(moveNum, testBoard, 0, numMovesToLookAhead, moveStack) )
			{
				// no legal move
				return false;
			}

			// !!! Should also terminate here for obvious stalemate / other draw
			if( (moveStack[0]._score == Constants.MS_WIN) || (moveStack[0]._score == Constants.MS_LOSE) )
			{
				return true;
			}

			numMovesToLookAhead += 2;
		}

		return true;
	}

	// !!! Move to BotPlayer
	// board is passed by value as this is called recursively
	// when we recurse back out, the board is automatically restored
	public boolean RecursiveFindBestMove(int moveNum, Board board, int lookAheadLevel, int numMovesToLookAhead, Move moveStack[])
	{
		// Sneaky stuff, take a copy so that the original board is not modified.
		// This effectively simulates pass by value for board.
		Board testBoard = board.copy();

		int idxPiece;
		boolean bFoundLegalMove = false;
		Move[] tempMoveStack = new Move[Constants.MAX_MOVES_LOOKAHEAD];
		int msi;

		moveStack[lookAheadLevel]._score = Constants.MS_ILLEGAL;
		MoveStack possibleMoves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

		// Loop through all pieces on my side
		for(idxPiece=0; idxPiece<Constants.PIECES_PER_SIDE; idxPiece++)
		{
			int idxMove;

			possibleMoves.Clear();

			if( _piece[idxPiece] != null )
			{
				if(testBoard.IsPieceAlive(_colour, idxPiece))
				{
					_piece[idxPiece].GetMoves(testBoard, possibleMoves, false);

					// Loop through all possible moves for the piece
					for(idxMove=0; idxMove<possibleMoves.getNumMoves(); idxMove++)
					{
						// Copy the move stack to temp
						for(msi=0; msi<Constants.MAX_MOVES_LOOKAHEAD; msi++)
						{
							tempMoveStack[msi] = new Move();
							tempMoveStack[msi].Copy(moveStack[msi]);
						}

						tempMoveStack[lookAheadLevel].Copy(possibleMoves.getMove(idxMove));
						tempMoveStack[lookAheadLevel]._score = Constants.MS_UNDEFINED;
						 
						// Evaluate the move
						EvaluateMove(moveNum, tempMoveStack, testBoard, lookAheadLevel, numMovesToLookAhead);

/*
						if (lookAheadLevel == 0)
						{
							tempMoveStack[lookAheadLevel].Print(moveNum);
						}
*/

						if(tempMoveStack[lookAheadLevel]._score > moveStack[lookAheadLevel]._score)
						{
							moveStack[lookAheadLevel]._score = tempMoveStack[lookAheadLevel]._score;

							// Copy the temp move stack back
							for(msi=0; msi<Constants.MAX_MOVES_LOOKAHEAD; msi++)
							{
								moveStack[msi].Copy(tempMoveStack[msi]);
							}

							bFoundLegalMove = true;

							if(moveStack[lookAheadLevel]._score == Constants.MS_WIN)
							{
								// If the move will win the game for us, then stop looking for a better move because
								// there isn't any
								return bFoundLegalMove;
							}
						}
					}
				}
			}
		}

		return bFoundLegalMove;
	}

	// !!! Move to BotPlayer
	// When we recurse back out, the board is automatically restored
	public void EvaluateMove(int moveNum, Move moveStack[], Board board, int lookAheadLevel, int numMovesToLookAhead)
	{
		// Sneaky stuff, take a copy so that the original board is not modified.
		// This effectively simulates pass by value for board.
		Board testBoard = board.copy();

		// Make the move
		testBoard.MakeMove(moveStack[lookAheadLevel]);

		// Determine if my own king is in check (if so then reject this move)
		// !!! should MakeMove be checking if move is illegal?
		if( testBoard.IsKingInCheck(_colour) )
		{
			moveStack[lookAheadLevel]._score = Constants.MS_ILLEGAL;
			return;
		}

		// Otherwise move is OK 
		boolean bEnemyHasLegalMoves;

		// If we have reached the look ahead level then rate the situation
		if( lookAheadLevel >= (numMovesToLookAhead-1) )
		{
			// Check if the enemy is checkmated or stalemated
//			bEnemyHasLegalMoves = _pEnemy.HasAnyLegalMoves(testBoard);
			bEnemyHasLegalMoves = testBoard.HasAnyLegalMoves(1 - _colour);

			if( bEnemyHasLegalMoves )
			{
				//	evaluate the position on the board
				moveStack[lookAheadLevel]._score = RateSituation(testBoard, moveStack[lookAheadLevel]);
			}
		}
		else
		{
			//	FindBestMove for enemy
			bEnemyHasLegalMoves = _pEnemy.RecursiveFindBestMove(moveNum, testBoard, lookAheadLevel + 1, numMovesToLookAhead, moveStack);

			if( bEnemyHasLegalMoves )
			{
				// Found a legal move for the enemy, our score for our move is the inverse of the 
				// best move for the enemy after making our move.
				moveStack[lookAheadLevel]._score = Constants.MS_WIN - moveStack[lookAheadLevel + 1]._score;
			}
		}

		// See if we have won or stalemated, also set flags for displaying checks
		if( bEnemyHasLegalMoves )
		{
			if(  testBoard.IsKingInCheck(1 - _colour) )
			{
				moveStack[lookAheadLevel]._bIsCheck = true;				
			}
			else
			{
				moveStack[lookAheadLevel]._bIsCheck = false;				
			}
		}
		else
		{
			// Enemy has no legal moves
			if( testBoard.IsKingInCheck(1 - _colour) )
			{
				// Enemy is checkmated, we win
				moveStack[lookAheadLevel]._score = Constants.MS_WIN;
				moveStack[lookAheadLevel]._bIsCheck = true;				
				moveStack[lookAheadLevel]._bIsMate = true;				
			}
			else
			{
				// Enemy is stalemated, we draw
				moveStack[lookAheadLevel]._score = Constants.MS_DRAW;
				moveStack[lookAheadLevel]._bIsCheck = false;				
			}
		}
	}


	// !!! Move to BotPlayer
	// RateSituation - return a score between MS_LOSE and MS_WIN indicating how favourable to board
	// position & forces are for calling army.
	// This is where the really intelligence is. Play with this to make computer player smarter
	// This is called after you (this army) has made a legal move, thus if you get here you
	// haven't been checkmated.
	//
	// This currently used a very rough algorithm, which determines a score from yours and your enemies
	// material strength and mobility.
	// NOTE : This routine should NOT modify the board
	// board = current position of board
	// move = last move made to get the board into this state

	// NOTE: move is the last move made (eg if lookahead is even this is the move made by opposite side).
	public int RateSituation(Board board, Move move)
	{
		int rating;
		long score, enemyScore;
		

		score = RateArmy(board);
		enemyScore = _pEnemy.RateArmy(board);

		// Rate position
		rating = (int)((score * Constants.MS_WIN) / (score + enemyScore));

		return rating;
	}

	// !!! Move to BotPlayer
	public long RateArmy(Board board)
	{
		MoveStack moves = new MoveStack(Constants.PIECES_PER_SIDE * Constants.MAX_PIECE_POSSIBLE_MOVES);
		long score;
		int i;

		// Rate your forces
		board.GetMoves(moves, _colour);

		score = moves.getNumMoves();

/*
		// Give bonus points for knight or bishop moves
		if ((move._rank == pieceRankType.PR_KNIGHT) ||
			(move._rank == pieceRankType.PR_BISHOP))
		{
			score += 10;
		}
		for(i=0; i<moves.getNumMoves(); i++)
		{
			int rank;

			rank = moves.getMove(i)._rank;
			if ((rank == pieceRankType.PR_KNIGHT) ||
				(rank == pieceRankType.PR_BISHOP))
			{
				score += 1;
			}
		}
*/

		// Give bonus points for pawn structure
		// Points for pawns in center of board
		// Points for pawns defended by other pawns

		// Loop through pawns
		for(i=0; i<8; i++)
		{
			if( _piece[i] != null )
			{
				if(board.IsPieceAlive(_colour, i))
				{
					// Get position of pawn
					relPosType relPos = new relPosType();

					board.WhereIs(_colour, i, relPos);

					if( ((relPos._rx == 4) || (relPos._rx == 5)) &&
						(relPos._ry == 4)  )
					{
						score += 3;
					}

					// See if pawn is defend by another pawn

					// Check SW pos (relative to pawn)
					pieceDescriptionType pieceDescrip;

					if (relPos._rx > 1)
					{
						pieceDescrip = board.WhatIsAt(_colour, relPos._rx - 1, relPos._ry - 1);

						if ((pieceDescrip._colour == _colour) && 
							(pieceDescrip._rank == pieceRankType.PR_PAWN))
						{
							score += 1;
						}
					}

					if (relPos._rx < 8)
					{
						// Check SE pos (relative to pawn)
						pieceDescrip = board.WhatIsAt(_colour, relPos._rx + 1, relPos._ry - 1);

						if ((pieceDescrip._colour == _colour) && 
							(pieceDescrip._rank == pieceRankType.PR_PAWN))
						{
							score += 1;
						}
					}
				}
			}
		}

		score += board.MaterialValue(_colour) * 10;

		return score;
	}

		// !!! Move to Board
	// When we recurse back out, the board is automatically restored
	// Returns true if the army has one or more legal moves
	// Returns false if the army is checkmated or stalemated
/*
	public boolean HasAnyLegalMoves(Board board)
	{
		// Sneaky stuff, take a copy so that the original board is not modified.
		// This effectively simulates pass by value for board.
		// !!! do we need to do this here, or only in methods that actually modify the board?
		Board testBoard = board.copy();

		int idxPiece;
		boolean bFoundLegalMove = false;
		MoveStack possibleMoves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

		// Loop through all pieces on my side
		for(idxPiece=0; idxPiece<Constants.PIECES_PER_SIDE; idxPiece++)
		{
			int idxMove;

			possibleMoves.Clear();

			if( _piece[idxPiece] != null )
			{
				if(testBoard.IsPieceAlive(_colour, idxPiece))
				{
					_piece[idxPiece].GetMoves(testBoard, possibleMoves, false);

					// Loop through all possible moves for the piece
					for(idxMove=0; idxMove<possibleMoves.getNumMoves(); idxMove++)
					{
						// Is the move legal?
						if( ! testBoard.WillMovePutKingInCheck(possibleMoves.getMove(idxMove), _colour) )
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}
*/

	// !!! Move to Board
	// NOTE : This routine should NOT modify the board
/*
	public boolean IsKingInCheck(Board board)
	{
		// Get the current position of the king
		relPosType relPos = new relPosType();

		board.WhereIs(_colour, pieceNumberType.PN_KING, relPos);

		return _pEnemy.CanCaptureAt(board, _colour, relPos._rx, relPos._ry);
	}
*/


	// !!! Move to Board
	// This routine assume that the move is a valid move for the piece,
	// It determines whether the move would put the king in check
	//
/*
	public boolean WillMovePutKingInCheck(Board board, Move move)
	{
		// Sneaky stuff, take a copy so that the original board is not modified.
		// This effectively simulates pass by value for board.
		Board testBoard = board.copy();

		// Make the move
		testBoard.MakeMove(move);

		// Determine if my own king is in check (if so then reject this move)
		if( testBoard.IsKingInCheck(_colour) )
		{
			return true;
		}

		return false;
	}
*/
	// !!! Move to Board
	// NOTE : This routine should NOT modify the board
/*
	public boolean CanCaptureAt(Board board, int colour, int rx, int ry)
	{
		int i;

		for(i=0; i<Constants.PIECES_PER_SIDE; i++)
		{
			if( _piece[i] != null )
			{
				if( _piece[i].CanPieceCaptureAt(board, colour, rx, ry) )
				{
					return true;
				}
			}
		}

		return false;
	}
*/

	// !!! Move to Board
	// NOTE : This routine should NOT modify the board
	// !!! Modify this (or GetMoves on Piece) to exclude moves that would place king in check
	/*public void GetMoves(Board board, MoveStack moves, boolean bCapturesOnly)
	{
		int idxPiece;

		// Empty the movestack
		moves.Clear();

		for(idxPiece=0; idxPiece<Constants.PIECES_PER_SIDE; idxPiece++)
		{
			if( _piece[idxPiece] != null )
			{
				if(board.IsPieceAlive(_colour, idxPiece))
				{
					_piece[idxPiece].GetMoves(board, moves, false);
				}
			}
		}
	}*/

	// !!! Move to HumanPlayer
	// Convert a move string the user has entered into a Move
	// NOTE : This routine should NOT modify the board
	public boolean InterpretMove(Board board, String sMove, Piece piece, Move move)
	{
		int sourceAX = 0;
		int sourceAY = 0;
		int destAX = 0;
		int destAY = 0;

		// Set default values
		move._colour = _colour;
		move._number = pieceNumberType.PN_NONE;
		move._rank = pieceRankType.PR_NONE;
		move._startRX = -1; // These are relative for each army
		move._startRY = -1;
		move._endRX = -1; // These are relative for each army
		move._endRY = -1;
		move._bIsCapture = false; // Is the move a capture?
		move._promoteTo = pieceRankType.PR_NONE;
		move._bIsCheck = false;
		move._bIsMate = false;
		move._bCastleQueenSide = false;
		move._bCastleKingSide = false;
		move._bEnPassantCapture = false;
		move._score = Constants.MS_UNDEFINED;
		move._bLegalMove = false;

		piece = null;

		if(sMove.compareToIgnoreCase("quit") == 0)
		{
			move._bLegalMove = true;
			return true;
		}

/*
		// This is no longer applicable
		if (sMove.compareToIgnoreCase("help") == 0)
		{
			// Some instructions
			System.out.print("\n*** AsciiChess Help ***\n\n");
			System.out.print("\t<Move> = quit | help | O-O | O-O-O | <Piece><Col><Row><Separator><Col><Row>[<Piece>]\n");
			System.out.print("\t<Piece> = P|R|N|B|Q|K\n"); 
			System.out.print("\t<Col> = a|b|c|d|e|f|g|h\n");
			System.out.print("\t<Row> = 1|2|3|4|5|6|7|8\n");
			System.out.print("\t<Separator> = x|-\n\n");
			System.out.print("\tExamples\n");
			System.out.print("\tPe2-e4  : Moves white's king pawn from e2 to e4\n");
			System.out.print("\tQa4xg4  : Captures piece on g4 with Queen on a4\n");
			System.out.print("\tO-O     : Castles king side\n");
			System.out.print("\tO-O-O   : Castles queen side\n");
			System.out.print("\tPa7-a8Q : Promotes pawn on a7 to a queen\n");
			System.out.print("\thelp    : This help\n");
			System.out.print("\tquit    : Quits game \n\n");
			System.out.print("\tFor en passant capture, enter the square the opposing pawn moved over. eg 'Pa5xb6'\n");
			System.out.print("\n***********************\n");
			System.out.print("Press any key to continue\n\n");

			String input = ConsoleInput.readLine();
			return false;
		}
*/

		// Check for castling moves
		if( (sMove.compareToIgnoreCase("O-O") == 0) || (sMove.compareToIgnoreCase("O-O-O") == 0))
		{
			move._rank = pieceRankType.PR_KING;

			move._startRY = 1;
			move._endRY = 1;

			if(_colour == pieceColourType.PC_WHITE)
			{
				move._startRX = 5;

				if(sMove.compareToIgnoreCase("O-O") == 0)
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

				if(sMove.compareToIgnoreCase("O-O") == 0)
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

			moveStringLen = sMove.length();

			if( moveStringLen < 6)
			{
				_sErrMsg = "Illegal move - move string is too short";
				return false;
			}

			// Breakdown move into component characters
			move._rank = board.ConvertSymbolToRank(sMove.charAt(0));
			sourceAX = sMove.charAt(1) - 'a';
			sourceAY = sMove.charAt(2) - '1';

			if(!board.IsAbsPositionOnBoard(sourceAX, sourceAY))
			{
				_sErrMsg = "Illegal move - source position is not on the board";
				return false;
			}

			if( sMove.charAt(3) == 'x' )
			{
				move._bIsCapture = true;
			}
			else
			{
				move._bIsCapture = false;
			}

			destAX = sMove.charAt(4) - 'a';
			destAY = sMove.charAt(5) - '1';

			if(!board.IsAbsPositionOnBoard(destAX, destAY))
			{
				_sErrMsg = "Illegal move - destination position is not on the board";
				return false;
			}

			relPosType startPos = new relPosType();
			board.ConvertAbsToRelCoords(_colour, sourceAX, sourceAY, startPos);
			move._startRX = startPos._rx;
			move._startRY = startPos._ry;

			relPosType endPos = new relPosType();
			board.ConvertAbsToRelCoords(_colour, destAX, destAY, endPos);
			move._endRX = endPos._rx;
			move._endRY = endPos._ry;

			if( moveStringLen == 7 )
			{
				move._promoteTo = board.ConvertSymbolToRank(sMove.charAt(6));
			}
			else
			{
				move._promoteTo = pieceRankType.PR_NONE;
			}
		}

		// Determine piece
		pieceDescriptionType pieceDescrip;
		pieceDescrip = board.WhatIsAt(_colour, move._startRX, move._startRY);

		if (pieceDescrip._rank == pieceRankType.PR_NONE )
		{
			_sErrMsg = "Illegal move - there is no piece at " + sMove.charAt(1) + sMove.charAt(2);
			return false;
		}

		if (pieceDescrip._rank != move._rank )
		{
			_sErrMsg = "Illegal move - piece at " + sMove.charAt(1) + sMove.charAt(2) + " is not a " + sMove.charAt(0);
			return false;
		}

		if (pieceDescrip._colour != _colour )
		{
			_sErrMsg = "Illegal move - piece at " + sMove.charAt(1) + sMove.charAt(2) + " is not yours"; 
			return false;
		}

		move._number = pieceDescrip._number;

		piece = _piece[pieceDescrip._number];

		if(piece == null)
		{
			// Couldn't find piece that move is refering to 
			_sErrMsg = "Internal error - null piece pointer";
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

				// !!! Replace this will WillMovePutMyKingInCheck?
				// Copy the board to a test board
				// Make a copy so that the original board is not modified
				Board testBoard = board.copy();

				// Make the move
				testBoard.MakeMove(move);

				if( testBoard.IsKingInCheck(_colour) )
				{
					_sErrMsg = "Illegal move - this move would place the king in check";
				}
				else
				{
					move._bLegalMove = true;
				}

				return false;
			}
		}

		_sErrMsg = "Illegal move - this move is not valid for this piece";
		return false;
	}

	// !!! Move to ChessPlayer
	public int Colour() { return _colour; }

	// !!! Move to Board
/*
	public Piece Piece(int pieceNum)
	{
		if( (pieceNum < 0) || (pieceNum >= pieceNumberType.PN_MAX_PIECES))
		{
			assert false;
		}

		return _piece[pieceNum];
	}
*/

	// !!! remove
	Army Enemy() { return _pEnemy; }

	// !!! remove
	public String GetErrMsg() { return _sErrMsg; }

	// !!! remove these
	private Piece[] _piece = new Piece[Constants.PIECES_PER_SIDE];
	int _colour;
	Army _pEnemy;
	String _sErrMsg;
}

