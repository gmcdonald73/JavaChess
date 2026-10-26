package io;

import engine.board.Board;
import engine.moves.*;
import engine.pieces.Colour;
import engine.pieces.PieceRank;

public class SANFormatter
{
    public static String getSAN(Board board, Colour playerColour, Move move)
    {
        String sMove;
        MoveParseResult mpr;
        MoveResolutionResult mrr;

        // Check if move is ambiguous
        sMove = getSanBase(move, false,false);
        mpr = MoveParser.parse(sMove);
        assert mpr.isValid();
        mrr = MoveResolver.resolve(board, mpr.getMoveRequest(), playerColour);

        if(!mrr.isSuccess())
        {
            // If resolve fails, try file disambig
            sMove = getSanBase(move, true, false);
            mpr = MoveParser.parse(sMove);
            assert mpr.isValid();
            mrr = MoveResolver.resolve(board, mpr.getMoveRequest(), playerColour);
        }

        if(!mrr.isSuccess())
        {
            // Try rank
            sMove = getSanBase(move, false, true);
            mpr = MoveParser.parse(sMove);
            assert mpr.isValid();
            mrr = MoveResolver.resolve(board, mpr.getMoveRequest(), playerColour);
        }

        if(!mrr.isSuccess())
        {
            // Try both
            sMove = getSanBase(move, true, true);
            mpr = MoveParser.parse(sMove);
            assert mpr.isValid();
            mrr = MoveResolver.resolve(board, mpr.getMoveRequest(), playerColour);
        }

        if(mrr.isSuccess())
        {

            Board testBoard = new Board(board);

            MoveValidationResult mvr = MoveValidator.IsMoveLegal(testBoard, playerColour, move);
            assert mvr.isValid();
            testBoard.MakeMove(mrr.getMove());

            if(testBoard.IsKingInCheck(playerColour.opposite()))
            {
                if(!testBoard.HasAnyLegalMoves(playerColour.opposite()))
                    sMove += "#";
                else
                    sMove += "+";
            }

            return sMove;
        }

        // !!! Shouldn't get here, but probably should handle this better
        return "";

    }

    private static String getSanBase(Move move, boolean showFileDisambiguation, boolean showRankDisambiguation)
    {
        String san = "";

        if(move.getColour() == Colour.NONE)
        {
            san = "Null Move";
            return san;
        }

        if( move.isCastleKingSide() )
        {
            san += "O-O";
        }
        else if( move.isCastleQueenSide() )
        {
            san += "O-O-O";
        }
        else
        {
            if( move.getRank() != PieceRank.PAWN )
            {
                san += move.getRank().getSymbol();

                if(showFileDisambiguation)
                    san += move.getStartPosition().getFile();

                if(showRankDisambiguation)
                    san += move.getStartPosition().getRank();
            }

            if(move.isCapture())
            {
                // pawn capture need to add source file
                if( move.getRank() == PieceRank.PAWN )
                {
                    san += move.getStartPosition().getFile();
                }

                san += "x";
            }

            // Display destination
            san += move.getEndPosition().toSAN();

            // If a pawn is being promoted, display the piece that it is promoted to
            if( (move.getRank() == PieceRank.PAWN) && (move.getPromoteTo() != null) )
            {
                san += "=" + move.getPromoteTo().getSymbol();
            }
        }

        return san;
    }

}
