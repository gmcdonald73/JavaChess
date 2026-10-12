package engine.pieces;

import engine.board.*;
import engine.moves.Move;
import engine.moves.MoveStack;

public class King extends Piece
{
	public King(Colour colour, Square position)
	{ 
		super(colour, position);
		_rank = PieceRank.KING;
		_materialValue = 1; // so formulas work if only king left
	}

	private King(King other)
	{
		super(other);
	}

	@Override
	public Piece copy()
	{
		return new King(this);
	}

	// NOTE : This routine should NOT modify the board
	@Override
	public void GetMoves(Board board, MoveStack possibleMoves, boolean bCapturesOnly)
	{
		// Get the current position of the king
		int kingRX = 0, kingRY = 0, i = 0;
		relPosType relPos = new relPosType();

		Board.ConvertAbsToRelCoords(_colour, _position.getX(), _position.getY(), relPos);

		kingRX = relPos._rx;
		kingRY = relPos._ry;

		Move[] _testMove = new Move[8];

		for(i=0; i<8; i++)
		{
			_testMove[i] = new Move();
		}

		// check NW is on the board, has no friendly piece, if so add to list
		_testMove[0]._endRX = kingRX - 1;
		_testMove[0]._endRY = kingRY + 1;

		// check N is on the board, has no friendly piece, if so add to list
		_testMove[1]._endRX = kingRX;
		_testMove[1]._endRY = kingRY + 1;

		// check NE is on the board, has no friendly piece, if so add to list
		_testMove[2]._endRX = kingRX + 1;
		_testMove[2]._endRY = kingRY + 1;

		// check W is on the board, has no friendly piece, if so add to list
		_testMove[3]._endRX = kingRX - 1;
		_testMove[3]._endRY = kingRY;

		// check E is on the board, has no friendly piece, if so add to list
		_testMove[4]._endRX = kingRX + 1;
		_testMove[4]._endRY = kingRY;

		// check SW is on the board, has no friendly piece, if so add to list
		_testMove[5]._endRX = kingRX - 1;
		_testMove[5]._endRY = kingRY - 1;

		// check S is on the board, has no friendly piece, if so add to list
		_testMove[6]._endRX = kingRX;
		_testMove[6]._endRY = kingRY - 1;

		// check SE is on the board, has no friendly piece, if so add to list
		_testMove[7]._endRX = kingRX + 1;
		_testMove[7]._endRY = kingRY - 1;

		Piece piece;

		for(i=0; i<8; i++)
		{
			if( board.IsRelPositionOnBoard(_colour, _testMove[i]._endRX, _testMove[i]._endRY) )
			{
				piece = board.WhatIsAt(_colour, _testMove[i]._endRX, _testMove[i]._endRY);

				if( piece == null || piece._colour != _colour )
				{
					AddMove(possibleMoves, kingRX, kingRY, _testMove[i]._endRX, _testMove[i]._endRY, (piece != null), null);
				}
			}
		}

		if(bCapturesOnly)
		{
			// If only looking for captures then don't bother with castling moves
			// This is important - it prevents an infinite loop
			return;
		}

		// Check for castling moves

		// Has king moved?
		if( HasMovedThisGame())
		{
			// King can't castle if it has already moved
			return;
		}

		// Is king in check?
		if( board.IsKingInCheck(_colour) )
		{
			// King can't castle if it is check
			return;
		}

		// Check for castling to the left
		// (queenside for white, king side for black)
		if(CanKingCastle(board, true))
		{
			AddMove(possibleMoves, kingRX, kingRY, kingRX - 2, kingRY, false, null);

			if(_colour == Colour.WHITE)
			{
				possibleMoves.getMove(possibleMoves.getNumMoves()-1)._bCastleQueenSide = true;	
			}
			else
			{
				possibleMoves.getMove(possibleMoves.getNumMoves()-1)._bCastleKingSide = true;	
			}
		}

		// Check for castling to the right
		// king side for white, queen side for black
		if(CanKingCastle(board, false))
		{
			AddMove(possibleMoves, kingRX, kingRY, kingRX + 2, kingRY, false, null);

			if(_colour == Colour.WHITE)
			{
				possibleMoves.getMove(possibleMoves.getNumMoves()-1)._bCastleKingSide = true;	
			}
			else
			{
				possibleMoves.getMove(possibleMoves.getNumMoves()-1)._bCastleQueenSide = true;	
			}
		}
	}

	// Check if the king can castle
	// if bCastleLeft is true then check for castling to the left, otherwise check to the right
	// This method assumes that king is at starting position
	// NOTE : This routine should NOT modify the board
	protected boolean CanKingCastle(Board board, boolean bCastleLeft)
	{
		// Check for castling
		int rookStartX;
		int kingRX;
		Piece piece;

		if( _colour == Colour.WHITE )
		{
			kingRX = 5;
		}
		else
		{
			kingRX = 4;
		}

		if( bCastleLeft )
		{
			rookStartX = 1;
		}
		else
		{
			rookStartX = 8;
		}

		// See if there is a rook at the correct position
		piece = board.WhatIsAt(_colour, rookStartX, 1);

		if(piece == null) return false;

		if( !((piece._rank == PieceRank.ROOK) && (piece._colour == _colour)) )
		{
			return false;
		}

		// Has rook moved?
		if( piece.HasMovedThisGame())
		{
			return false;
		}

		int rx;
		int startRX;
		int endRX;

		if( bCastleLeft )
		{
			startRX = rookStartX + 1;
			endRX = kingRX - 1;
		}
		else
		{
			startRX = kingRX + 1;
			endRX = rookStartX - 1;
		}

		// Are the squares between the king and the rook clear?
		for(rx=startRX; rx<=endRX; rx++)
		{
			piece = board.WhatIsAt(_colour, rx, 1);

			if(piece != null)
			{
				return false;
			}
		}

		// Are the squares that the king will cross and move to under threat?
		if( bCastleLeft )
		{
			startRX = kingRX - 2;
			endRX = kingRX - 1;
		}
		else
		{
			startRX = kingRX + 1;
			endRX = kingRX + 2;
		}

		for(rx=startRX; rx<=endRX; rx++)
		{
			if( board.CanCaptureAt(_colour.opposite(), _colour, rx, 1) )
			{
				return false;
			}
		}

		return true;
	}
}




