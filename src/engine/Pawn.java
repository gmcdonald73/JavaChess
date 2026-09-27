package engine;

public class Pawn extends Piece
{
	public Pawn(Army pArmy, int colour, int number) 
	{ 
		super(pArmy, colour, number);
		_rank = pieceRankType.PR_PAWN;
		_materialValue = 1;
	}

	// NOTE : This routine should NOT modify the board
	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		int startNumMoves = possibleMoves.getNumMoves();

		if( !board.IsPieceAlive(_colour, _number) )
		{
			// return no moves if the piece is dead
			return;
		}

		// Get the current position of the pawn
		int pawnRX = 0, pawnRY = 0;
		posType testRelPos = new posType();
		relPosType relPos = new relPosType();

		board.WhereIs(_colour, _number, relPos);

		pawnRX = relPos._rx;
		pawnRY = relPos._ry;

		pieceDescriptionType pieceDescrip = new pieceDescriptionType();

		// Check if pawn has been promoted
		pieceDescrip = board.WhatIsAt(_colour, pawnRX, pawnRY);

		if(pieceDescrip._rank == pieceRankType.PR_KNIGHT)
		{
			Knight knight = new Knight(_pArmy, _colour, _number);
			knight.GetMoves(board, possibleMoves, false);
			return;
		}

		if(pieceDescrip._rank == pieceRankType.PR_BISHOP)
		{
			Bishop bishop = new Bishop(_pArmy, _colour, _number);
			bishop.GetMoves(board, possibleMoves, false);
			return;
		}

		if(pieceDescrip._rank == pieceRankType.PR_ROOK)
		{
			Rook rook = new Rook(_pArmy, _colour, _number);
			rook.GetMoves(board, possibleMoves, false);
			return;
		}

		if(pieceDescrip._rank == pieceRankType.PR_QUEEN)
		{
			Queen queen = new Queen(_pArmy, _colour, _number);
			queen.GetMoves(board, possibleMoves, false);
			return;
		}

		// Check the pawn can move forward one square
		testRelPos._x = pawnRX;
		testRelPos._y = pawnRY + 1;

		if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
		{
			pieceDescrip = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

			// If the position is empty then we can move there
			if( pieceDescrip._colour == pieceColourType.PC_NONE)
			{
				AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, false, pieceRankType.PR_NONE);

				// Check if the pawn can move forward two squares
				// It can only do this if it is on the second row (starting row), and it could
				// have moved one square
				if(pawnRY == 2)
				{
					testRelPos._y++;

					if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
					{
						pieceDescrip = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

						// If the position is empty then we can move there
						if( pieceDescrip._colour == pieceColourType.PC_NONE)
						{
							AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, false, pieceRankType.PR_NONE);
						}
					}
				}
			}
		}

		// Check for captures

		// Check the pawn can capture left
		testRelPos._x = pawnRX - 1;
		testRelPos._y = pawnRY + 1;

		if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
		{
			pieceDescrip = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

			// If the position is occupied by an enemy piece then we can move there
			if( pieceDescrip._colour == board.OppositeColour(_colour) )
			{
				AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, true, pieceRankType.PR_NONE);
			}
		}

		// Check the pawn can capture right
		testRelPos._x = pawnRX + 1;
		testRelPos._y = pawnRY + 1;

		if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
		{
			pieceDescrip = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

			// If the position is occupied by an enemy piece then we can move there
			if( pieceDescrip._colour == board.OppositeColour(_colour) )
			{
				AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, true, pieceRankType.PR_NONE);
			}
		}

		// Check for en passant captures

		// Theses are only applicable if the pawn is on the 5th row
		if( pawnRY == 5)
		{
			// Check left
			// If we are on the 5th row and there is a pawn to the left and that pawn moved two steps last turn 
			// then we can do an en passant capture
			testRelPos._x = pawnRX - 1;
			testRelPos._y = pawnRY;

			if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
			{
				pieceDescrip = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

				if( pieceDescrip._rank == pieceRankType.PR_PAWN )
				{
					if(board.HasPawnMovedTwoStepsLastTurn(pieceDescrip._colour, pieceDescrip._number))
					{
						AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y + 1, true, pieceRankType.PR_NONE);
						possibleMoves.getMove(possibleMoves.getNumMoves()-1)._bEnPassantCapture = true;
					}
				}
			}

			// Check right
			// If we are on the 5th row and there is a pawn to the right and that pawn moved two steps last turn 
			// then we can do an en passant capture
			testRelPos._x = pawnRX + 1;
			testRelPos._y = pawnRY;

			if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
			{
				pieceDescrip = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

				if( pieceDescrip._rank == pieceRankType.PR_PAWN )
				{
					if(board.HasPawnMovedTwoStepsLastTurn(pieceDescrip._colour, pieceDescrip._number))
					{
						AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y + 1, true, pieceRankType.PR_NONE);
						possibleMoves.getMove(possibleMoves.getNumMoves()-1)._bEnPassantCapture = true;
					}
				}
			}
		}

		// Check for promotions
		int i;
		int stopPoint;

		stopPoint = possibleMoves.getNumMoves();

		for(i=startNumMoves; i<stopPoint; i++)
		{
			if( possibleMoves.getMove(i)._endRY == 8 )
			{
				Move currMove;

				currMove = possibleMoves.getMove(i);

				// Promote pawn to queen
				currMove._promoteTo = pieceRankType.PR_QUEEN;

				// Add promotion to Knight move
				AddMove(possibleMoves, 
						currMove._startRX, 
						currMove._startRY, 
						currMove._endRX, 
						currMove._endRY, 
						currMove._bIsCapture, 
						pieceRankType.PR_KNIGHT);

				// Add promotion to Bishop move
				AddMove(possibleMoves, 
						currMove._startRX, 
						currMove._startRY, 
						currMove._endRX, 
						currMove._endRY, 
						currMove._bIsCapture, 
						pieceRankType.PR_BISHOP);

				// Add promotion to Rook move
				AddMove(possibleMoves, 
						currMove._startRX, 
						currMove._startRY, 
						currMove._endRX, 
						currMove._endRY, 
						currMove._bIsCapture, 
						pieceRankType.PR_ROOK);
			}
		}
	}


	// NOTE : This routine should NOT modify the board
	public int Rank(Board board)
	{
		// Get the current position of the pawn
		relPosType relPos = new relPosType();

		board.WhereIs(_colour, _number, relPos);

		if( board.IsRelPositionOnBoard(_colour, relPos._rx, relPos._ry) )
		{
			pieceDescriptionType pieceDescrip = new pieceDescriptionType();

			// Check if pawn has been promoted
			pieceDescrip = board.WhatIsAt(_colour, relPos._rx, relPos._ry);

			return pieceDescrip._rank;
		}

		// In case the pawn hasn't been placed onto the board yet
		return pieceRankType.PR_PAWN;
	}

	public void PromoteTo(Board board, int promoteTo)
	{
		board.PromotePawn(_colour, _number, promoteTo);
	}
}












