import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class ChessApplet extends javax.swing.JApplet
{

	public static final int NUM_MOVES_LOOKAHEAD = 2; // This is actually the number of half moves. Ideally this number should be even, but 3 makes for fast responses
	public static final int MAX_MOVES_LOOKAHEAD = NUM_MOVES_LOOKAHEAD;

	JLabel _statusLine = new JLabel("Welcome to JavaChess!");

	ImageIcon _whiteIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];
	ImageIcon _blackIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];

	Board _board; 
	GUIBoard _guiBoard; 
	Army[] _army = new Army[2];
	boolean[] _armyIsHumanControlled = {true, false};
	Thread _ceThread;

	public void init()
	{
		Image img;

		// Load white icons
		img = getImage(getCodeBase(), "3Pawn.gif");
		_whiteIcons[pieceRankType.PR_PAWN] = new ImageIcon(img);

		img = getImage(getCodeBase(), "3Rook.gif");
		_whiteIcons[pieceRankType.PR_ROOK] = new ImageIcon(img);

		img = getImage(getCodeBase(), "3Knight.gif");
		_whiteIcons[pieceRankType.PR_KNIGHT] = new ImageIcon(img);

		img = getImage(getCodeBase(), "3Bishop.gif");
		_whiteIcons[pieceRankType.PR_BISHOP] = new ImageIcon(img);

		img = getImage(getCodeBase(), "3Queen.gif");
		_whiteIcons[pieceRankType.PR_QUEEN] = new ImageIcon(img);

		img = getImage(getCodeBase(), "3King.gif");
		_whiteIcons[pieceRankType.PR_KING] = new ImageIcon(img);

		// Load black icons
		img = getImage(getCodeBase(), "4Pawn.gif");
		_blackIcons[pieceRankType.PR_PAWN] = new ImageIcon(img);

		img = getImage(getCodeBase(), "4Rook.gif");
		_blackIcons[pieceRankType.PR_ROOK] = new ImageIcon(img);

		img = getImage(getCodeBase(), "4Knight.gif");
		_blackIcons[pieceRankType.PR_KNIGHT] = new ImageIcon(img);

		img = getImage(getCodeBase(), "4Bishop.gif");
		_blackIcons[pieceRankType.PR_BISHOP] = new ImageIcon(img);

		img = getImage(getCodeBase(), "4Queen.gif");
		_blackIcons[pieceRankType.PR_QUEEN] = new ImageIcon(img);

		img = getImage(getCodeBase(), "4King.gif");
		_blackIcons[pieceRankType.PR_KING] = new ImageIcon(img);

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

		_guiBoard.Update();
		repaint();

		JPanel subPane;
		subPane = _guiBoard.getPanel();
		buildConstraints(constraints, 0, 0, 1, 1, 100, 100);
		gridBag.setConstraints(subPane, constraints);
		pane.add(subPane);

		buildConstraints(constraints, 0, 1, 1, 1, 100, 100);
		gridBag.setConstraints(_statusLine, constraints);
		pane.add(_statusLine);

		getContentPane().add(pane);
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

	public void start()
	{
		ChessEngine ce = new ChessEngine();
		_ceThread = new Thread(ce);

		_ceThread.start();
	}

	public void stop()
	{
//		_ceThread.stop();
	}

	void OutputMessage(String sMsg)
	{
		_statusLine.setText(sMsg);
	}


private class ChessEngine implements Runnable
{
	public void run()
	{
		Move[] moveStack = new Move[MAX_MOVES_LOOKAHEAD];
		int moveNum = 1;
		boolean bDone = false; //User has quit, or game has ended
		int armyNum = 0;
		String sMove = "";
		Piece piece = null;
		boolean bLegalMove;

		// Change to use a MoveStack?
		for(int i=0; i<MAX_MOVES_LOOKAHEAD; i++)
		{
			moveStack[i] = new Move();
		}

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
							repaint();
						}
					}
				}
			}
			else
			{
				// Computer is controlling this army
				Date startTime, finishTime;

				OutputMessage("Thinking...");

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
					repaint();

					// Save the move
					sMove = moveStack[0].Format(moveNum);
					sMove = sMove + "\r\n";

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
						OutputMessage("Checkmate!!!");
					}
					else
					{
						OutputMessage("Stalemate!!!");
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

	//	OutputMessage("Press enter to exit\n");
	//	ConsoleInput.readLine();
	}
}

}
