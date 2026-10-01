package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class HumanPlayer extends ChessPlayer {

    BufferedReader reader;

    public HumanPlayer(String name, int colour) {
        super(name, colour);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Move getMove(Board board) {
        // Get move from console/UI
        Move move = new Move();
        String input = "";

        System.out.print("> ");
        try {
            input = reader.readLine();
        }
        catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }

        InterpretMove(board, input, move);
        return move;
    }

    // !!! Need to do something with errorMessage!
    public boolean InterpretMove(Board board, String sMove, Move move)
    {
        int sourceAX = 0;
        int sourceAY = 0;
        int destAX = 0;
        int destAY = 0;

        int colour = getColour();

        String errorMessage;

        // Set default values
        move._colour = colour;
        move._number = pieceNumberType.PN_NONE;
        move._rank = pieceRankType.PR_NONE;
        move._startRX = -1; // These are relative for each army
        move._startRY = -1;
        move._endRX = -1; // These are relative for each army
        move._endRY = -1;
        move._bIsCapture = false; // Is the move a capture?
        move._promoteTo = pieceRankType.PR_NONE;
        move._bIsCheck = false;
        move._bIsMate = false;
        move._bCastleQueenSide = false;
        move._bCastleKingSide = false;
        move._bEnPassantCapture = false;
        move._score = Constants.MS_UNDEFINED;
        move._bLegalMove = false;

        Piece piece = null;

        if(sMove.compareToIgnoreCase("quit") == 0)
        {
            move._bLegalMove = true;
            return true;
        }

/*
		// This is no longer applicable
		if (sMove.compareToIgnoreCase("help") == 0)
		{
			// Some instructions
			System.out.print("\n*** AsciiChess Help ***\n\n");
			System.out.print("\t<Move> = quit | help | O-O | O-O-O | <Piece><Col><Row><Separator><Col><Row>[<Piece>]\n");
			System.out.print("\t<Piece> = P|R|N|B|Q|K\n");
			System.out.print("\t<Col> = a|b|c|d|e|f|g|h\n");
			System.out.print("\t<Row> = 1|2|3|4|5|6|7|8\n");
			System.out.print("\t<Separator> = x|-\n\n");
			System.out.print("\tExamples\n");
			System.out.print("\tPe2-e4  : Moves white's king pawn from e2 to e4\n");
			System.out.print("\tQa4xg4  : Captures piece on g4 with Queen on a4\n");
			System.out.print("\tO-O     : Castles king side\n");
			System.out.print("\tO-O-O   : Castles queen side\n");
			System.out.print("\tPa7-a8Q : Promotes pawn on a7 to a queen\n");
			System.out.print("\thelp    : This help\n");
			System.out.print("\tquit    : Quits game \n\n");
			System.out.print("\tFor en passant capture, enter the square the opposing pawn moved over. eg 'Pa5xb6'\n");
			System.out.print("\n***********************\n");
			System.out.print("Press any key to continue\n\n");

			String input = ConsoleInput.readLine();
			return false;
		}
*/

        // Check for castling moves
        if( (sMove.compareToIgnoreCase("O-O") == 0) || (sMove.compareToIgnoreCase("O-O-O") == 0))
        {
            move._rank = pieceRankType.PR_KING;

            move._startRY = 1;
            move._endRY = 1;

            if(colour == pieceColourType.PC_WHITE)
            {
                move._startRX = 5;

                if(sMove.compareToIgnoreCase("O-O") == 0)
                {
                    move._endRX = 7;
                    move._bCastleKingSide = true;
                }
                else
                {
                    move._endRX = 3;
                    move._bCastleQueenSide = true;
                }
            }
            else
            {
                move._startRX = 4;

                if(sMove.compareToIgnoreCase("O-O") == 0)
                {
                    move._endRX = 2;
                    move._bCastleKingSide = true;
                }
                else
                {
                    move._endRX = 6;
                    move._bCastleQueenSide = true;
                }
            }
        }
        else
        {
            int moveStringLen;

            moveStringLen = sMove.length();

            if( moveStringLen < 6)
            {
                errorMessage = "Illegal move - move string is too short";
                return false;
            }

            // Breakdown move into component characters
            move._rank = board.ConvertSymbolToRank(sMove.charAt(0));
            sourceAX = sMove.charAt(1) - 'a';
            sourceAY = sMove.charAt(2) - '1';

            if(!board.IsAbsPositionOnBoard(sourceAX, sourceAY))
            {
                errorMessage = "Illegal move - source position is not on the board";
                return false;
            }

            if( sMove.charAt(3) == 'x' )
            {
                move._bIsCapture = true;
            }
            else
            {
                move._bIsCapture = false;
            }

            destAX = sMove.charAt(4) - 'a';
            destAY = sMove.charAt(5) - '1';

            if(!board.IsAbsPositionOnBoard(destAX, destAY))
            {
                errorMessage = "Illegal move - destination position is not on the board";
                return false;
            }

            relPosType startPos = new relPosType();
            board.ConvertAbsToRelCoords(colour, sourceAX, sourceAY, startPos);
            move._startRX = startPos._rx;
            move._startRY = startPos._ry;

            relPosType endPos = new relPosType();
            board.ConvertAbsToRelCoords(colour, destAX, destAY, endPos);
            move._endRX = endPos._rx;
            move._endRY = endPos._ry;

            if( moveStringLen == 7 )
            {
                move._promoteTo = board.ConvertSymbolToRank(sMove.charAt(6));
            }
            else
            {
                move._promoteTo = pieceRankType.PR_NONE;
            }
        }

        // Determine piece
        pieceDescriptionType pieceDescrip;
        pieceDescrip = board.WhatIsAt(colour, move._startRX, move._startRY);

        if (pieceDescrip._rank == pieceRankType.PR_NONE )
        {
            errorMessage = "Illegal move - there is no piece at " + sMove.charAt(1) + sMove.charAt(2);
            return false;
        }

        if (pieceDescrip._rank != move._rank )
        {
            errorMessage = "Illegal move - piece at " + sMove.charAt(1) + sMove.charAt(2) + " is not a " + sMove.charAt(0);
            return false;
        }

        if (pieceDescrip._colour != colour )
        {
            errorMessage = "Illegal move - piece at " + sMove.charAt(1) + sMove.charAt(2) + " is not yours";
            return false;
        }

        move._number = pieceDescrip._number;

/*
        piece = _piece[pieceDescrip._number];

        if(piece == null)
        {
            // Couldn't find piece that move is refering to
            errorMessage = "Internal error - null piece pointer";
            return false;
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

            if( (move._startRX == currMove._startRX) &&
                    (move._startRY == currMove._startRY) &&
                    (move._endRX == currMove._endRX) &&
                    (move._endRY == currMove._endRY) &&
                    (move._promoteTo == currMove._promoteTo))
            {
                // Found the move in the list of possible moves for the piece

                // Update the move with the one from the moveStack so other flags eg bIsCapture etc.
                // are set correctly
                move.Copy(currMove);

                // Now check if the move would place our king in check

                // Copy the board to a test board
                // Make a copy so that the original board is not modified
                Board testBoard = new Board();
                testBoard.Copy(board);

                // Make the move
                testBoard.MakeMove(move);

                if( IsKingInCheck(testBoard) )
                {
                    errorMessage = "Illegal move - this move would place the king in check";
                }
                else
                {
                    move._bLegalMove = true;
                }

                return false;
            }
        }

        errorMessage = "Illegal move - this move is not valid for this piece";
*/
        return false;
    }
}