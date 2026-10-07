package engine.moves;

import engine.*;
import engine.board.Board;
import engine.board.Square;
import engine.pieces.Colour;
import engine.pieces.Piece;

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

        move._bCastleKingSide = true;

        if(colour == Colour.WHITE){
            move._startPosition = new Square(4,0);
            move._endPosition = new Square(6,0);
        }
        else
        {
            move._startPosition = new Square(4,7);
            move._endPosition = new Square(6,7);
        }

        return MoveResolutionResult.success(move);
    }

    private static MoveResolutionResult resolveCastleQueenSide(Board board, MoveRequest request, Colour colour)
    {
        Move move = Move.fromMoveRequest(request, colour);

        move._bCastleQueenSide = true;

        if(colour == Colour.WHITE){
            move._startPosition = new Square(4,0);
            move._endPosition = new Square(2,0);
        }
        else
        {
            move._startPosition = new Square(4,7);
            move._endPosition = new Square(2,7);
        }

        return MoveResolutionResult.success(move);
    }

    private static MoveResolutionResult resolveExplicitSource(Board board, MoveRequest request, Colour colour)
    {
        Move move = Move.fromMoveRequest(request, colour);

        return MoveResolutionResult.success(move);
    }

    // !!! need to handle disambiguation in this method
    private static MoveResolutionResult resolveImplicitSource(Board board, MoveRequest request, Colour colour)
    {
        Move resolvedMove = null;

        for(int pieceNum = 0; pieceNum <16; pieceNum++)
        {

            if(board.IsPieceAlive(colour, pieceNum))
            {
                Piece piece = board.getPiece(colour, pieceNum);

                if(piece.Rank(board) == request.getRank())
                {
                    MoveStack moves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

                    // Get the moves for the piece
                    piece.GetMoves(board, moves, false);
                    int numMoves = moves.getNumMoves();

                    for(int i=0; i<numMoves; i++)
                    {
                        Move currMove;

                        currMove = moves.getMove(i);

                        // !!! Where does this belong?
                        currMove._startPosition = Square.fromRelCoords(currMove._colour, currMove._startRX, currMove._startRY);
                        currMove._endPosition = Square.fromRelCoords(currMove._colour, currMove._endRX, currMove._endRY);

                        // !!! should also check promotionTo
                        if(request.getEndPosition().equals(currMove._endPosition))
                        {
                            if(resolvedMove == null)
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

        }

        if(resolvedMove == null)
        {
            return MoveResolutionResult.error("Move is not valid for this piece");
        }

        return MoveResolutionResult.success(resolvedMove);
    }

}