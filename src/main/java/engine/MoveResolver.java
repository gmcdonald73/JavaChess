package engine;

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
            return MoveResolutionResult.error("King side castle not implemented");
        }

        if (request.isCastleQueenSide())
        {
            return MoveResolutionResult.error("Queen side castle not implemented");
        }

        // If the source square was explicitly specified,
        // use it directly.
        if (request.hasStartPosition())
        {
            return resolveExplicitSource(board, request, colour);
        }

        // Otherwise find the piece that can make the move.
//        return resolveImplicitSource(board, request, colour);
        return MoveResolutionResult.error("resolveImplicitSource not implemented");

    }

    private static MoveResolutionResult resolveExplicitSource(Board board, MoveRequest request, Colour colour)
    {
        Move move = Move.fromMoveRequest(request, colour);

        return MoveResolutionResult.success(move);
    }


}