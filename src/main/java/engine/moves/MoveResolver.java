package engine.moves;

import engine.*;
import engine.board.Board;
import engine.board.Square;
import engine.pieces.Colour;
import engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveResolver
{
    public static MoveResolutionResult resolve(
            Board board,
            MoveRequest request,
            Colour colour)
    {
        // Covert MoveRequest to a Move determining Source piece and square
        // return an error if we cannot fully resolve the move

        if (board == null)
            return MoveResolutionResult.error("Board cannot be null");

        if (request == null)
            return MoveResolutionResult.error("Move request cannot be null");

        if (colour == Colour.NONE)
            return MoveResolutionResult.error("Colour must be specified");


        // Castling does not require source-piece resolution.
        if (request.isCastleKingSide())
        {
            return resolveCastleKingSide(board, request, colour);
        }

        if (request.isCastleQueenSide())
        {
            return resolveCastleQueenSide(board, request, colour);
        }

        // If the source square was explicitly specified,
        // use it directly.
        if (request.hasStartPosition())
        {
            return resolveExplicitSource(board, request, colour);
        }

        // Otherwise find the piece that can make the move.
        return resolveImplicitSource(board, request, colour);

    }

    private static MoveResolutionResult resolveCastleKingSide(Board board, MoveRequest request, Colour colour)
    {
        Move move = Move.fromMoveRequest(request, colour);

        move.setCastleKingSide(true);

        if(colour == Colour.WHITE){
            move.setStartPosition( new Square(4,0));
            move.setEndPosition( new Square(6,0));
        }
        else
        {
            move.setStartPosition( new Square(4,7));
            move.setEndPosition( new Square(6,7));
        }

        return MoveResolutionResult.success(move);
    }

    private static MoveResolutionResult resolveCastleQueenSide(Board board, MoveRequest request, Colour colour)
    {
        Move move = Move.fromMoveRequest(request, colour);

        move.setCastleQueenSide(true);

        if(colour == Colour.WHITE){
            move.setStartPosition( new Square(4,0));
            move.setEndPosition( new Square(2,0));
        }
        else
        {
            move.setStartPosition( new Square(4,7));
            move.setEndPosition( new Square(2,7));
        }

        return MoveResolutionResult.success(move);
    }

    private static MoveResolutionResult resolveExplicitSource(Board board, MoveRequest request, Colour colour)
    {
        Move move = Move.fromMoveRequest(request, colour);

        return MoveResolutionResult.success(move);
    }

    private static MoveResolutionResult resolveImplicitSource(Board board, MoveRequest request, Colour colour)
    {
        Move resolvedMove = null;
        List<Piece> pieceList = board.getPiecesForColour(colour);

        for(Piece piece : pieceList)
        {
            if(piece.Rank() == request.getRank())
            {
                if(request.getDisambiguationFile() != null)
                {
                    if(piece.getPosition().getFile() != request.getDisambiguationFile())
                    {
                        continue;
                    }
                }

                if(request.getDisambiguationRank() != null)
                {
                    if(piece.getPosition().getRank() != request.getDisambiguationRank())
                    {
                        continue;
                    }
                }

                List<Move> moves = new ArrayList<>();

                // Get the moves for the piece
                piece.GetMoves(board, moves, false);

                for (Move currMove : moves)
                {
                    if (request.getEndPosition().equals(currMove.getEndPosition())
                            && (request.getPromoteTo() == currMove.getPromoteTo()))
                    {
                        if (resolvedMove == null)
                        {
                            resolvedMove = currMove;
                        }
                        else
                        {
                            return MoveResolutionResult.error("Move is ambiguous");
                        }
                    }
                }
            }
        }

        if(resolvedMove == null)
        {
            return MoveResolutionResult.error("Move is not valid for this piece");
        }

        return MoveResolutionResult.success(resolvedMove);
    }

}