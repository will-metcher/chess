package util;

public class Vector2 {

	private int x;
	private int y;

	/**
	 * Vector2 constructor
	 * 
	 * @param x Starting x position
	 * @param y Starting y position
	 */
	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2() {
		x = 0;
		y = 0;
	}

	/**
	 * Overloaded Vector2 constructor to create a new object from an existing one
	 * 
	 * @param vec The Vector2 to duplicate
	 */
	public Vector2(Vector2 vec) {
		x = vec.getX();
		y = vec.getY();
	}

	// x getter
	public int getX() {
		return x;
	}

	// y getter
	public int getY() {
		return y;
	}
}
