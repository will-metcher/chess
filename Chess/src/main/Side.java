package main;

public enum Side {

	BLACK, WHITE;

	/**
	 * Returns the opposite color
	 * 
	 * @param side The starting side
	 * @return The opposite color
	 */
	public static Side getOtherSide(Side side) {
		if (side == Side.BLACK) {
			return side.WHITE;
		}
		return side.BLACK;
	}
}
