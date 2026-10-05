package engine;

public class BotPlayer extends ChessPlayer {

    public BotPlayer(String name, Colour colour) {
        super(name, colour);
    }

    @Override
    public PlayerAction getAction(Board board) {

        // Calculate move

        Move[] engineMoves =
                new Move[Constants.MAX_MOVES_LOOKAHEAD];

        for (int i = 0; i < engineMoves.length; i++) {
            engineMoves[i] = new Move();
        }

        FindBestMove(
                board,
                Constants.NUM_MOVES_LOOKAHEAD,
                engineMoves);

        return PlayerAction.move(engineMoves[0]);
    }

    // Do a breadth first search, look 2 moves ahead for win or lose, then 4 moves ahead etc.
    public boolean FindBestMove(Board board, int lookAheadMax, Move moveStack[])
    {
        // Sneaky stuff, take a copy so that the original board is not modified.
        // This effectively simulates pass by value for board.
        // !!! do we need to do this here, or just in RecursiveFindBestMove?
        Board testBoard = board.copy();

        int numMovesToLookAhead = 2;

        while(numMovesToLookAhead <= lookAheadMax)
        {
            if( !RecursiveFindBestMove(getColour(), testBoard, 0, numMovesToLookAhead, moveStack) )
            {
                // no legal move
                return false;
            }

            // !!! Should also terminate here for obvious stalemate / other draw
            if( (moveStack[0]._score == Constants.MS_WIN) || (moveStack[0]._score == Constants.MS_LOSE) )
            {
                return true;
            }

            numMovesToLookAhead += 2;
        }

        return true;
    }

    public boolean RecursiveFindBestMove(Colour colour, Board board, int lookAheadLevel, int numMovesToLookAhead, Move moveStack[])
    {
        // Take a copy so that the original board is not modified.
        // This effectively simulates pass by value for board.
        // !!! Do we need to do this here, or just in EvaluateMove?
        Board testBoard = board.copy();

        boolean bFoundLegalMove = false;
        Move[] tempMoveStack = new Move[Constants.MAX_MOVES_LOOKAHEAD];
        int msi;

        moveStack[lookAheadLevel]._score = Constants.MS_ILLEGAL;
        MoveStack possibleMoves = new MoveStack(Constants.PIECES_PER_SIDE * Constants.MAX_PIECE_POSSIBLE_MOVES);

        int idxMove;

        possibleMoves.Clear();

        // Ask board for all legal moves
        // then loop through each move and rate them
        testBoard.GetMoves(possibleMoves, colour);

        // Loop through all possible moves for the player
        for(idxMove=0; idxMove<possibleMoves.getNumMoves(); idxMove++)
        {
            // Copy the move stack to temp
            for(msi=0; msi<Constants.MAX_MOVES_LOOKAHEAD; msi++)
            {
                tempMoveStack[msi] = new Move();
                tempMoveStack[msi].Copy(moveStack[msi]);
            }

            tempMoveStack[lookAheadLevel].Copy(possibleMoves.getMove(idxMove));
            tempMoveStack[lookAheadLevel]._score = Constants.MS_UNDEFINED;

            // Evaluate the move
            EvaluateMove(colour, tempMoveStack, testBoard, lookAheadLevel, numMovesToLookAhead);

            if(tempMoveStack[lookAheadLevel]._score > moveStack[lookAheadLevel]._score)
            {
                moveStack[lookAheadLevel]._score = tempMoveStack[lookAheadLevel]._score;

                // Copy the temp move stack back
                for(msi=0; msi<Constants.MAX_MOVES_LOOKAHEAD; msi++)
                {
                    moveStack[msi].Copy(tempMoveStack[msi]);
                }

                bFoundLegalMove = true;

                if(moveStack[lookAheadLevel]._score == Constants.MS_WIN)
                {
                    // If the move will win the game for us, then stop looking for a better move because
                    // there isn't any
                    return bFoundLegalMove;
                }
            }
        }

        return bFoundLegalMove;
    }

    public void EvaluateMove(Colour colour, Move moveStack[], Board board, int lookAheadLevel, int numMovesToLookAhead)
    {
        // Take a copy so that the original board is not modified.
        // This effectively simulates pass by value for board.
        Board testBoard = board.copy();

        // Make the move
        testBoard.MakeMove(moveStack[lookAheadLevel]);

        // Determine if my own king is in check (if so then reject this move)
        // !!! should MakeMove be checking if move is illegal?
        if( testBoard.IsKingInCheck(colour) )
        {
            moveStack[lookAheadLevel]._score = Constants.MS_ILLEGAL;
            return;
        }

        // Otherwise move is OK
        boolean bEnemyHasLegalMoves;

        // If we have reached the look ahead level then rate the situation
        if( lookAheadLevel >= (numMovesToLookAhead-1) )
        {
            // Check if the enemy is checkmated or stalemated
            bEnemyHasLegalMoves = testBoard.HasAnyLegalMoves(colour.opposite());

            if( bEnemyHasLegalMoves )
            {
                //	evaluate the position on the board
                moveStack[lookAheadLevel]._score = RateSituation(testBoard, colour); //, moveStack[lookAheadLevel]);

                // Display movestack and score
/*
                String sMoveStack = "";
                for(int x =0; x<=lookAheadLevel; x++){
                    sMoveStack += moveStack[x].Format(0) + ",";
                }
                sMoveStack += " : " + moveStack[lookAheadLevel]._score;
                System.out.println(sMoveStack);
*/
            }
        }
        else
        {
            //	FindBestMove for enemy
            bEnemyHasLegalMoves = RecursiveFindBestMove(colour.opposite(), testBoard, lookAheadLevel + 1, numMovesToLookAhead, moveStack);

            if( bEnemyHasLegalMoves )
            {
                // Found a legal move for the enemy, our score for our move is the inverse of the
                // best move for the enemy after making our move.
                moveStack[lookAheadLevel]._score = Constants.MS_WIN - moveStack[lookAheadLevel + 1]._score;
            }
        }

        // See if we have won or stalemated, also set flags for displaying checks
        if( bEnemyHasLegalMoves )
        {
            if(  testBoard.IsKingInCheck(colour.opposite()) )
            {
                moveStack[lookAheadLevel]._bIsCheck = true;
            }
            else
            {
                moveStack[lookAheadLevel]._bIsCheck = false;
            }
        }
        else
        {
            // Enemy has no legal moves
            if( testBoard.IsKingInCheck(colour.opposite()) )
            {
                // Enemy is checkmated, we win
                moveStack[lookAheadLevel]._score = Constants.MS_WIN;
                moveStack[lookAheadLevel]._bIsCheck = true;
                moveStack[lookAheadLevel]._bIsMate = true;
            }
            else
            {
                // Enemy is stalemated, we draw
                moveStack[lookAheadLevel]._score = Constants.MS_DRAW;
                moveStack[lookAheadLevel]._bIsCheck = false;
            }
        }
    }

    // RateSituation - return a score between MS_LOSE and MS_WIN indicating how favourable to board
    // position & forces are for calling army.
    // This is where the really intelligence is. Play with this to make computer player smarter
    // This is called after you (this army) has made a legal move, thus if you get here you
    // haven't been checkmated.
    //
    // This currently used a very rough algorithm, which determines a score from yours and your enemies
    // material strength and mobility.
    // NOTE : This routine should NOT modify the board
    // board = current position of board
    // move = last move made to get the board into this state

    // NOTE: move is the last move made (eg if lookahead is even this is the move made by opposite side).
    public int RateSituation(Board board, Colour colour) // , Move move)
    {
        int rating;
        long score, enemyScore;

        score = RateArmy(board, colour);
        enemyScore = RateArmy(board, colour.opposite());

        // Rate position
        rating = (int)((score * Constants.MS_WIN) / (score + enemyScore));

        return rating;
    }

    public long RateArmy(Board board, Colour colour)
    {
        MoveStack moves = new MoveStack(Constants.PIECES_PER_SIDE * Constants.MAX_PIECE_POSSIBLE_MOVES);
        long score;
        // int i;

        // Rate your forces
        board.GetMoves(moves, colour);

        score = moves.getNumMoves();

/*
		// Give bonus points for knight or bishop moves
		if ((move._rank == pieceRankType.PR_KNIGHT) ||
			(move._rank == pieceRankType.PR_BISHOP))
		{
			score += 10;
		}
		for(i=0; i<moves.getNumMoves(); i++)
		{
			int rank;

			rank = moves.getMove(i)._rank;
			if ((rank == pieceRankType.PR_KNIGHT) ||
				(rank == pieceRankType.PR_BISHOP))
			{
				score += 1;
			}
		}
*/

        // Give bonus points for pawn structure
        // Points for pawns in center of board
        // Points for pawns defended by other pawns

        // Loop through pawns
        for(int i=0; i<8; i++)
        {
            if(board.IsPieceAlive(colour, i))
            {
                // Get position of pawn
                relPosType relPos = new relPosType();

                board.WhereIs(colour, i, relPos);

                if( ((relPos._rx == 4) || (relPos._rx == 5)) &&
                        (relPos._ry == 4)  )
                {
                    score += 3;
                }

                // See if pawn is defend by another pawn

                // Check SW pos (relative to pawn)
                pieceDescriptionType pieceDescrip;

                if (relPos._rx > 1)
                {
                    pieceDescrip = board.WhatIsAt(colour, relPos._rx - 1, relPos._ry - 1);

                    if ((pieceDescrip._colour == colour) &&
                            (pieceDescrip._rank == PieceRank.PAWN))
                    {
                        score += 1;
                    }
                }

                if (relPos._rx < 8)
                {
                    // Check SE pos (relative to pawn)
                    pieceDescrip = board.WhatIsAt(colour, relPos._rx + 1, relPos._ry - 1);

                    if ((pieceDescrip._colour == colour) &&
                            (pieceDescrip._rank == PieceRank.PAWN))
                    {
                        score += 1;
                    }
                }
            }
        }

        score += board.MaterialValue(colour) * 10;

        return score;
    }

}