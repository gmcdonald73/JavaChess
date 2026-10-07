package engine.moves;

import engine.board.Board;
import engine.board.Square;
import engine.board.posType;
import engine.board.relPosType;
import engine.pieces.PieceRank;

public class MoveRequest
{
    private final PieceRank _rank;
    private final Square _startPosition;
    private final Square _endPosition;
    private final boolean _isCapture;
    private final Character _disambiguation;
    private final PieceRank _promoteTo;
    private final boolean _castleKingSide;
    private final boolean _castleQueenSide;

    public MoveRequest(
            PieceRank rank,
            Square startPosition,
            Square endPosition,
            boolean isCapture,
            Character disambiguation,
            PieceRank promoteTo,
            boolean castleKingSide,
            boolean castleQueenSide)
    {
        _rank = rank;
        _startPosition = startPosition;
        _endPosition = endPosition;
        _isCapture = isCapture;
        _disambiguation = disambiguation;
        _promoteTo = promoteTo == null ? PieceRank.NONE : promoteTo;
        _castleKingSide = castleKingSide;
        _castleQueenSide = castleQueenSide;
    }

    public static MoveRequest fromMove(Move move)
    {
        // !!! Change these - use Square from relcoords

        // Convert move start pos from rel to abs and create startPosition
        relPosType relStart = new relPosType();
        posType absStart = new posType();
        relStart._colour = move._colour;
        relStart._rx = move._startRX;
        relStart._ry = move._startRY;
        Board.ConvertRelToAbsCoords(relStart, absStart);
        Square startPosition = new Square(absStart._x, absStart._y);

        // Convert move end pos from rel to abs and create endPosition
        relPosType relEnd = new relPosType();
        posType absEnd = new posType();
        relEnd._colour = move._colour;
        relEnd._rx = move._endRX;
        relEnd._ry = move._endRY;
        Board.ConvertRelToAbsCoords(relEnd, absEnd);
        Square endPosition = new Square(absEnd._x, absEnd._y);

        return new MoveRequest(
                move._rank,
                startPosition,
                endPosition,
                move._bIsCapture,
                null,
                move._promoteTo,
                move._bCastleKingSide,
                move._bCastleQueenSide
        );
    }

    public PieceRank getRank() { return _rank; }
    public Square getStartPosition() { return _startPosition; }
    public Square getEndPosition() { return _endPosition; }
    public boolean isCapture() { return _isCapture; }
    public Character getDisambiguation() { return _disambiguation; }
    public PieceRank getPromoteTo() { return _promoteTo; }
    public boolean isCastleKingSide() { return _castleKingSide; }
    public boolean isCastleQueenSide() { return _castleQueenSide; }
    public boolean hasStartPosition() { return _startPosition != null; }
}