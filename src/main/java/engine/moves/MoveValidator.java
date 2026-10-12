package engine.moves;

import engine.*;
import engine.board.Board;
import engine.board.Square;
// import engine.board.pieceDescriptionType;
import engine.pieces.Colour;
import engine.pieces.Piece;
import engine.pieces.PieceRank;

public class MoveValidator
{

    public static MoveValidationResult IsMoveLegal(Board board, Colour colour, Move move)
    {
        // Determine piece
        Piece piece;
        piece = board.WhatIsAtSquare(move.getStartPosition());
        String errorMessage;

        if (piece == null )
        {
            errorMessage = "Illegal move - there is no piece at " + move.StartPos();
            return new MoveValidationResult(false, errorMessage);
        }

        if (piece.Rank() != move._rank )
        {
            errorMessage = "Illegal move - piece at " + move.StartPos() + " is not a " + move._rank.getSymbol();
            return new MoveValidationResult(false, errorMessage);
        }

        if (piece.Colour() != colour )
        {
            errorMessage = "Illegal move - piece at " + move.StartPos() + " is not yours";
            return new MoveValidationResult(false, errorMessage);
        }

        int numMoves = 0;
        MoveStack moves = new MoveStack(Constants.MAX_PIECE_POSSIBLE_MOVES);

        // Get the moves for the piece
        piece.GetMoves(board, moves, false);
        numMoves = moves.getNumMoves();

        // Find out if the entered move is one of them
        int i;

        for(i=0; i<numMoves; i++)
        {
            Move currMove;

            currMove = moves.getMove(i);

            // !!! Where does this belong?
            currMove.setStartPosition( Square.fromRelCoords(currMove._colour, currMove._startRX, currMove._startRY));
            currMove.setEndPosition( Square.fromRelCoords(currMove._colour, currMove._endRX, currMove._endRY));

            if( move.equals(currMove))
            {
                // Found the move in the list of possible moves for the piece

                // Update the move with the one from the moveStack so other flags eg bIsCapture etc.
                // are set correctly
                move.Copy(currMove);

                // Now check if the move would place our king in check

                // Copy the board to a test board
                // Make a copy so that the original board is not modified
                Board testBoard = new Board(board);

                // Make the move
                testBoard.MakeMove(move);

                if( testBoard.IsKingInCheck(colour) )
                {
                    errorMessage = "Illegal move - this move would place the king in check";
                    return new MoveValidationResult(false, errorMessage);
                }

                return new MoveValidationResult(true, "");
            }
        }

        errorMessage = "Illegal move - this move is not valid for this piece";
        return new MoveValidationResult(false, errorMessage);

    }

}
