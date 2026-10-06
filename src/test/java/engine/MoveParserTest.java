package engine;

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
        assertEquals(4, request.getEndX());
        assertEquals(3, request.getEndY());
        assertFalse(request.isCapture());
    }
}