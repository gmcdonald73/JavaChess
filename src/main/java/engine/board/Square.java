package engine.board;

import engine.pieces.Colour;

import java.util.Objects;

public class Square
{
    private final int _x;
    private final int _y;

    public Square(int x, int y)
    {
        _x = x;
        _y = y;
    }

    public static Square fromSAN(String san)
    {
        int x,y;

        x = san.charAt(0) - 'a';
        y = san.charAt(1) - '1';

        return new Square(x,y);
    }

    public String toSAN()
    {
        String pos = String.valueOf(getFile());
        pos += getRank();
        return pos;
    }

    public static Square fromRelCoords(Colour colour, int rx, int ry)
    {
        int x = -1, y = -1;

        if(colour == Colour.WHITE)
        {
            x = rx - 1;
            y = ry - 1;
        }
        else if(colour == Colour.BLACK)
        {
            x = 8 - rx;
            y = 8 - ry;
        }
        return new Square(x,y);
    }

    public int getX()
    {
        return _x;
    }

    public int getY()
    {
        return _y;
    }

    public int getRank() { return _y + 1; }

    public Character getFile() { return (char) (_x + 'a'); }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof Square other))
            return false;

        return _x == other._x
                && _y == other._y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_x, _y);
    }
}