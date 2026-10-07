package engine;

import engine.moves.MoveParseResult;
import engine.moves.MoveParser;
import engine.moves.MoveRequest;
import engine.pieces.PieceRank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveParserTest {

    @Test
    void parsePawnMove() {
        MoveParseResult mpr = MoveParser.parse("e4");

        // assertions
        assertTrue(mpr.isValid());
        MoveRequest request = mpr.getMoveRequest();

        assertEquals(PieceRank.PAWN, request.getRank());
        assertEquals('e', request.getEndPosition().getFile());
        assertEquals(4, request.getEndPosition().getRank());
        assertFalse(request.isCapture());

        // test capture and that parser recognises b means pawn not bishop
        mpr = MoveParser.parse("bxc6");

        // assertions
        assertTrue(mpr.isValid());
        request = mpr.getMoveRequest();

        assertEquals(PieceRank.PAWN, request.getRank());
        assertEquals('c', request.getEndPosition().getFile());
        assertEquals(6, request.getEndPosition().getRank());
        assertTrue(request.isCapture());
    }
}