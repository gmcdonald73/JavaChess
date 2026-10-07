package engine.pieces;

import engine.board.Board;
import engine.board.pieceDescriptionType;
import engine.board.posType;
import engine.board.relPosType;
import engine.moves.MoveStack;

public class Queen extends Piece
{

	public Queen(Colour colour, int number)
	{ 
		super(colour, number);
		_rank = PieceRank.QUEEN;
		_materialValue = 9;
	}

	private Queen(Queen other)
	{
		super(other);
	}

	@Override
	public Piece copy()
	{
		return new Queen(this);
	}

	@Override
	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		if( !board.IsPieceAlive(_colour, _number) )
		{
			// return no moves if the piece is dead
			return;
		}

		// Get the current position of the queen
		int queenRX = 0, queenRY = 0, i = 0;
		posType[] increment = new posType[8];
		posType currRelPos = new posType();
		boolean bDone;

		relPosType relPos = new relPosType();

		board.WhereIs(_colour, _number, relPos);

		queenRX = relPos._rx;
		queenRY = relPos._ry;

		// Check NW of queen
		increment[0] = new posType();
		increment[0]._x = -1;
		increment[0]._y = 1;

		// Check N of queen
		increment[1] = new posType();
		increment[1]._x = 0;
		increment[1]._y = 1;

		// Check NE of queen
		increment[2] = new posType();
		increment[2]._x = 1;
		increment[2]._y = 1;

		// Check W of queen
		increment[3] = new posType();
		increment[3]._x = -1;
		increment[3]._y = 0;

		// Check E of queen
		increment[4] = new posType();
		increment[4]._x = 1;
		increment[4]._y = 0;

		// Check SW of queen
		increment[5] = new posType();
		increment[5]._x = -1;
		increment[5]._y = -1;

		// Check S of queen
		increment[6] = new posType();
		increment[6]._x = 0;
		increment[6]._y = -1;

		// Check SE of queen
		increment[7] = new posType();
		increment[7]._x = 1;
		increment[7]._y = -1;

		pieceDescriptionType pieceDescrip = new pieceDescriptionType();

		for(i=0; i<8; i++)
		{
			currRelPos._x = queenRX;
			currRelPos._y = queenRY;
			bDone = false;

			while(!bDone)
			{
				currRelPos._x += increment[i]._x;
				currRelPos._y += increment[i]._y;

				if( board.IsRelPositionOnBoard(_colour, currRelPos._x, currRelPos._y) )
				{
					pieceDescrip = board.WhatIsAt(_colour, currRelPos._x, currRelPos._y);

					if( pieceDescrip._colour == _colour )
					{
						bDone = true;
					}
					else 
					{
						AddMove(possibleMoves, 
								queenRX, 
								queenRY, 
								currRelPos._x, 
								currRelPos._y, 
								(pieceDescrip._colour != Colour.NONE),
								PieceRank.NONE);

						if ( pieceDescrip._colour != Colour.NONE )
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





