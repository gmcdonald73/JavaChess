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

        if (true)
        {
            // Load images when the panel is created.
            pieceImages.put("WK", loadImage("wikipedia", "wK.png"));
            pieceImages.put("WQ", loadImage("wikipedia", "wQ.png"));
            pieceImages.put("WR", loadImage("wikipedia", "wR.png"));
            pieceImages.put("WB", loadImage("wikipedia", "wB.png"));
            pieceImages.put("WN", loadImage("wikipedia", "wN.png"));
            pieceImages.put("WP", loadImage("wikipedia", "wP.png"));

            pieceImages.put("BK", loadImage("wikipedia", "bK.png"));
            pieceImages.put("BQ", loadImage("wikipedia", "bQ.png"));
            pieceImages.put("BR", loadImage("wikipedia", "bR.png"));
            pieceImages.put("BB", loadImage("wikipedia", "bB.png"));
            pieceImages.put("BN", loadImage("wikipedia", "bN.png"));
            pieceImages.put("BP", loadImage("wikipedia", "bP.png"));

        } else
        {
            // Load images when the panel is created.
            pieceImages.put("WK", loadImage("set1", "3King.gif"));
            pieceImages.put("WQ", loadImage("set1", "3Queen.gif"));
            pieceImages.put("WR", loadImage("set1", "3Rook.gif"));
            pieceImages.put("WB", loadImage("set1", "3Bishop.gif"));
            pieceImages.put("WN", loadImage("set1", "3Knight.gif"));
            pieceImages.put("WP", loadImage("set1", "3Pawn.gif"));

            pieceImages.put("BK", loadImage("set1", "4King.gif"));
            pieceImages.put("BQ", loadImage("set1", "4Queen.gif"));
            pieceImages.put("BR", loadImage("set1", "4Rook.gif"));
            pieceImages.put("BB", loadImage("set1", "4Bishop.gif"));
            pieceImages.put("BN", loadImage("set1", "4Knight.gif"));
            pieceImages.put("BP", loadImage("set1", "4Pawn.gif"));
        }
    }
    
    private Image loadImage(String set, String filename)
    {
        String path = "/gui/" + set + "/" + filename;
        URL url = getClass().getResource(path);

        if (url == null)
        {
            throw new IllegalArgumentException("Image not found: " + path);
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
        Color lightSqColour =  new Color(223,233,235);
        Color darkSqColour = new Color(88, 195,214);

        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                boolean lightSquare = (x + y) % 2 == 1;

                g.setColor(lightSquare
                        ? lightSqColour
                        : darkSqColour);

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
        drawPieces(g);
    }
}