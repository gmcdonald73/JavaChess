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

    public void close() {
        if (frame != null) {
            frame.dispose();
            // frame = null;
        }
    }
}

