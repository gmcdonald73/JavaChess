package engine;

import engine.board.Square;
import engine.moves.MoveParseResult;
import engine.moves.MoveParser;
import engine.moves.MoveRequest;
import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveParserTest {

    private final MoveParser parser = new MoveParser();

    @Test
    void parsePawnMove() {
        MoveParseResult mpr = MoveParser.parse("e4");

        // assertions
        assertTrue(mpr.isValid());
        MoveRequest request = mpr.getMoveRequest();

        assertEquals(PieceRank.PAWN, request.getRank());
        assertEquals(Square.fromSAN("e4"), request.getEndPosition());
        assertFalse(request.isCapture());
    }

    // ------------------------------------------------------------
    // Piece moves
    // ------------------------------------------------------------

    @Test
    void parsesKnightMove() {
        MoveRequest request = parser.parse("Nf3").getMoveRequest();

        assertEquals(PieceRank.KNIGHT, request.getRank());
        assertEquals(Square.fromSAN("f3"), request.getEndPosition());
        assertFalse(request.isCapture());
    }

    @Test
    void parsesBishopMove() {
        MoveRequest request = parser.parse("Bc4").getMoveRequest();

        assertEquals(PieceRank.BISHOP, request.getRank());
        assertEquals(Square.fromSAN("c4"), request.getEndPosition());
    }

    @Test
    void parsesRookMove() {
        MoveRequest request = parser.parse("Ra1").getMoveRequest();

        assertEquals(PieceRank.ROOK, request.getRank());
        assertEquals(Square.fromSAN("a1"), request.getEndPosition());
    }

    @Test
    void parsesQueenMove() {
        MoveRequest request = parser.parse("Qd4").getMoveRequest();

        assertEquals(PieceRank.QUEEN, request.getRank());
        assertEquals(Square.fromSAN("d4"), request.getEndPosition());
    }

    @Test
    void parsesKingMove() {
        MoveRequest request = parser.parse("Kf2").getMoveRequest();

        assertEquals(PieceRank.KING, request.getRank());
        assertEquals(Square.fromSAN("f2"), request.getEndPosition());
    }

    // ------------------------------------------------------------
    // Captures
    // ------------------------------------------------------------

    @Test
    void parsesPawnCapture() {
        MoveRequest request = parser.parse("bxc6").getMoveRequest();

        assertEquals(PieceRank.PAWN, request.getRank());
        assertTrue(request.isCapture());
        assertEquals('b', request.getDisambiguationFile());
        assertEquals(Square.fromSAN("c6"), request.getEndPosition());
    }

    @Test
    void parsesPieceCapture() {
        MoveRequest request = parser.parse("Bxc6").getMoveRequest();

        assertEquals(PieceRank.BISHOP, request.getRank());
        assertTrue(request.isCapture());
        assertEquals(Square.fromSAN("c6"), request.getEndPosition());
    }

    @Test
    void parsesKnightCapture() {
        MoveRequest request = parser.parse("Nxf3").getMoveRequest();

        assertEquals(PieceRank.KNIGHT, request.getRank());
        assertTrue(request.isCapture());
        assertEquals(Square.fromSAN("f3"), request.getEndPosition());
    }

    // ------------------------------------------------------------
    // Disambiguation
    // ------------------------------------------------------------

    @Test
    void parsesFileDisambiguation() {
        MoveRequest request = parser.parse("Nbd2").getMoveRequest();

        assertEquals(PieceRank.KNIGHT, request.getRank());
        assertEquals('b', request.getDisambiguationFile());
        assertEquals(Square.fromSAN("d2"), request.getEndPosition());
    }

    @Test
    void parsesRankDisambiguation() {
        MoveRequest request = parser.parse("N1d2").getMoveRequest();

        assertEquals(PieceRank.KNIGHT, request.getRank());
        assertEquals(1, request.getDisambiguationRank());
        assertEquals(Square.fromSAN("d2"), request.getEndPosition());
    }

    @Test
    void parsesFullSquareDisambiguation() {
        MoveRequest request = parser.parse("Nb1d2").getMoveRequest();

        assertEquals(PieceRank.KNIGHT, request.getRank());
        assertEquals('b', request.getDisambiguationFile());
        assertEquals(1, request.getDisambiguationRank());
        assertEquals(Square.fromSAN("d2"), request.getEndPosition());
    }

    @Test
    void parsesFileDisambiguationWithCapture() {
        MoveRequest request = parser.parse("Nbxd2").getMoveRequest();

        assertEquals(PieceRank.KNIGHT, request.getRank());
        assertEquals('b', request.getDisambiguationFile());
        assertTrue(request.isCapture());
        assertEquals(Square.fromSAN("d2"), request.getEndPosition());
    }

}