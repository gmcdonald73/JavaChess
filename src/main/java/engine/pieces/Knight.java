package engine.pieces;

import engine.board.Board;
import engine.board.Square;
import engine.board.relPosType;
import engine.moves.Move;
import engine.moves.MoveStack;

public class Knight extends Piece
{

	public Knight(Colour colour, int number, Square position)
	{ 
		super(colour, number, position);
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
	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		if( !board.IsPieceAlive(_colour, _number) )
		{
			// return no moves if the piece is dead
			return;
		}

		// Get the current position of the knight
		int knightRX = 0, knightRY = 0, i = 0;
		relPosType relPos = new relPosType();

		Board.ConvertAbsToRelCoords(_colour, _position.getX(), _position.getY(), relPos);

		knightRX = relPos._rx;
		knightRY = relPos._ry;

		Move[] _testMove = new Move[8];

		for(i=0; i<8; i++)
		{
			_testMove[i] = new Move();
		}

		// Check two points to the north
		_testMove[0]._startRX = knightRX;
		_testMove[0]._startRY = knightRY;
		_testMove[0]._endRX = knightRX - 1;
		_testMove[0]._endRY = knightRY + 2;

		_testMove[1]._startRX = knightRX;
		_testMove[1]._startRY = knightRY;
		_testMove[1]._endRX = knightRX + 1;
		_testMove[1]._endRY = knightRY + 2;

		// Check two points to the south
		_testMove[2]._startRX = knightRX;
		_testMove[2]._startRY = knightRY;
		_testMove[2]._endRX = knightRX - 1;
		_testMove[2]._endRY = knightRY - 2;

		_testMove[3]._startRX = knightRX;
		_testMove[3]._startRY = knightRY;
		_testMove[3]._endRX = knightRX + 1;
		_testMove[3]._endRY = knightRY - 2;

		// Check two points to the west
		_testMove[4]._startRX = knightRX;
		_testMove[4]._startRY = knightRY;
		_testMove[4]._endRX = knightRX - 2;
		_testMove[4]._endRY = knightRY + 1;

		_testMove[5]._startRX = knightRX;
		_testMove[5]._startRY = knightRY;
		_testMove[5]._endRX = knightRX - 2;
		_testMove[5]._endRY = knightRY - 1;

		// Check two points to the east
		_testMove[6]._startRX = knightRX;
		_testMove[6]._startRY = knightRY;
		_testMove[6]._endRX = knightRX + 2;
		_testMove[6]._endRY = knightRY - 1;

		_testMove[7]._startRX = knightRX;
		_testMove[7]._startRY = knightRY;
		_testMove[7]._endRX = knightRX + 2;
		_testMove[7]._endRY = knightRY + 1;

		Piece piece;

		for(i=0; i<8; i++)
		{

			// Check end point is on board
			if( !board.IsRelPositionOnBoard(_colour, _testMove[i]._endRX, _testMove[i]._endRY) )
			{
				continue;
			}

			// Check endpoint doesn't have a friendly piece there
			piece = board.WhatIsAt(_colour, _testMove[i]._endRX, _testMove[i]._endRY);

			if(piece != null)
			{
				if(piece._colour == _colour)
				{
					continue;
				}
			}

			AddMove(possibleMoves, knightRX, knightRY, _testMove[i]._endRX, _testMove[i]._endRY, (piece != null), PieceRank.NONE);
		}
	}
}


