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
			return Side.WHITE;
		}
		return Side.BLACK;
	}
}
