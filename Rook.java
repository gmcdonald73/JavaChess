public class Rook extends Piece
{

	public Rook(Army pArmy, int colour, int number) 
	{ 
		super(pArmy, colour, number); 
		_rank = pieceRankType.PR_ROOK; 
		_materialValue = 5;
	}

	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		if( !board.IsPieceAlive(_colour, _number) )
		{
			// return no moves if the piece is dead
			return;
		}

		// Get the current position of the rook
		int rookRX = 0, rookRY = 0, i = 0;
		posType[] increment = new posType[4];
		posType currRelPos = new posType();
		boolean bDone = false;
		relPosType relPos = new relPosType();

		board.WhereIs(_colour, _number, relPos);

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

		pieceDescriptionType pieceDescrip = new pieceDescriptionType();

		for(i=0; i<4; i++)
		{
			currRelPos._x = rookRX;
			currRelPos._y = rookRY;
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
								rookRX, 
								rookRY, 
								currRelPos._x, 
								currRelPos._y, 
								(pieceDescrip._colour != pieceColourType.PC_NONE), 
								pieceRankType.PR_NONE);

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



