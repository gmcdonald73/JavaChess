package ui;

import engine.board.Board;

import javax.swing.*;

public class GraphicalChess
{
    private final JFrame frame;
    private final ChessBoardPanel chessBoardPanel;

    public GraphicalChess(Board board)
    {
        chessBoardPanel = new ChessBoardPanel(board);

        frame = new JFrame("Java Chess");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(chessBoardPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void refresh()
    {
        chessBoardPanel.repaint();
    }
}

/*
public class GraphicalChess
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            JFrame frame = new JFrame("Java Chess");

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE);

            Board board = new Board();

            board.SetupForGame();

            frame.add(new ChessBoardPanel(board));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}*/
