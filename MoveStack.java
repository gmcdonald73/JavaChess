class MoveStack
{
	private int _numMoves;
	private Move[] _move; 
	private int _moveStackSize;

	public int getNumMoves() { return _numMoves; }

	public Move getMove(int idx) { return _move[idx]; }

	public MoveStack(int size)
	{
		_numMoves = 0;
		_moveStackSize = size;
		_move = new Move[size];
	}

	public void Clear()
	{
		_numMoves = 0;
	}

	public void AddMove(Move move)
	{
		assert(_numMoves < _moveStackSize);

		_move[_numMoves] = move;

		_numMoves++;
	}
}

