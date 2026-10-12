package engine.pieces;

import engine.board.*;
import engine.moves.Move;
import engine.moves.MoveStack;

public class Pawn extends Piece
{
	public Pawn(Colour colour, Square position)
	{ 
		super(colour, position);
		_rank = PieceRank.PAWN;
		_materialValue = 1;
	}

	private Pawn(Pawn other)
	{
		super(other);
	}

	@Override
	public Piece copy()
	{
		return new Pawn(this);
	}

	// NOTE : This routine should NOT modify the board
	@Override
	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		int startNumMoves = possibleMoves.getNumMoves();

		// Get the current position of the pawn
		int pawnRX = 0, pawnRY = 0;
		posType testRelPos = new posType();
		relPosType relPos = new relPosType();

		Board.ConvertAbsToRelCoords(_colour, _position.getX(), _position.getY(), relPos);

		pawnRX = relPos._rx;
		pawnRY = relPos._ry;

		// Check the pawn can move forward one square
		testRelPos._x = pawnRX;
		testRelPos._y = pawnRY + 1;

		Piece piece;

		if( Board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
		{
			piece = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

			// If the position is empty then we can move there
			if( piece == null)
			{
				AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, false, null);

				// Check if the pawn can move forward two squares
				// It can only do this if it is on the second row (starting row), and it could
				// have moved one square
				if(pawnRY == 2)
				{
					testRelPos._y++;

					if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
					{
						piece = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

						// If the position is empty then we can move there
						if( piece == null)
						{
							AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, false, null);
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
			piece = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

			if(piece != null)
			{
				// If the position is occupied by an enemy piece then we can move there
				if (piece._colour == _colour.opposite())
				{
					AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, true, null);
				}
			}
		}

		// Check the pawn can capture right
		testRelPos._x = pawnRX + 1;
		testRelPos._y = pawnRY + 1;

		if( board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
		{
			piece = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

			if(piece != null)
			{
				// If the position is occupied by an enemy piece then we can move there
				if( piece._colour == _colour.opposite() )
				{
					AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y, true, null);
				}
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
				piece = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

				if( piece != null && piece._rank == PieceRank.PAWN )
				{
					if(board.HasPawnMovedTwoStepsLastTurn(piece._colour, 0))
					{
						AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y + 1, true, null);
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
				piece = board.WhatIsAt(_colour, testRelPos._x, testRelPos._y);

				if(piece != null && piece._rank == PieceRank.PAWN )
				{
					if(board.HasPawnMovedTwoStepsLastTurn(piece._colour, 0))
					{
						AddMove(possibleMoves, pawnRX, pawnRY, testRelPos._x, testRelPos._y + 1, true, null);
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
				currMove._promoteTo = PieceRank.QUEEN;

				// Add promotion to Knight move
				AddMove(possibleMoves, 
						currMove._startRX, 
						currMove._startRY, 
						currMove._endRX, 
						currMove._endRY, 
						currMove._bIsCapture, 
						PieceRank.KNIGHT);

				// Add promotion to Bishop move
				AddMove(possibleMoves, 
						currMove._startRX, 
						currMove._startRY, 
						currMove._endRX, 
						currMove._endRY, 
						currMove._bIsCapture, 
						PieceRank.BISHOP);

				// Add promotion to Rook move
				AddMove(possibleMoves, 
						currMove._startRX, 
						currMove._startRY, 
						currMove._endRX, 
						currMove._endRY, 
						currMove._bIsCapture, 
						PieceRank.ROOK);
			}
		}
	}

/*
	@Override
	// NOTE : This routine should NOT modify the board
	// !!! update pawn rank when it is promoted rather than having to check board?
	public PieceRank Rank(Board board)
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
		return PieceRank.PAWN;
	}
*/
}












