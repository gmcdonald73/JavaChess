package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUIBoard implements ActionListener
{

	public static final int MAX_X = 8;
	public static final int MAX_Y = 8;
	public static final int MAX_ARMIES = 2;

	JButton[][] _cells = new JButton[8][8];
	boolean _bIsSelected;
	JButton _sourceBtn;
	Color _sourceBackColour;
	String _sourceCoord;
	String _destCoord;
	boolean _bHasMove;
	PopupMenu _promotePawnPopup;
	char _cPromoteTo;

	ImageIcon _whiteIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];
	ImageIcon _blackIcons[] = new ImageIcon[pieceRankType.PR_MAX_RANKS];

// public methods
	public GUIBoard(ImageIcon[] whiteIcons, ImageIcon[] blackIcons)
	{
		int x, y;
		int i;

		for(i=0; i<pieceRankType.PR_MAX_RANKS; i++)
		{
			_whiteIcons[i] = whiteIcons[i];
			_blackIcons[i] = blackIcons[i];
		}

		_board = new Board();

		GridLayout lm = new GridLayout(9, 9, 0, 0);
		_boardPane = new JPanel();
		_boardPane.setLayout(lm);

		_bHasMove = false;
		_bIsSelected = false;

		JButton btn;
        MenuItem mi;

	    _promotePawnPopup = new PopupMenu("Choose Piece:");

        mi = new MenuItem("Bishop");
        mi.addActionListener(this);
		mi.setActionCommand("_B"); 
	    _promotePawnPopup.add(mi);

        mi = new MenuItem("Knight");
        mi.addActionListener(this);
		mi.setActionCommand("_N"); 
	    _promotePawnPopup.add(mi);

        mi = new MenuItem("Rook");
        mi.addActionListener(this);
		mi.setActionCommand("_R"); 
	    _promotePawnPopup.add(mi);

        mi = new MenuItem("Queen");
        mi.addActionListener(this);
		mi.setActionCommand("_Q"); 
	    _promotePawnPopup.add(mi);

	    _boardPane.add(_promotePawnPopup); // add popup menu to applet
           
		for(y = 8; y>=0; y--)
		{
			for(x = 0; x<9; x++)
			{	
				if ((x>0) && (y>0))
				{
					btn = new JButton();

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
	}

	public void Update()
	{
		pieceDescriptionType pieceDescrip;
		int x, y;
		char symbol;


		for(y = MAX_Y; y>0; y--)
		{
			for(x = 1; x<=MAX_X; x++)
			{
				
				pieceDescrip = _board.WhatIsAt(pieceColourType.PC_WHITE, x, y);

				if( pieceDescrip._rank == pieceRankType.PR_NONE)
				{
					_cells[x-1][y-1].setIcon(null);
				}
				else
				{
					_cells[x-1][y-1].setIcon( getIcon(pieceDescrip._colour, pieceDescrip._rank));
				}
			}
		}
	}


	public Board getBoard()
	{
		return _board;
	}

	public JPanel getPanel()
	{
		return _boardPane;
	}

	public boolean HasMove()
	{
		return _bHasMove;
	}

	public String GetMove()
	{
		int sourceX, sourceY;
		int destX, destY;
		pieceDescriptionType pieceDescrip;
		char symbol;
		String sMove;

		_bHasMove = false;

		sourceX = ((int)(_sourceCoord.charAt(0) - 'a')) + 1;
		sourceY = (int)(_sourceCoord.charAt(1) - '0');

		destX = ((int)(_destCoord.charAt(0) - 'a')) + 1;
		destY = (int)(_destCoord.charAt(1) - '0');

		pieceDescrip = _board.WhatIsAt(pieceColourType.PC_WHITE, sourceX, sourceY);

		// !!! Need to check that their is a piece at source!!!

		symbol = _board.ConvertRankToSymbol(pieceDescrip._rank);

		sMove = symbol + _sourceCoord + "-" + _destCoord; 

		if( (pieceDescrip._rank == pieceRankType.PR_PAWN) && ((destY == 8) || (destY == 1)) )
		{
			// Create a popup menu, get user to choose piece to promote pawn to
			// from Knight, Bishop, Rook or Queen
			_cPromoteTo = '*';
			_promotePawnPopup.show(_boardPane, 0, 0);

			// HACK!!! : popup menu must operate asychronously???. Wait until its results are ready.
			do {} while (_cPromoteTo == '*');

			// Pawn has reached last rank, promote to Queen
			sMove = sMove + _cPromoteTo;
		}

//		System.out.println("Move = " + sMove);

		return sMove;
	}

	public void actionPerformed(ActionEvent evt)
	{
		Object source = evt.getSource();
		String sActionCommand = evt.getActionCommand();
//		statusLine.setText(sActionCommand);

		if (sActionCommand.charAt(0) == '_')
		{
			// This is the choice the user has made when promoting a pawn
			_cPromoteTo = sActionCommand.charAt(1);
			return;
		}

		// Otherwise user is moving piece normally
		if (!_bIsSelected)
		{
			_bIsSelected = true;
			_sourceBtn = (JButton)source;
			_sourceBackColour = _sourceBtn.getBackground();
			_sourceBtn.setBackground(Color.blue);
			_sourceCoord = sActionCommand;

			// !!! Could highlight the legal moves for this piece here
		}
		else
		{
			_bIsSelected = false;
			JButton dest = (JButton)evt.getSource();

			_destCoord = sActionCommand;
			_sourceBtn.setBackground(_sourceBackColour);

			if (_destCoord != _sourceCoord )
			{
				_bHasMove = true;
			}
		}
	}


	public ImageIcon getIcon(int colour, int rank)
	{
		if (colour == pieceColourType.PC_WHITE)
		{
			return _whiteIcons[rank];
		}
		else if (colour == pieceColourType.PC_BLACK)
		{
			return _blackIcons[rank];
		}
		else
		{
			return null;
		}
	}

	private Board _board;
	private JPanel _boardPane;
}






