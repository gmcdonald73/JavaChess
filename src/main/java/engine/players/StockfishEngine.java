package engine.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class StockfishEngine implements AutoCloseable
{
    private final Process process;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public StockfishEngine(String executablePath)
            throws IOException
    {
        process = new ProcessBuilder(executablePath)
                .redirectErrorStream(true)
                .start();

        reader = new BufferedReader(
                new InputStreamReader(
                        process.getInputStream()));

        writer = new BufferedWriter(
                new OutputStreamWriter(
                        process.getOutputStream()));

        sendCommand("uci");
        waitFor("uciok");

        sendCommand("isready");
        waitFor("readyok");
    }

    private void sendCommand(String command)
            throws IOException
    {
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    private String waitFor(String expected)
            throws IOException
    {
        String line;

        while ((line = reader.readLine()) != null)
        {
            if (line.equals(expected))
                return line;
        }

        throw new IOException(
                "Stockfish terminated before: " + expected);
    }

    public String getBestMove(
            List<String> moveHistory,
            int depth) throws IOException
    {
        StringBuilder position =
                new StringBuilder("position startpos");

        if (!moveHistory.isEmpty())
        {
            position.append(" moves ");

            position.append(
                    String.join(" ", moveHistory));
        }

        sendCommand(position.toString());
        sendCommand("go depth " + depth);

        String line;

        while ((line = reader.readLine()) != null)
        {
            if (line.startsWith("bestmove "))
            {
                String[] parts = line.split("\\s+");

                return parts[1];
            }
        }

        throw new IOException(
                "Stockfish terminated before returning a move");
    }

    @Override
    public void close() throws IOException
    {
        sendCommand("quit");

        try
        {
            process.waitFor();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
        }

        reader.close();
        writer.close();
    }
}