package engine;

public class MoveValidator
{

    public static MoveValidationResult IsMoveLegal(Board board, Colour colour, Move move)
    {
        // Determine piece
        pieceDescriptionType pieceDescrip;
        pieceDescrip = board.WhatIsAtSquare(move._startPosition);
        String errorMessage;

        if (pieceDescrip._rank == PieceRank.NONE )
        {
            errorMessage = "Illegal move - there is no piece at " + move.StartPos();
            return new MoveValidationResult(false, errorMessage);
        }

        if (pieceDescrip._rank != move._rank )
        {
            errorMessage = "Illegal move - piece at " + move.StartPos() + " is not a " + move._rank.getSymbol(); // ConvertRankToSymbol(move._rank);
            return new MoveValidationResult(false, errorMessage);
        }

        if (pieceDescrip._colour != colour )
        {
            errorMessage = "Illegal move - piece at " + move.StartPos() + " is not yours";
            return new MoveValidationResult(false, errorMessage);
        }

        move._number = pieceDescrip._number;

        Piece piece = board.getPiece(colour, pieceDescrip._number);

        if(piece == null)
        {
            // Couldn't find piece that move is referring to
            errorMessage = "Internal error - null piece pointer";
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
            currMove._startPosition = Square.fromRelCoords(currMove._colour, currMove._startRX, currMove._startRY);
            currMove._endPosition = Square.fromRelCoords(currMove._colour, currMove._endRX, currMove._endRY);

            if( move.equals(currMove))
            {
                // Found the move in the list of possible moves for the piece

                // Update the move with the one from the moveStack so other flags eg bIsCapture etc.
                // are set correctly
                move.Copy(currMove);

                // Now check if the move would place our king in check

                // Copy the board to a test board
                // Make a copy so that the original board is not modified
                Board testBoard = board.copy();

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
