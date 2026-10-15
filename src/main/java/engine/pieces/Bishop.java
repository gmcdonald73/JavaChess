package engine.pieces;

import engine.board.*;
import engine.moves.Move;

import java.util.List;

public class Bishop extends Piece
{

	public Bishop(Colour colour, Square position)
	{ 
		super(colour, position);
		_rank = PieceRank.BISHOP;
		_materialValue = 3;
	}

	private Bishop(Bishop other)
	{
		super(other);
	}

	@Override
	public Piece copy()
	{
		return new Bishop(this);
	}

	@Override
	public void GetMoves(Board board, List<Move> possibleMoves, boolean bNoCastling)
	{
		// Get the current position of the bishop
		int bishopRX = 0, bishopRY = 0, i = 0;
		posType[] increment = new posType[4];
		posType currRelPos = new posType();
		boolean bDone;
		relPosType relPos = new relPosType();

		Board.ConvertAbsToRelCoords(_colour, _position.getX(), _position.getY(), relPos);

		bishopRX = relPos._rx;
		bishopRY = relPos._ry;

		// Check NE of bishop
		increment[0] = new posType();
		increment[0]._x = 1;
		increment[0]._y = 1;

		// Check SE of bishop
		increment[1] = new posType();
		increment[1]._x = 1;
		increment[1]._y = -1;

		// Check NW of bishop
		increment[2] = new posType();
		increment[2]._x = -1;
		increment[2]._y = 1;

		// Check SW of bishop
		increment[3] = new posType();
		increment[3]._x = -1;
		increment[3]._y = -1;

		Piece piece;

		for(i=0; i<4; i++)
		{
			currRelPos._x = bishopRX;
			currRelPos._y = bishopRY;
			bDone = false;

			while(!bDone)
			{
				currRelPos._x += increment[i]._x;
				currRelPos._y += increment[i]._y;

				if( Board.IsRelPositionOnBoard(_colour, currRelPos._x, currRelPos._y) )
				{
					piece = board.WhatIsAt(_colour, currRelPos._x, currRelPos._y);

					if( piece != null && piece._colour == _colour )
					{
						bDone = true;
					}
					else 
					{
						AddMove(possibleMoves, bishopRX, bishopRY, currRelPos._x, currRelPos._y, (piece != null), null);

						if ( piece != null )
						{
							bDone = true;
						}
					}
				}
				else
				{
					bDone = true;
				}
			}
		}
	}
}




