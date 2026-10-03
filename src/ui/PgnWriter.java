package ui;

import engine.*;

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

            for (int i = 0; i < moves.size(); i++)
            {
                if (i % 2 == 0)
                {
                    writer.write((i / 2 + 1) + ". ");
                }

                writer.write(moves.get(i).getSan() + " ");
            }

            // Result
            writer.write(game.getResult().getPgnValue());
            writer.newLine();
        }
    }
}
