package engine.moves;

import engine.pieces.PieceRank;
import engine.board.Square;

public class MoveParser
{
    public static MoveParseResult parse(String san)
    {
        if (san == null || san.isBlank())
            return MoveParseResult.error("Move cannot be empty");

        String input = san.trim();

        // Remove SAN check/mate suffix.
        if (input.endsWith("+") || input.endsWith("#"))
            input = input.substring(0, input.length() - 1);

        // Castling
        if (input.equals("O-O") || input.equals("0-0"))
        {
            MoveRequest request = new MoveRequest(PieceRank.KING, null);
            request.setCastleKingSide(true);
            return MoveParseResult.success(request);
        }

        if (input.equals("O-O-O") || input.equals("0-0-0"))
        {
            MoveRequest request = new MoveRequest(PieceRank.KING, null);
            request.setCastleQueenSide(true);
            return MoveParseResult.success(request);
        }

        // Promotion
        PieceRank promoteTo = null;

        int promotionIndex = input.indexOf('=');

        if (promotionIndex >= 0)
        {
            if (promotionIndex != input.length() - 2)
                return MoveParseResult.error("Invalid promotion notation");

            char promotionChar =
                    Character.toUpperCase(input.charAt(promotionIndex + 1));

            promoteTo = pieceRankFromSanCharacter(promotionChar);

            if (promoteTo == null ||
                    promoteTo == PieceRank.KING ||
                    promoteTo == PieceRank.PAWN)
            {
                return MoveParseResult.error("Invalid promotion piece");
            }

            input = input.substring(0, promotionIndex);
        }

        if (input.length() < 2)
            return MoveParseResult.error("Invalid move");

        // Destination must be the final two characters.
        int destinationIndex = input.length() - 2;

        char file = Character.toLowerCase(input.charAt(destinationIndex));
        char rank = input.charAt(destinationIndex + 1);

        if (file < 'a' || file > 'h')
            return MoveParseResult.error("Invalid destination file");

        if (rank < '1' || rank > '8')
            return MoveParseResult.error("Invalid destination rank");

        int endX = file - 'a';
        int endY = rank - '1';

        // Remove destination.
        String prefix = input.substring(0, destinationIndex);

        boolean isCapture = prefix.contains("x");

        // Remove capture indicator.
        prefix = prefix.replace("x", "");

        PieceRank pieceRank = PieceRank.PAWN;
        Character disambiguationFile = null;
        Integer disambiguationRank = null;

        // A piece move starts with K/Q/R/B/N.
        if (!prefix.isEmpty())
        {
            char first = prefix.charAt(0);

            if (Character.isUpperCase(first))
            {
                PieceRank parsedRank = pieceRankFromSanCharacter(first);

                if (parsedRank != null && parsedRank != PieceRank.PAWN)
                {
                    pieceRank = parsedRank;
                    prefix = prefix.substring(1);
                }
            }
        }

        // What remains must be SAN disambiguation.
        if (!prefix.isEmpty())
        {
            if (prefix.length() > 2)
                return MoveParseResult.error("Invalid move disambiguation");

            if(prefix.length() == 2)
            {
                // Disambiguation should be file then rank (e.g. b1)
                char c = Character.toLowerCase(prefix.charAt(0));

                if (!(c >= 'a' && c <= 'h'))
                {
                    return MoveParseResult.error("Invalid move disambiguation");
                }

                disambiguationFile = c;

                c = Character.toLowerCase(prefix.charAt(1));

                if (!(c >= '1' && c <= '8'))
                {
                    return MoveParseResult.error("Invalid move disambiguation");
                }

                disambiguationRank = c - '0';

            }
            else
            {
                // Single character disambiguation could be rank or file

                char c = Character.toLowerCase(prefix.charAt(0));

                if (c >= 'a' && c <= 'h')
                {
                    disambiguationFile = c;
                }
                else if (c >= '1' && c <= '8')
                {
                    disambiguationRank = c - '0';
                }
                else
                {
                    return MoveParseResult.error("Invalid move disambiguation");
                }
            }
        }

        // Pawn captures must contain a source file.
        if (pieceRank == PieceRank.PAWN && isCapture)
        {
            if (disambiguationFile == null)
            {
                return MoveParseResult.error(
                        "Pawn capture must specify source file");
            }
        }

        MoveRequest request = new MoveRequest(
                pieceRank,
                new Square(endX, endY));

        request.setIsCapture(isCapture);
        request.setDisambiguationFile(disambiguationFile);
        request.setDisambiguationRank(disambiguationRank);
        request.setPromoteTo(promoteTo);

        return MoveParseResult.success(request);

    }


    private static PieceRank pieceRankFromSanCharacter(char c)
    {
        return switch (c)
        {
            case 'K' -> PieceRank.KING;
            case 'Q' -> PieceRank.QUEEN;
            case 'R' -> PieceRank.ROOK;
            case 'B' -> PieceRank.BISHOP;
            case 'N' -> PieceRank.KNIGHT;
            default -> null;
        };
    }
}