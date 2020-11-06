package pieces;

import main.Board;
import main.Side;
import util.Vector2;

public class Knight extends Piece {

	public Knight(Side side, int x, int y) {
		super(side, x, y);
		type = Type.KNIGHT;
		loadImage();
	}

	// Calculates all valid moves
	public void calcValidMoves() {
		super.calcValidMoves();

		int[] xs = { 2, 1, -1, -2, -2, -1, 1, 2 };
		int[] ys = { 1, 2, 2, 1, -1, -2, -2, -1 };

		for (int i = 0; i < xs.length; i++) {
			Vector2 pos = new Vector2(position.getX() + xs[i], position.getY() + ys[i]);
			if (Board.isInBounds(pos)
					&& (Board.getPieceOnPosition(pos) == null || Board.getPieceOnPosition(pos).getSide() != side)) {
				validMoves.add(Board.getSquareOnPosition(pos));
			}
		}
	}

}
