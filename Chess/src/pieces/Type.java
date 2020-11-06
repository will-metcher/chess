package pieces;

import java.util.ArrayList;

public enum Type {

	PAWN(1), KNIGHT(3), BISHOP(3), ROOK(5), QUEEN(9), KING(0);

	private int value;

	/**
	 * Type constructor
	 * 
	 * @param value The point value of the Piece
	 */
	private Type(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Returns an ArrayList containing the Types of Pieces that a Pawn can be
	 * promoted to
	 * 
	 * @return ArrayList of 'promotable' Piece types
	 */
	public static ArrayList<Type> getPromotableTypes() {
		ArrayList<Type> types = new ArrayList<Type>();
		for (Type type : Type.values()) {
			if (type.getValue() > 1) {
				types.add(type);
			}
		}
		return types;
	}
}
