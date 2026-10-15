package io;

import engine.board.Board;
import engine.game.ChessGame;
import engine.moves.*;
import engine.pieces.Colour;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PgnWriter
{
    public static void save(ChessGame game, Path file) throws IOException
    {
        Path parent = file.getParent();

        if (parent != null)
        {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file))
        {
            // PGN headers
            writer.write("[Event \"JavaChess Game\"]");
            writer.newLine();

            writer.write("[Site \"JavaChess\"]");
            writer.newLine();

            String date = LocalDate.now().format(
                    DateTimeFormatter.ofPattern("yyyy.MM.dd")
            );

            writer.write("[Date \"" + date + "\"]");
            writer.newLine();

            writer.write("[White \"" + game.getWhitePlayer().getName() + "\"]");
            writer.newLine();

            writer.write("[Black \"" + game.getBlackPlayer().getName() + "\"]");
            writer.newLine();

            writer.write("[Result \"" + game.getResult().getPgnValue() + "\"]");
            writer.newLine();

            writer.newLine();

            // Moves
            List<Move> moves = game.getMoves();
            Board testBoard = new Board();
            Colour playerColour = Colour.WHITE;
            Move move;
            String sMove;

            testBoard.SetupForGame();

            for (int i = 0; i < moves.size(); i++)
            {
                if (i % 2 == 0)
                {
                    writer.write((i / 2 + 1) + ". ");
                }

                move = moves.get(i);
                sMove = getUnambiguousMove(testBoard, playerColour, move);

                MoveValidationResult mvr = MoveValidator.IsMoveLegal(testBoard, playerColour, move);
                assert mvr.isValid();

                testBoard.MakeMove(move);
                writer.write( sMove + " ");

                playerColour = playerColour.opposite();

            }

            // Result
            writer.write(game.getResult().getPgnValue());
            writer.newLine();
        }
    }

    private static String getUnambiguousMove(Board testBoard, Colour playerColour, Move move)
    {
        String sMove;
        MoveParseResult mpr;
        MoveResolutionResult mrr;

        // Check if move is ambiguous
        sMove = move.getSan();
        mpr = MoveParser.parse(sMove);
        assert mpr.isValid();
        mrr = MoveResolver.resolve(testBoard, mpr.getMoveRequest(), playerColour);
        if(mrr.isSuccess())
            return sMove;

        // If resolve fails, try file disambig
        sMove = move.getSan(true, false);
        mpr = MoveParser.parse(sMove);
        assert mpr.isValid();
        mrr = MoveResolver.resolve(testBoard, mpr.getMoveRequest(), playerColour);
        if(mrr.isSuccess())
            return sMove;

        // Try rank
        sMove = move.getSan(false, true);
        mpr = MoveParser.parse(sMove);
        assert mpr.isValid();
        mrr = MoveResolver.resolve(testBoard, mpr.getMoveRequest(), playerColour);
        if(mrr.isSuccess())
            return sMove;

        // Try both
        sMove = move.getSan(true, true);
        mpr = MoveParser.parse(sMove);
        assert mpr.isValid();
        mrr = MoveResolver.resolve(testBoard, mpr.getMoveRequest(), playerColour);
        if(mrr.isSuccess())
            return sMove;

        // !!! Shouldn't get here, but probably should handle this better
        return "";

    }
}
