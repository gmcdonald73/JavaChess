import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class pieceDescriptionType
{
	int _rank;
	int _colour;
	int _number;
}

class posDescriptionType
{
	boolean _bIsAlive;
	int _ax;
	int _ay;
	boolean _bHasMovedThisGame;
	boolean _bPawnMovedTwoStepsLastTurn;
}

class Board // implements ActionListener
{

	public static final int MAX_X = 8;
	public static final int MAX_Y = 8;
	public static final int MAX_ARMIES = 2;

//	JButton[][] _cells = new JButton[8][8];
	boolean _bIsSelected;
//	JButton _sourceBtn;
//	Color _sourceBackColour;

//	ImageIcon _whiteIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];
//	ImageIcon _blackIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];

// public methods
	public Board()
	{
		int x, y;

//		GridLayout lm = new GridLayout(9, 9, 0, 0);
//		_boardPane = new JPanel();
//		_boardPane.setLayout(lm);

		_bIsSelected = false;

		for(x=0; x<MAX_X; x++)
		{
			for(y=0; y<MAX_Y; y++)
			{
				_position[x][y] = new pieceDescriptionType();
			}
		}

		int armyNum, pieceNum;

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<JavaChess.PIECES_PER_SIDE; pieceNum++)
			{
				_piece[armyNum][pieceNum] = new posDescriptionType();
			}
		}

		EmptyBoard();

/*
		for(y = 8; y>=0; y--)
		{
			for(x = 0; x<9; x++)
			{	
				if ((x>0) && (y>0))
				{
					JButton btn = new JButton();

					btn.addActionListener(this);

					char c = (char)('a' + (x-1));

					btn.setActionCommand(c + "" + y); 

					if(((x+y) % 2) == 0)
					{
						btn.setBackground(Color.black);
					}
					else
					{
						btn.setBackground(Color.white);
					}

					_cells[x-1][y-1] = btn;
					_boardPane.add(btn);
				}
				else
				{
					if ((x > 0) || (y > 0))
					{
						if (x == 0)
						{
							_boardPane.add(new JLabel(y + "", SwingConstants.CENTER));
						}
						else
						{
							// y == 0
							char c = (char)('a' + (x-1));

							_boardPane.add(new JLabel(c + "", SwingConstants.CENTER));
						}
					}
					else
					{
						_boardPane.add(new JLabel(""));
					}
				}
			}
		}

		_whiteIcons[pieceRankType.PR_PAWN] = new ImageIcon("3Pawn.gif");
		_whiteIcons[pieceRankType.PR_ROOK] = new ImageIcon("3Rook.gif");
		_whiteIcons[pieceRankType.PR_KNIGHT] = new ImageIcon("3Knight.gif");
		_whiteIcons[pieceRankType.PR_BISHOP] = new ImageIcon("3Bishop.gif");
		_whiteIcons[pieceRankType.PR_QUEEN] = new ImageIcon("3Queen.gif");
		_whiteIcons[pieceRankType.PR_KING] = new ImageIcon("3King.gif");

		_blackIcons[pieceRankType.PR_PAWN] = new ImageIcon("4Pawn.gif");
		_blackIcons[pieceRankType.PR_ROOK] = new ImageIcon("4Rook.gif");
		_blackIcons[pieceRankType.PR_KNIGHT] = new ImageIcon("4Knight.gif");
		_blackIcons[pieceRankType.PR_BISHOP] = new ImageIcon("4Bishop.gif");
		_blackIcons[pieceRankType.PR_QUEEN] = new ImageIcon("4Queen.gif");
		_blackIcons[pieceRankType.PR_KING] = new ImageIcon("4King.gif");

		cells[0][0].setIcon(whiteRook);
		cells[1][0].setIcon(whiteKnight);
		cells[2][0].setIcon(whiteBishop);
		cells[3][0].setIcon(whiteQueen);
		cells[4][0].setIcon(whiteKing);
		cells[5][0].setIcon(whiteBishop);
		cells[6][0].setIcon(whiteKnight);
		cells[7][0].setIcon(whiteRook);

		cells[0][7].setIcon(blackRook);
		cells[1][7].setIcon(blackKnight);
		cells[2][7].setIcon(blackBishop);
		cells[3][7].setIcon(blackQueen);
		cells[4][7].setIcon(blackKing);
		cells[5][7].setIcon(blackBishop);
		cells[6][7].setIcon(blackKnight);
		cells[7][7].setIcon(blackRook);
*/
	}

	public void EmptyBoard()
	{
		int x, y;

		for(x=0; x<MAX_X; x++)
		{
			for(y=0; y<MAX_Y; y++)
			{
				_position[x][y]._colour = pieceColourType.PC_NONE;
				_position[x][y]._rank = pieceRankType.PR_NONE;
				_position[x][y]._number = pieceNumberType.PN_NONE;
			}
		}

		int armyNum, pieceNum;

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<JavaChess.PIECES_PER_SIDE; pieceNum++)
			{
				_piece[armyNum][pieceNum]._bIsAlive = false;
				_piece[armyNum][pieceNum]._ax = -1;
				_piece[armyNum][pieceNum]._ay = -1;
				_piece[armyNum][pieceNum]._bHasMovedThisGame = false; 
				_piece[armyNum][pieceNum]._bPawnMovedTwoStepsLastTurn = false;
			}
		}
	}

	public void PlacePiece(Piece piece, char x, int y)
	{
		int colour;
		int rank;
		int number;

		colour = piece.Colour();
		rank = piece.Rank(this);
		number = piece.Number();

		int ax, ay;

		ax = x - 'a';
		ay = y - 1;

		if( IsAbsPositionOnBoard(ax, ay) )
		{
			// Make sure that there is nothing already there
			if (_position[ax][ay]._colour != pieceColourType.PC_NONE )
			{
				assert false;
			}

			_position[ax][ay]._colour = colour;
			_position[ax][ay]._number = number;
			_position[ax][ay]._rank = rank;

			_piece[colour][number]._bIsAlive = true;
			_piece[colour][number]._ax = ax;
			_piece[colour][number]._ay = ay;
			_piece[colour][number]._bPawnMovedTwoStepsLastTurn = false;

			// Set has piece moved flag
			// This only really matters for the rooks and the king to check if castling is allowed,
			// but might as well do it correctly for all pieces
			_piece[colour][number]._bHasMovedThisGame = true; 

			int rx = 0, ry = 0;
			relPosType relPos = new relPosType();

			ConvertAbsToRelCoords(colour, ax, ay, relPos);
			rx = relPos._rx;
			ry = relPos._ry;

			if( rank == pieceRankType.PR_PAWN)
			{
				if( ry == 2)
				{
					_piece[colour][number]._bHasMovedThisGame = false; 
				}
			}
			else if( rank == pieceRankType.PR_ROOK)
			{
				if( (ry == 1) && ((rx==1) || (rx==8)) )
				{
					_piece[colour][number]._bHasMovedThisGame = false; 						
				}
			}
			else if( rank == pieceRankType.PR_KNIGHT)
			{
				if( (ry == 1) && ((rx==2) || (rx==7)) )
				{
					_piece[colour][number]._bHasMovedThisGame = false; 						
				}
			}
			else if( rank == pieceRankType.PR_BISHOP)
			{
				if( (ry == 1) && ((rx==3) || (rx==6)) )
				{
					_piece[colour][number]._bHasMovedThisGame = false; 						
				}
			}
			else if( rank == pieceRankType.PR_QUEEN)
			{
				if( (ry == 1) && (rx==(4 + colour)) )
				{
					_piece[colour][number]._bHasMovedThisGame = false; 						
				}
			}
			else if( rank == pieceRankType.PR_KING)
			{
				if( (ry == 1) && (rx==(5 - colour)) )
				{
					_piece[colour][number]._bHasMovedThisGame = false; 						
				}
			}
			else
			{
				assert false;;
			}
		}
		else
		{
			assert false;
		}
	}

	public void MakeMove(Move move)
	{
		// Clear the PawnMovedTwoStepsLastTurn flags
		int pieceNum;

		for(pieceNum=0; pieceNum<JavaChess.PIECES_PER_SIDE; pieceNum++)
		{
			_piece[move._colour][pieceNum]._bPawnMovedTwoStepsLastTurn = false;
		}

		// Move the piece
		MovePiece(move);

		// If the pawn moved two steps then flag it
		if(move._rank == pieceRankType.PR_PAWN)
		{
			if((move._endRY - move._startRY) == 2)
			{
				_piece[move._colour][move._number]._bPawnMovedTwoStepsLastTurn = true;
			}
		}

		// Castling stuff
		if((move._bCastleQueenSide) || (move._bCastleKingSide))
		{
			Move rookMove = new Move(); // if castling
			pieceDescriptionType pieceDescrip = new pieceDescriptionType();

			rookMove._colour = move._colour;
			rookMove._rank = pieceRankType.PR_ROOK;
			rookMove._bIsCapture = false; // Is the move a capture?
			rookMove._promoteTo = pieceRankType.PR_NONE;
			rookMove._startRY = 1;
			rookMove._endRY = 1;
			rookMove._bIsCapture = false; // Is the move a capture?
			rookMove._promoteTo = pieceRankType.PR_NONE;
			rookMove._bIsCheck = false;
			rookMove._bIsMate = false;
			rookMove._bCastleQueenSide = false;
			rookMove._bCastleKingSide = false;
			rookMove._bEnPassantCapture = false;
			rookMove._score = JavaChess.MS_UNDEFINED;

			if(move._bCastleQueenSide)
			{
				if(move._colour == pieceColourType.PC_WHITE)
				{
					rookMove._startRX = 1; 
					rookMove._endRX = 4; 
				}
				else
				{
					rookMove._startRX = 8; 
					rookMove._endRX = 5; 
				}
			}
			else if(move._bCastleKingSide)
			{
				if(move._colour == pieceColourType.PC_WHITE)
				{
					rookMove._startRX = 8; 
					rookMove._endRX = 6; 
				}
				else
				{
					rookMove._startRX = 1; 
					rookMove._endRX = 3; 
				}
			}

			pieceDescrip = WhatIsAt(move._colour, rookMove._startRX, rookMove._startRY);

			if(pieceDescrip._rank != pieceRankType.PR_ROOK)
			{
				assert false;;
			}

			rookMove._number = pieceDescrip._number;

			MovePiece(rookMove);
		}
	}

	// Change the rank of a pawn. 
	// Note that the pawn doesn't have to be on the 8th row, as we may be loading a position
	public void PromotePawn(int colour, int number, int promoteTo)
	{
		// Get the current position of the pawn
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		WhereIs(colour, number, relPos);

		if( IsRelPositionOnBoard(colour, relPos._rx, relPos._ry) )
		{
			ConvertRelToAbsCoords(relPos, absPos);

			_position[absPos._x][absPos._y]._rank = promoteTo;
		}
	}

	public boolean IsPieceAlive(int colour, int number)
	{
		return _piece[colour][number]._bIsAlive;
	}

	public boolean HasPieceMovedThisGame(int colour, int number)
	{
		return _piece[colour][number]._bHasMovedThisGame;
	}

	public boolean HasPawnMovedTwoStepsLastTurn(int colour, int number)
	{
		return _piece[colour][number]._bPawnMovedTwoStepsLastTurn;
	}

	public void WhereIs(int colour, int number, relPosType relPos)
	{
		ConvertAbsToRelCoords(colour, _piece[colour][number]._ax, _piece[colour][number]._ay, relPos);
	}

	public pieceDescriptionType WhatIsAt(int colour, int rx, int ry)
	{
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		relPos._colour = colour;
		relPos._rx = rx;
		relPos._ry = ry;

		ConvertRelToAbsCoords(relPos, absPos);

		if(!IsAbsPositionOnBoard(absPos._x, absPos._y))
		{
			assert false;
		}

		return _position[absPos._x][absPos._y];
	}

	public void DisplayBoard()
	{
		pieceDescriptionType pieceDescrip;
		int ax, ay;
		char symbol;

		System.out.print("\n");

		for(ay = MAX_Y-1; ay>=0; ay--)
		{
			// print row number
			System.out.print((ay + 1) + " ");

			for(ax = 0; ax<MAX_X; ax++)
			{
				pieceDescrip = _position[ax][ay];

				if( pieceDescrip._rank == pieceRankType.PR_NONE)
				{
					symbol = '_';
				}
				else
				{
					symbol = ConvertRankToSymbol(pieceDescrip._rank);

					if( pieceDescrip._colour == pieceColourType.PC_BLACK )
					{
						// Display black pieces in lower case
						symbol = (char)(symbol - 'A' + 'a');
					}
				}

				System.out.print(symbol + "|");

			}

			System.out.print("\n");
		}

		System.out.print("  a|b|c|d|e|f|g|h|\n\n");
	}

	public static void SavePosition(String sFileName)
	{
/*
		int armyNum;
		int pieceNum;
		FILE * fptr;
		pieceDescriptionType pieceDescrip;

		fptr = fopen(sFileName,"w");

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<PIECES_PER_SIDE; pieceNum++)
			{
				if( _piece[armyNum][pieceNum]._bIsAlive )
				{
					if( armyNum == 0 )
					{
						fprintf.out.print(fptr,"W");
					}
					else
					{
						fprintf(fptr,"B");
					}

					pieceDescrip = WhatIsAt(pieceColourType.PC_WHITE, _piece[armyNum][pieceNum]._ax + 1, _piece[armyNum][pieceNum]._ay + 1);

					fprintf(fptr,"%c", ConvertRankToSymbol(pieceDescrip._rank));


					fprintf(fptr,"%c%d\n", _piece[armyNum][pieceNum]._ax + 'a', _piece[armyNum][pieceNum]._ay + 1 ); 
				}
			}
		}

		fclose(fptr);
*/
	}

	public static int OppositeColour(int colour)
	{
		if(colour == pieceColourType.PC_NONE)
		{
			assert false;;
		}

		if(colour == pieceColourType.PC_WHITE)
		{
			return pieceColourType.PC_BLACK;
		}

		return pieceColourType.PC_WHITE;
	}


	public static char ConvertRankToSymbol(int rank)
	{
		switch (rank)
		{
		case pieceRankType.PR_PAWN:	return 'P';
		case pieceRankType.PR_KNIGHT: return 'N';
		case pieceRankType.PR_BISHOP: return 'B';
		case pieceRankType.PR_ROOK: return 'R';
		case pieceRankType.PR_QUEEN: return 'Q';
		case pieceRankType.PR_KING: return 'K';
		default: return '?';
		}
	}

	public static int ConvertSymbolToRank(char symbol)
	{
		switch (symbol)
		{
		case 'P': return pieceRankType.PR_PAWN;
		case 'N': return pieceRankType.PR_KNIGHT;
		case 'B': return pieceRankType.PR_BISHOP;
		case 'R': return pieceRankType.PR_ROOK;
		case 'Q': return pieceRankType.PR_QUEEN;
		case 'K': return pieceRankType.PR_KING;
		default: return pieceRankType.PR_NONE;
		}
	}

	public static void ConvertRelToAbsCoords(relPosType relPos, posType absPos)
	{
		if(relPos._colour == pieceColourType.PC_WHITE)
		{
			absPos._x = relPos._rx - 1;
			absPos._y = relPos._ry - 1;
		}
		else if(relPos._colour == pieceColourType.PC_BLACK)
		{
			absPos._x = MAX_X - relPos._rx;
			absPos._y = MAX_Y - relPos._ry;
		}
		else
		{
			assert false;
		}
	}

	public static void ConvertAbsToRelCoords(int colour, int ax, int ay, relPosType relPos)
	{
		relPos._colour = colour;

		if(colour == pieceColourType.PC_WHITE)
		{
			relPos._rx = ax + 1;
			relPos._ry = ay + 1;
		}
		else if(colour == pieceColourType.PC_BLACK)
		{
			relPos._rx = MAX_X - ax;
			relPos._ry = MAX_Y - ay;
		}
		else
		{
			assert false;
		}
	}

	public static void InvertCoords(relPosType posEnemyCoords, relPosType posMyCoords)
	{
		posType absPos  = new posType();

		ConvertRelToAbsCoords(posEnemyCoords, absPos);

		posMyCoords._colour = OppositeColour(posEnemyCoords._colour);

		ConvertAbsToRelCoords(posMyCoords._colour, absPos._x, absPos._y, posMyCoords);

	}

	public static boolean IsAbsPositionOnBoard(int ax, int ay)
	{
		if(ax < 0)
		{
			return false;
		}

		if(ay < 0)
		{
			return false;
		}

		if(ax >= MAX_X)
		{
			return false;
		}

		if(ay >= MAX_Y)
		{
			return false;
		}

		return true;
	}

	public static boolean IsRelPositionOnBoard(int colour, int rx, int ry)
	{
		relPosType relPos = new relPosType();
		posType absPos = new posType();

		relPos._colour = colour;
		relPos._rx = rx;
		relPos._ry = ry;

		ConvertRelToAbsCoords(relPos, absPos);

		return IsAbsPositionOnBoard(absPos._x, absPos._y);
	}

// private methods

// This routine assumes that the move has already been validated and is thus valid for the piece
	private	void MovePiece(Move move)
	{
		relPosType startRelPos = new relPosType();
		relPosType endRelPos = new relPosType();
		posType startPos = new posType();
		posType endPos = new posType();

		startRelPos._colour = move._colour;
		startRelPos._rx = move._startRX;
		startRelPos._ry = move._startRY;

		ConvertRelToAbsCoords(startRelPos, startPos);

		endRelPos._colour = move._colour;
		endRelPos._rx = move._endRX;
		endRelPos._ry = move._endRY;

		ConvertRelToAbsCoords(endRelPos, endPos);

		// Check start pos is on the board
		if(!IsAbsPositionOnBoard(startPos._x, startPos._y))
		{
			assert false;
		}

		// Check end pos is on the board
		if(!IsAbsPositionOnBoard(endPos._x, endPos._y))
		{
			assert false;
		}

		// Check start pos matches where the piece currently is
		if(startPos._x != _piece[move._colour][move._number]._ax)
		{
			assert false;	
		}

		if(startPos._y != _piece[move._colour][move._number]._ay)
		{
			assert false;	
		}

		// Check if anything is at the end point
		// If it is a friendly piece then the move is illegal
		if(_position[endPos._x][endPos._y]._colour == move._colour)
		{
			assert false;
		}

		// If it is an enemy then remove it
		if(_position[endPos._x][endPos._y]._colour != pieceColourType.PC_NONE)
		{
			RemovePiece(_position[endPos._x][endPos._y]._colour, _position[endPos._x][endPos._y]._number, endPos._x, endPos._y);
		}

		// Handle en passant captures
		if( move._bEnPassantCapture )
		{
			relPosType epCaptureRelPos = new relPosType();
			posType epCapturePos = new posType();

			epCaptureRelPos._colour = move._colour;
			epCaptureRelPos._rx = move._endRX;
			epCaptureRelPos._ry = move._endRY - 1;

			ConvertRelToAbsCoords(epCaptureRelPos, epCapturePos);

			RemovePiece(_position[epCapturePos._x][epCapturePos._y]._colour, _position[epCapturePos._x][epCapturePos._y]._number, epCapturePos._x, epCapturePos._y);
		}

		// Move piece to destination
		int colour, number, rank;

		colour = _position[startPos._x][startPos._y]._colour;
		number = _position[startPos._x][startPos._y]._number;
		rank = _position[startPos._x][startPos._y]._rank;

		_position[endPos._x][endPos._y]._colour = colour;
		_position[endPos._x][endPos._y]._number = number;

		if(move._promoteTo != pieceRankType.PR_NONE)
		{
			_position[endPos._x][endPos._y]._rank = move._promoteTo;
		}
		else
		{
			_position[endPos._x][endPos._y]._rank = _position[startPos._x][startPos._y]._rank;
		}

		_position[startPos._x][startPos._y]._colour = pieceColourType.PC_NONE;
		_position[startPos._x][startPos._y]._rank = pieceRankType.PR_NONE;
		_position[startPos._x][startPos._y]._number = pieceNumberType.PN_NONE;
//		_cells[startPos._x][startPos._y].setIcon(null);

		_piece[move._colour][move._number]._ax = endPos._x;
		_piece[move._colour][move._number]._ay = endPos._y;
		_piece[move._colour][move._number]._bHasMovedThisGame = true;
	}

	private	void RemovePiece(int colour, int number, int ax, int ay)
	{
		// Check position is on the board
		if(!IsAbsPositionOnBoard(ax, ay))
		{
			assert false;
		}

		// Check the piece we think is there is actually there
		if(colour != _position[ax][ay]._colour)
		{
			assert false;
		}

		if(number != _position[ax][ay]._number)
		{
			assert false;
		}

		if(_piece[colour][number]._ax != ax)
		{
			assert false;
		}

		if(_piece[colour][number]._ay != ay)
		{
			assert false;
		}

		// Check that the piece is alive
		if(_piece[colour][number]._bIsAlive == false)
		{
			assert false;
		}

		// Remove the piece
		_position[ax][ay]._colour = pieceColourType.PC_NONE;
		_position[ax][ay]._rank = pieceRankType.PR_NONE;

//		_cells[ax][ay].setIcon(null);

		_piece[colour][number]._bIsAlive = false;
		_piece[colour][number]._ax = -1;
		_piece[colour][number]._ay = -1;
	}


	public void Copy(Board src)
	{
		int x, y;

		for(x=0; x<MAX_X; x++)
		{
			for(y=0; y<MAX_Y; y++)
			{
				_position[x][y] = new pieceDescriptionType();

				pieceDescriptionType srcPieceDescrip;
				relPosType relPos = new relPosType();

				ConvertAbsToRelCoords(pieceColourType.PC_WHITE, x, y, relPos);

				srcPieceDescrip = src.WhatIsAt(relPos._colour, relPos._rx, relPos._ry);

				_position[x][y]._rank = srcPieceDescrip._rank;
				_position[x][y]._colour = srcPieceDescrip._colour;
				_position[x][y]._number = srcPieceDescrip._number;
			}
		}

		int armyNum, pieceNum;

		for(armyNum=0; armyNum<MAX_ARMIES; armyNum++)
		{
			for(pieceNum=0; pieceNum<JavaChess.PIECES_PER_SIDE; pieceNum++)
			{
				_piece[armyNum][pieceNum] = new posDescriptionType();

				posDescriptionType srcPosDescrip;

				srcPosDescrip = src.getPosDescription(armyNum, pieceNum);

				_piece[armyNum][pieceNum]._bIsAlive = srcPosDescrip._bIsAlive;
				_piece[armyNum][pieceNum]._ax = srcPosDescrip._ax;
				_piece[armyNum][pieceNum]._ay = srcPosDescrip._ay;
				_piece[armyNum][pieceNum]._bHasMovedThisGame = srcPosDescrip._bHasMovedThisGame;
				_piece[armyNum][pieceNum]._bPawnMovedTwoStepsLastTurn = srcPosDescrip._bPawnMovedTwoStepsLastTurn;
			}
		}
	}

	public posDescriptionType getPosDescription(int colour, int pieceNum)
	{
		return _piece[colour][pieceNum];
	}

	// The positions on the board, lookup what piece is there
	private	pieceDescriptionType[][] _position = new pieceDescriptionType[Board.MAX_X][Board.MAX_Y];

	// The pieces on either side, lookup where it is, or if it is dead
	private	posDescriptionType[][] _piece =  new posDescriptionType[Board.MAX_ARMIES][JavaChess.PIECES_PER_SIDE];

//	private JPanel _boardPane;

}

