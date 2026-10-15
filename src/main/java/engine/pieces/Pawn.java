package engine.pieces;

import engine.board.*;
import engine.moves.Move;

import java.util.List;

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
	public void GetMoves(Board board, List<Move> possibleMoves, boolean bNoCastling)
	{
		int startNumMoves = possibleMoves.size();

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

					if( Board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
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

		if( Board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
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

		if( Board.IsRelPositionOnBoard(_colour, testRelPos._x, testRelPos._y) )
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
		Square enPassantTarget = board.getEnPassantTarget();

		// Theses are only applicable if the pawn is on the 5th row
		if( enPassantTarget != null && pawnRY == 5)
		{
			int epAbsX = enPassantTarget.getX();
			int epAbsY = enPassantTarget.getY();
			relPosType epRelPos = new relPosType();

			Board.ConvertAbsToRelCoords(_colour, epAbsX, epAbsY, epRelPos);

			// Is ep target on square NW of pawn
			if((epRelPos._rx == pawnRX - 1) && (epRelPos._ry == pawnRY + 1))
			{
				AddMove(possibleMoves, pawnRX, pawnRY, epRelPos._rx, epRelPos._ry, true, null);
				possibleMoves.getLast()._bEnPassantCapture = true;
			}

			// Is ep target on square NE of pawn
			if((epRelPos._rx == pawnRX + 1) && (epRelPos._ry == pawnRY + 1))
			{
				AddMove(possibleMoves, pawnRX, pawnRY, epRelPos._rx, epRelPos._ry, true, null);
				possibleMoves.getLast()._bEnPassantCapture = true;
			}
		}

		// Check for promotions
		int i;
		int stopPoint;

		stopPoint = possibleMoves.size();

		for(i=startNumMoves; i<stopPoint; i++)
		{
			if( possibleMoves.get(i)._endRY == 8 )
			{
				Move currMove;

				currMove = possibleMoves.get(i);

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
}












