package engine.pieces;

public enum PieceRank {
	KING('K'),
	QUEEN('Q'),
	BISHOP('B'),
	KNIGHT('N'),
	ROOK('R'),
	PAWN('P');

	private final char symbol;

	PieceRank(char symbol) {
		this.symbol = symbol;
	}

	public char getSymbol() {
		return symbol;
	}

	public static PieceRank fromSymbol(char symbol) {
		for (PieceRank rank : values()) {
			if (rank.symbol == Character.toUpperCase(symbol)) {
				return rank;
			}
		}

		throw new IllegalArgumentException(
				"Invalid piece symbol: " + symbol);
	}
}