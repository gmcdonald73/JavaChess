
public class Bishop extends Piece
{

	public Bishop(Army pArmy, int colour, int number) 
	{ 
		super(pArmy, colour, number);
		_rank = pieceRankType.PR_BISHOP; 
		_materialValue = 3;
	}

	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		if( !board.IsPieceAlive(_colour, _number) )
		{
			// return no moves if the piece is dead
			return;
		}

		// Get the current position of the bishop
		int bishopRX = 0, bishopRY = 0, i = 0;
		posType[] increment = new posType[4];
		posType currRelPos = new posType();
		boolean bDone;
		relPosType relPos = new relPosType();

		board.WhereIs(_colour, _number, relPos);

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

		pieceDescriptionType pieceDescrip;

		for(i=0; i<4; i++)
		{
			currRelPos._x = bishopRX;
			currRelPos._y = bishopRY;
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
						AddMove(possibleMoves, bishopRX, bishopRY, currRelPos._x, currRelPos._y, (pieceDescrip._colour != pieceColourType.PC_NONE), pieceRankType.PR_NONE);

						if ( pieceDescrip._colour != pieceColourType.PC_NONE )
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




