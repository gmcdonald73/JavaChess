package engine;

import engine.board.Board;
import engine.pieces.Colour;
import engine.players.BotPlayer;
import engine.players.ChessPlayer;
import engine.players.PlayerAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MateTests
{

    @Test
    void backRankMateIn1()
    {
        Board board = new Board();

        board.setupFromFEN("6k1/5ppp/8/8/8/6K1/8/3R4 w - - 0 1");

        ChessPlayer playerA = new BotPlayer("Bot");
        playerA.setColour(Colour.WHITE);
        PlayerAction action = playerA.getAction(board, null);

        assertEquals("d8", action.getMoveRequest().getEndPosition().toSAN());
    }

}
