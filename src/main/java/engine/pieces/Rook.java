package engine.pieces;

import engine.board.*;
import engine.moves.Move;

import java.util.List;

public class Rook extends Piece
{

	public Rook(Colour colour, Square position)
	{ 
		super(colour, position);
		_rank = PieceRank.ROOK;
		_materialValue = 5;
	}

	private Rook(Rook other)
	{
		super(other);
	}

	@Override
	public Piece copy()
	{
		return new Rook(this);
	}

	@Override
	public void GetMoves(Board board, List<Move> possibleMoves, boolean bNoCastling)
	{
		// Get the current position of the rook
		int rookRX = 0, rookRY = 0, i = 0;
		posType[] increment = new posType[4];
		posType currRelPos = new posType();
		boolean bDone = false;
		relPosType relPos = new relPosType();

		Board.ConvertAbsToRelCoords(_colour, _position.getX(), _position.getY(), relPos);

		rookRX = relPos._rx;
		rookRY = relPos._ry;


		// Check N of rook
		increment[0] = new posType();
		increment[0]._x = 0;
		increment[0]._y = 1;

		// Check S of rook
		increment[1] = new posType();
		increment[1]._x = 0;
		increment[1]._y = -1;

		// Check W of rook
		increment[2] = new posType();
		increment[2]._x = -1;
		increment[2]._y = 0;

		// Check E of rook
		increment[3] = new posType();
		increment[3]._x = 1;
		increment[3]._y = 0;

		Piece piece;

		for(i=0; i<4; i++)
		{
			currRelPos._x = rookRX;
			currRelPos._y = rookRY;
			bDone = false;

			while(!bDone)
			{
				currRelPos._x += increment[i]._x;
				currRelPos._y += increment[i]._y;

				if( Board.IsRelPositionOnBoard(_colour, currRelPos._x, currRelPos._y) )
				{
					piece = board.WhatIsAtRelPos(_colour, currRelPos._x, currRelPos._y);

					if( piece != null && piece._colour == _colour )
					{
						bDone = true;
					}
					else 
					{
						AddMove(possibleMoves, 
								rookRX, 
								rookRY, 
								currRelPos._x, 
								currRelPos._y, 
								(piece != null),
								null);

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



