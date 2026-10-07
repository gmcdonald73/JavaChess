package engine.board;

import engine.pieces.Colour;
import engine.pieces.PieceRank;

public class pieceDescriptionType
{
	public PieceRank _rank;
	public Colour _colour;
	public int _number;

	public pieceDescriptionType()
	{
	}

	public pieceDescriptionType(pieceDescriptionType other)
	{
		this._rank = other._rank;
		this._colour = other._colour;
		this._number = other._number;
	}
}
