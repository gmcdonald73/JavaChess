import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Alphabet extends JFrame implements MouseListener, MouseMotionListener
{

	int selx, sely;
	boolean bIsSelected;

	JButton[][] cells = new JButton[9][9];

	Alphabet()
	{
		super("GUI Java Chess");
		setSize(450, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		bIsSelected = false;

		JPanel pane = new JPanel();
		GridLayout lm = new GridLayout(9, 9, 0, 0);
		pane.setLayout(lm);

		int x, y;
		ImageIcon whitePawn = new ImageIcon("3Pawn.gif");
		ImageIcon whiteRook = new ImageIcon("3Rook.gif");
		ImageIcon whiteKnight = new ImageIcon("3Knight.gif");
		ImageIcon whiteBishop = new ImageIcon("3Bishop.gif");
		ImageIcon whiteQueen = new ImageIcon("3Queen.gif");
		ImageIcon whiteKing = new ImageIcon("3king.gif");

		ImageIcon blackPawn = new ImageIcon("4Pawn.gif");
		ImageIcon blackRook = new ImageIcon("4Rook.gif");
		ImageIcon blackKnight = new ImageIcon("4Knight.gif");
		ImageIcon blackBishop = new ImageIcon("4Bishop.gif");
		ImageIcon blackQueen = new ImageIcon("4Queen.gif");
		ImageIcon blackKing = new ImageIcon("4king.gif");

		for(y = 8; y>=0; y--)
		{
			for(x =0; x<9; x++)
			{	
				cells[x][y] = new JButton();

				if((x>0) && (y>0))
				{
					if(((x+y) % 2) == 0)
					{
						cells[x][y].setBackground(Color.black);
					}
					else
					{
						cells[x][y].setBackground(Color.white);
					}
				}

				pane.add(cells[x][y]);
			}
		}

		cells[1][1].setIcon(whiteRook);
		cells[2][1].setIcon(whiteKnight);
		cells[3][1].setIcon(whiteBishop);
		cells[4][1].setIcon(whiteQueen);
		cells[5][1].setIcon(whiteKing);
		cells[6][1].setIcon(whiteBishop);
		cells[7][1].setIcon(whiteKnight);
		cells[8][1].setIcon(whiteRook);

		cells[1][8].setIcon(blackRook);
		cells[2][8].setIcon(blackKnight);
		cells[3][8].setIcon(blackBishop);
		cells[4][8].setIcon(blackQueen);
		cells[5][8].setIcon(blackKing);
		cells[6][8].setIcon(blackBishop);
		cells[7][8].setIcon(blackKnight);
		cells[8][8].setIcon(blackRook);

		for(y=1; y<9; y++)
		{
			cells[0][y].setLabel(y + "");
		}

		for(x=1; x<9; x++)
		{
//			int a = (int)'a';
			char c = (char)('a' + (x-1));

			cells[x][0].setLabel(c + "");
			cells[x][2].setIcon(whitePawn);
			cells[x][7].setIcon(blackPawn);
		}

		setContentPane(pane);
		setVisible(true);
	}

	public static void main(String[] arguments)
	{
		JFrame frame = new Alphabet();
		frame.show();
	}

	public void mouseClicked(MouseEvent evt)
	{
	}

	public void mouseEntered(MouseEvent evt)
	{
	}

	public void mouseExited(MouseEvent evt)
	{
	}

	public void mousePressed(MouseEvent evt)
	{
	}

	public void mouseReleased(MouseEvent evt)
	{
	}

	public void mouseDragged(MouseEvent evt)
	{
	}
	public void mouseMoved(MouseEvent evt)
	{
	}

}

