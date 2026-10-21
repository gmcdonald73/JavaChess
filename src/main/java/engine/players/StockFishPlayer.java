package engine.players;

import engine.board.Board;
import engine.board.Square;
import engine.moves.Move;
import engine.moves.MoveRequest;
import engine.pieces.Colour;
import engine.pieces.Piece;
import engine.pieces.PieceRank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockFishPlayer extends ChessPlayer
    implements AutoCloseable
{
    StockfishEngine _engine;

    private static final Path STOCKFISH_PATH =
            Path.of(
                    "engines",
                    "stockfish",
                    "stockfish-windows-x86-64-universal.exe");


    public StockFishPlayer(String name, Colour colour)
    {
        super(name, colour);

        assertTrue(
                Files.isRegularFile(STOCKFISH_PATH),
                "Stockfish executable not found: "
                        + STOCKFISH_PATH.toAbsolutePath());

        try
        {
            _engine = new StockfishEngine(
                    STOCKFISH_PATH.toAbsolutePath().toString());
        }
        catch (IOException e)
        {
            throw new RuntimeException(
                    "Failed to start Stockfish", e);
        }

    }

    @Override
    public PlayerAction getAction(Board board, List<Move> moveHistory)
    {
        MoveRequest moveRequest;

        // 1. Obtain the game's move history.
        // 2. Ask Stockfish for its best move.
        // 3. Convert the UCI move into your Move representation.
        // 4. Return the Move.

        // Convert move history to a list of UCI moves here
        List<String> uciMoves = moveHistory.stream()
                .map(this::toUciMove)
                .toList();

        String move;

        try
        {
            // Get best move from stockfish
            move = _engine.getBestMove(uciMoves, 1);
        }
        catch (IOException e)
        {
            throw new RuntimeException(
                    "Stockfish getBestMove failed", e);
        }

        assertNotNull(move);
        assertTrue(
                move.matches("[a-h][1-8][a-h][1-8][qrbn]?"),
                "Invalid UCI move returned: " + move);

        System.out.println(
                "Stockfish response : " + move);

        // Convert stockfish response to a move request
        moveRequest = toMoveRequest(move, board);

        return PlayerAction.moveRequest(moveRequest);
    }

    @Override
    public void close() throws IOException
    {
        _engine.close();
    }

    private String toUciMove(Move move)
    {
        StringBuilder uci = new StringBuilder();

        uci.append(move.getStartPosition().toSAN());
        uci.append(move.getEndPosition().toSAN());

        if (move._promoteTo != null)
        {
            uci.append(Character.toLowerCase(move._promoteTo.getSymbol()));
        }

        return uci.toString();
    }

    private MoveRequest toMoveRequest(String uciMove, Board board)
    {
        if (uciMove == null ||
                !uciMove.matches("[a-h][1-8][a-h][1-8][qrbn]?"))
        {
            throw new IllegalArgumentException(
                    "Invalid UCI move: " + uciMove);
        }

        // Convert UCI coordinates to absolute board coordinates.
        String startSquare = uciMove.substring(0, 2);
        String endSquare = uciMove.substring(2, 4);

        Square startPosition = Square.fromSAN(startSquare);
        Square endPosition = Square.fromSAN(endSquare);

        // Determine the moving piece.
        Piece piece = board.WhatIsAtSquare(startPosition);

        if (piece == null)
        {
            throw new IllegalArgumentException(
                    "No piece at UCI start square: " + startSquare);
        }

        MoveRequest request = new MoveRequest(piece.Rank(), endPosition);
        request.setStartPosition(startPosition);

/*
        // Detect captures.
        Piece targetPiece = board.WhatIsAtSquare(endPosition);

        boolean isCapture = targetPiece != null;

        // En passant captures onto an empty destination square.
        if (piece.getRank() == PieceRank.PAWN &&
                startX != endX &&
                targetPiece == null)
        {
            isCapture = true;
        }

        request.setIsCapture(isCapture);

        // Detect castling from the king's two-square move.
        if (piece.getRank() == PieceRank.KING &&
                Math.abs(endX - startX) == 2)
        {
            request.setCastleKingSide(endX > startX);
            request.setCastleQueenSide(endX < startX);
        }
*/

        // Handle promotion.
        if (uciMove.length() == 5)
        {
            PieceRank promoteTo = switch (
                    Character.toLowerCase(uciMove.charAt(4)))
            {
                case 'q' -> PieceRank.QUEEN;
                case 'r' -> PieceRank.ROOK;
                case 'b' -> PieceRank.BISHOP;
                case 'n' -> PieceRank.KNIGHT;
                default -> throw new IllegalArgumentException(
                        "Invalid promotion in UCI move: " + uciMove);
            };

            request.setPromoteTo(promoteTo);
        }

        return request;
    }
}