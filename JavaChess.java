import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
// import MyStuff.ConsoleInput;

class JavaChess extends JFrame
{

public static final int PIECES_PER_SIDE = 16;
public static final int MAX_PIECE_POSSIBLE_MOVES = 32; // maximum number of possible moves for any single piece

public static final int NUM_MOVES_LOOKAHEAD = 2; // This is actually the number of half moves. This number should be even

public static final int MAX_MOVES_LOOKAHEAD = NUM_MOVES_LOOKAHEAD;
public static final String GUIJAVACHESS_VERSION = "1.0";


// Move Score
public static final int MS_UNDEFINED = -1;
public static final int MS_ILLEGAL = -2;
public static final int MS_LOSE = 0;
public static final int MS_DRAW = 500;
public static final int MS_WIN	= 1000;

JLabel _statusLine = new JLabel("Welcome to JavaChess!");

Board _board; // = new Board();
GUIBoard _guiBoard; 

Army[] _army = new Army[2];
boolean[] _armyIsHumanControlled = {true, false};

ImageIcon _whiteIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];
ImageIcon _blackIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];

JavaChess()
{
	super("GUI Java Chess");
	setSize(500, 500);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

	_guiBoard = new GUIBoard(_whiteIcons, _blackIcons);

	_board = _guiBoard.getBoard();

	GridBagLayout gridBag = new GridBagLayout();
	GridBagConstraints constraints = new GridBagConstraints();
	JPanel pane = new JPanel();
	pane.setLayout(gridBag);

	_army[0] = new Army();
	_army[1] = new Army();

	// Initialise or load a game
	_army[0].Initialise(pieceColourType.PC_WHITE, _board, _army[1]);
	_army[1].Initialise(pieceColourType.PC_BLACK, _board, _army[0]);

	_army[0].SetupForGame(_board);
	_army[1].SetupForGame(_board);

//	_army[0].SetupForTesting(_board);
//	_army[1].SetupForTesting(_board);

	_guiBoard.Update();

	JPanel subPane;
	subPane = _guiBoard.getPanel();
	buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
	gridBag.setConstraints(subPane, constraints);
	pane.add(subPane);

	buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
	gridBag.setConstraints(_statusLine, constraints);
	pane.add(_statusLine);

	setContentPane(pane);
	setVisible(true);
}

void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy)
{
	gbc.gridx = gx;
	gbc.gridy = gy;
	gbc.gridwidth = gw;
	gbc.gridheight = gh;
	gbc.weightx = wx;
	gbc.weighty = wy;
//	gbc.fill = GridBagConstraints.BOTH;
}

public static void main(String[] arguments)
{
	JavaChess jc = new JavaChess();

	jc.show();

	jc.PlayGame();

//	SolvePuzzle("D:\\Projects\\AsciiChess\\Positions\\Diagram238.txt", 4);
//	SolvePuzzle("D:\\Projects\\AsciiChess\\Positions\\Diagram239.txt", 4);
//	SolvePuzzle("D:\\Projects\\AsciiChess\\Positions\\Diagram240.txt", 6);
//	SolvePuzzle("D:\\Projects\\AsciiChess\\Positions\\Diagram241.txt", 5);
//	SolvePuzzle("D:\\Projects\\AsciiChess\\Positions\\Diagram242.txt", 6);

}

void PlayGame()
{
	Move[] moveStack = new Move[MAX_MOVES_LOOKAHEAD];
	int moveNum = 1;
	boolean bDone = false; //User has quit, or game has ended
	int armyNum = 0;
	String sMove = "";
	Piece piece = null;
	boolean bLegalMove;
//	FileWriter movesFile;

/*
	try
	{
		movesFile = new FileWriter("JavaMoves.txt");
	}
	catch(IOException e)
	{
		OutputMessage("File open failed : " + e.getMessage());
		return;
	}
*/
	// Change to use a MoveStack?
	for(int i=0; i<MAX_MOVES_LOOKAHEAD; i++)
	{
		moveStack[i] = new Move();
	}

//	System.out.print("\nType 'help' for help\n\n");

	// while not quit or checkmate
	while(!bDone)
	{
		if(_armyIsHumanControlled[armyNum])
		{
			// Human is controlling this army

			// See if army has any legal moves, or is checkmated or stalemated
			bLegalMove = _army[armyNum].HasAnyLegalMoves(_board);

			if(bLegalMove)
			{
				// Human has at least one legal move, keep looping until they enter one
				bLegalMove = false;

				while(!bLegalMove)
				{
					// optionally suggest move
					// accept move
					if( armyNum == 0)
					{
//						OutputMessage("White's move");
//						OutputMessage("White : enter move " + moveNum);
					}
					else
					{
//						OutputMessage("Black's move");
//						OutputMessage("Black : enter move " + moveNum);
					}

					boolean bGotMove = false;
					do
					{
						if (_guiBoard.HasMove())
						{
							sMove = _guiBoard.GetMove();

							// Clear the status line
							OutputMessage("");
							bGotMove = true;
						}
					} while ( !bGotMove);

//					sMove = ConsoleInput.readLine();

					// moveStack[0] = new Move();
					String sErrMsg = "";

					bDone = _army[armyNum].InterpretMove(_board, sMove, piece, moveStack[0]);
					bLegalMove = moveStack[0]._bLegalMove;

					// If the user didn't enter a legal move then redisplay the board for them
					if(!bLegalMove)
					{
						sErrMsg = _army[armyNum].GetErrMsg();
						OutputMessage(sErrMsg);
//						_board.DisplayBoard();
						_guiBoard.Update();
					}
				}
			}
		}
		else
		{
			// Computer is controlling this army
			Date startTime, finishTime;

			// Find best move
//			startTime = new Date();
			bLegalMove = _army[armyNum].FindBestMove(moveNum, _board, NUM_MOVES_LOOKAHEAD, moveStack);
//			finishTime = new Date();

//			OutputMessage("Start time = " + startTime);
//			OutputMessage("Finish time = " + finishTime);
//			OutputMessage("Num seconds taken = %ld\n" + finishTime - startTime );

			if(bLegalMove)
			{
				// Display it
				// moveStack[0].Print(moveNum);
				OutputMessage(moveStack[0].Format(moveNum));
			}

		}

		if( !bDone )
		{
			if(bLegalMove)
			{
				// Make the move
				_board.MakeMove(moveStack[0]);
//				_board.DisplayBoard();
				_guiBoard.Update();

				// Save the move
				sMove = moveStack[0].Format(moveNum);
				sMove = sMove + "\r\n";

/*
				try
				{
					movesFile.write(sMove, 0, sMove.length());
				}
				catch(IOException e)
				{
					OutputMessage("File write failed : " + e.getMessage());
					return;
				}
*/
				// Increment army and moveNum
				armyNum++;

				if(armyNum > 1)
				{
					armyNum = 0;
					moveNum++;
				}
			}
			else
			{
				// No legal move for army, either checkmate or stalemate
				if( _army[armyNum].IsKingInCheck(_board) )
				{
					OutputMessage("Checkmate!!!\n");
				}
				else
				{
					OutputMessage("Stalemate!!!\n");
				}

				bDone = true;
			}
		}

		if( moveNum >= 100)
		{
			OutputMessage("*** HACK : 100 moves limit reached - this game is over!!! ***");
			bDone = true;
		}
	}

/*
	try
	{
		movesFile.close();
	}
	catch(IOException e)
	{
		OutputMessage("File close failed : " + e.getMessage());
		return;
	}
*/
//	OutputMessage("Press enter to exit\n");
//	ConsoleInput.readLine();
}

/*
void SolvePuzzle(String sFilename, int numMoves)
{
	Board board;
	Army[] army = new Army[2];
	int i;
	Move[] moveStack = new Move[MAX_MOVES_LOOKAHEAD];
	time_t startTime, finishTime;

	if(numMoves > MAX_MOVES_LOOKAHEAD)
	{
		Exception e;
		throw e;
	}

	startTime = time(NULL);

	// Initialise armies
	army[0].Initialise(PC_WHITE, board, army[1]);
	army[1].Initialise(PC_BLACK, board, army[0]);

	// Load the position to solve
	if(LoadPosition(sFilename, board, army) )
	{
//		board.DisplayBoard();

		for(i=0; i<MAX_MOVES_LOOKAHEAD; i++)
		{
			moveStack[i]._score = MS_UNDEFINED;
		}

		// Find best move for white
		if( !army[0].FindBestMove(1, board, numMoves, moveStack) )
		{
			OutputMessage("No legal move\n");
		}
		else
		{
			// Display move stack
			PrintMoves(numMoves, moveStack);
		}

		finishTime = time(NULL);

		OutputMessage("Num seconds taken = %ld\n", finishTime - startTime );
	}

	OutputMessage("Press any key to continue\n");
	getch();
}
*/

/*
bool LoadPosition(String sFilename, Board board, Army army[])
{
	FILE * fptr;
	String sLine;
	char cSide, cSymbol, cColumn;
	int row;
	pieceRankType rank;
	int rankCount[2][PR_MAX_RANKS] = {{0},{0}};
	int[] maxRankCount = {1,1,2,2,2,8};
	pieceNumberType[] rankBase = {PN_KING, PN_QUEEN, PN_BISHOP_1, PN_KNIGHT_1, PN_ROOK_1, PN_PAWN_1 };
	int armyNum;

	Piece piece;

	fptr = fopen(sFilename, "r");

	if( fptr == NULL)
	{
		OutputMessage("Unable to open file '%s'\n", sFilename);
		return false;
	}

	while(fscanf(fptr, "%s\n", sLine) == 1)
	{
		if( (sLine[0] == '/') && (sLine[1] == '/'))
		{
			continue;
		}

		cSide = sLine[0];
		cSymbol = sLine[1];
		cColumn = sLine[2];
		row = sLine[3] - '1' + 1;

		rank = board->ConvertSymbolToRank(cSymbol);

		if( rank == PR_NONE )
		{
			Exception e;
			throw e;
		}

		
		// Find the correct piece
		if( cSide == 'W' )
		{
			armyNum = 0;
		}
		else if ( cSide == 'B' )
		{
			armyNum = 1;
		}
		else
		{
			Exception e;
			throw e;
		}

		rankCount[armyNum][rank]++;

		if( rankCount[armyNum][rank] <= maxRankCount[rank] )
		{
			piece = army[armyNum].Piece( pieceNumberType(rankBase[rank] + rankCount[armyNum][rank] - 1) );

			// Place it onto the board
			board->PlacePiece(piece, cColumn, row);
		}
		else
		{
			if( (rank == PR_PAWN) || (rank == PR_KING))
			{
				Exception e;
				throw e;
			}

			// Otherwise start turning pawns into pieces
			rankCount[armyNum][PR_PAWN]++;
			if( rankCount[armyNum][PR_PAWN] <= maxRankCount[PR_PAWN] )
			{
				piece = army[armyNum].Piece( pieceNumberType(rankBase[PR_PAWN] + rankCount[armyNum][PR_PAWN] - 1) );

				// Place it onto the board
				board->PlacePiece(piece, cColumn, row);

				// Promote pawn to desired rank
				cPawn * pPawn;

				pPawn = (cPawn *)piece;

				pPawn->PromoteTo(board, rank);
			}
			else
			{
				// maximum pawns already
				Exception e;
				throw e;
			}
		}

	}

	return true;
}
*/

void PrintMoves(int numMoves, Move moveStack[])
{
	int i;

	for(i=0; i<numMoves; i++)
	{
		moveStack[i].Print(i);
	}
}

void OutputMessage(String sMsg)
{
//	System.out.print(sMsg + "\n");
//	_statusText.append(sMsg + "\n");
	_statusLine.setText(sMsg);
}


}