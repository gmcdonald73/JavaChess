package ui;

import engine.board.Board;
import engine.board.Square;
import engine.pieces.Colour;
import engine.pieces.Piece;
import engine.pieces.PieceRank;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardPanel extends JPanel
{
    private static final int SQUARE_SIZE = 80;

    private final Board board;
    private final Map<String, Image> pieceImages;

    public ChessBoardPanel(Board board)
    {
        this.board = board;

        setPreferredSize(new Dimension(
                8 * SQUARE_SIZE,
                8 * SQUARE_SIZE));

        pieceImages = new HashMap<>();

        // Load images when the panel is created.
        pieceImages.put("WK", loadImage("3King.gif"));
        pieceImages.put("WQ", loadImage("3Queen.gif"));
        pieceImages.put("WR", loadImage("3Rook.gif"));
        pieceImages.put("WB", loadImage("3Bishop.gif"));
        pieceImages.put("WN", loadImage("3Knight.gif"));
        pieceImages.put("WP", loadImage("3Pawn.gif"));

        pieceImages.put("BK", loadImage("4King.gif"));
        pieceImages.put("BQ", loadImage("4Queen.gif"));
        pieceImages.put("BR", loadImage("4Rook.gif"));
        pieceImages.put("BB", loadImage("4Bishop.gif"));
        pieceImages.put("BN", loadImage("4Knight.gif"));
        pieceImages.put("BP", loadImage("4Pawn.gif"));
    }

    private Image loadImage(String filename)
    {
        URL url = getClass().getResource(
                "/gui/" + filename);

        if (url == null)
        {
            throw new IllegalArgumentException(
                    "Image not found: " + filename);
        }

        return new ImageIcon(url).getImage();
    }

    private Image getPieceImage(Piece piece)
    {
        String colour = switch (piece.Colour())
        {
            case WHITE -> "W";
            case BLACK -> "B";
            case NONE -> null;
        };

        String rank = String.valueOf(piece.Rank().getSymbol());

        String key = colour + rank;
        Image image = pieceImages.get(key);

        if (image == null)
        {
            throw new IllegalArgumentException(
                    "No image loaded for piece: " + key);
        }

        return image;
    }

    private void drawPieces(Graphics g)
    {
        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                boolean lightSquare = (x + y) % 2 == 1;

                g.setColor(lightSquare
                        ? new Color(87, 106, 138)
                        : new Color(3, 41, 102));

                g.fillRect(
                        x * SQUARE_SIZE,
                        (7 - y) * SQUARE_SIZE,
                        SQUARE_SIZE,
                        SQUARE_SIZE);

                Square square = new Square(x, y);
                Piece piece = board.WhatIsAtSquare(square);

                if (piece != null)
                {
                    drawPiece(g, piece, x, y);
                }
            }
        }
    }
    private void drawPiece(
            Graphics g, Piece piece, int x, int y)
    {
        Image image = getPieceImage(piece);

        g.drawImage(
                image,
                x * SQUARE_SIZE,
                (7 - y) * SQUARE_SIZE,
                SQUARE_SIZE,
                SQUARE_SIZE,
                this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

//        drawSquares(g);
        drawPieces(g);
    }
}