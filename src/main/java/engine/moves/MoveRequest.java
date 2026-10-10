package engine.moves;

import engine.board.Board;
import engine.board.Square;
import engine.board.posType;
import engine.board.relPosType;
import engine.pieces.PieceRank;

public class MoveRequest
{
    private final PieceRank _rank;
    private Square _startPosition;
    private final Square _endPosition;
    private boolean _isCapture;
    private Integer _disambiguationRank;
    private Character _disambiguationFile;
    private PieceRank _promoteTo;
    private boolean _castleKingSide;
    private boolean _castleQueenSide;

    public MoveRequest(
            PieceRank rank,
            Square endPosition)
    {
        _rank = rank;
        _startPosition = null;
        _endPosition = endPosition;
        _isCapture = false;
        _disambiguationRank = null;
        _disambiguationFile = null;
        _promoteTo = PieceRank.NONE;
        _castleKingSide = false;
        _castleQueenSide = false;
    }

    public static MoveRequest fromMove(Move move)
    {

        // Convert move start pos from rel to abs and create startPosition
        Square startPosition = Square.fromRelCoords(move._colour, move._startRX, move._startRY);
        Square endPosition = Square.fromRelCoords(move._colour, move._endRX, move._endRY);

        MoveRequest request = new MoveRequest(
                move._rank,
                endPosition );

        request.setStartPosition(startPosition);
        request.setIsCapture(move._bIsCapture);
        request.setPromoteTo(move._promoteTo);
        request.setCastleKingSide(move._bCastleKingSide);
        request.setCastleQueenSide(move._bCastleQueenSide);

        return request;
    }

    public PieceRank getRank() { return _rank; }
    public Square getEndPosition() { return _endPosition; }

    public Square getStartPosition() { return _startPosition; }
    public void setStartPosition(Square startPosition) { _startPosition = startPosition; }

    public boolean isCapture() { return _isCapture; }
    public void setIsCapture(boolean isCapture) { _isCapture = isCapture; }

    public Integer getDisambiguationRank() { return _disambiguationRank; }
    public void setDisambiguationRank(Integer disambiguationRank) { _disambiguationRank = disambiguationRank; }

    public Character getDisambiguationFile() { return _disambiguationFile; }
    public void setDisambiguationFile(Character disambiguationFile) { _disambiguationFile = disambiguationFile; }

    public PieceRank getPromoteTo() { return _promoteTo; }
    public void setPromoteTo(PieceRank promoteTo) { _promoteTo = promoteTo; }

    public boolean isCastleKingSide() { return _castleKingSide; }
    public void setCastleKingSide(boolean castleKingSide) { _castleKingSide = castleKingSide; }

    public boolean isCastleQueenSide() { return _castleQueenSide; }
    public void setCastleQueenSide(boolean castleQueenSide) { _castleQueenSide = castleQueenSide; }

    public boolean hasStartPosition() { return _startPosition != null; }
}