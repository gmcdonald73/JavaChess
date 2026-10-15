package engine;

public final class Constants
{
    public static final int NUM_MOVES_LOOKAHEAD = 2; // This is actually the number of half moves. This number should be even
    public static final int MAX_MOVES_LOOKAHEAD = NUM_MOVES_LOOKAHEAD;
    public static final String GUIJAVACHESS_VERSION = "1.0";


    // Move Score
    public static final int MS_UNDEFINED = -1;
    public static final int MS_ILLEGAL = -2;
    public static final int MS_LOSE = 0;
    public static final int MS_DRAW = 500;
    public static final int MS_WIN	= 1000;

}
