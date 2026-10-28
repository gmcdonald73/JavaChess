package engine.pieces;

import engine.board.Board;
import engine.board.Square;
import engine.board.relPosType;
import engine.moves.Move;

import java.util.List;

public class Knight extends Piece
{

	public Knight(Colour colour, Square position)
	{ 
		super(colour, position);
		_rank = PieceRank.KNIGHT;
		_materialValue = 3;
	}

	private Knight(Knight other)
	{
		super(other);
	}

	@Override
	public Piece copy()
	{
		return new Knight(this);
	}

	@Override
	public void GetMoves(Board board, List<Move> possibleMoves, boolean bNoCastling)
	{
		// Get the current position of the knight
		int knightRX = 0, knightRY = 0, i = 0;
		relPosType relPos = new relPosType();

		Board.ConvertAbsToRelCoords(_colour, _position.getX(), _position.getY(), relPos);

		knightRX = relPos._rx;
		knightRY = relPos._ry;

		relPosType[] _testMove = new relPosType[8];

		for(i=0; i<8; i++)
		{
			_testMove[i] = new relPosType();
		}

		// Check two points to the north
		_testMove[0]._rx = knightRX - 1;
		_testMove[0]._ry = knightRY + 2;

		_testMove[1]._rx = knightRX + 1;
		_testMove[1]._ry = knightRY + 2;

		// Check two points to the south
		_testMove[2]._rx = knightRX - 1;
		_testMove[2]._ry = knightRY - 2;

		_testMove[3]._rx = knightRX + 1;
		_testMove[3]._ry = knightRY - 2;

		// Check two points to the west
		_testMove[4]._rx = knightRX - 2;
		_testMove[4]._ry = knightRY + 1;

		_testMove[5]._rx = knightRX - 2;
		_testMove[5]._ry = knightRY - 1;

		// Check two points to the east
		_testMove[6]._rx = knightRX + 2;
		_testMove[6]._ry = knightRY - 1;

		_testMove[7]._rx = knightRX + 2;
		_testMove[7]._ry = knightRY + 1;

		Piece piece;

		for(i=0; i<8; i++)
		{

			// Check end point is on board
			if( !Board.IsRelPositionOnBoard(_colour, _testMove[i]._rx, _testMove[i]._ry) )
			{
				continue;
			}

			// Check endpoint doesn't have a friendly piece there
			piece = board.WhatIsAtRelPos(_colour, _testMove[i]._rx, _testMove[i]._ry);

			if(piece != null)
			{
				if(piece._colour == _colour)
				{
					continue;
				}
			}

			AddMove(possibleMoves, knightRX, knightRY, _testMove[i]._rx, _testMove[i]._ry, (piece != null), null);
		}
	}
}


