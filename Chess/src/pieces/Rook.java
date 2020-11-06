package pieces;

import main.Board;
import main.Game;
import main.Side;
import main.Square;
import util.Vector2;

public class Rook extends Piece {

	public Rook(Side side, int x, int y) {
		super(side, x, y);
		type = Type.ROOK;
		loadImage();
	}

	// Calculates all valid moves
	public void calcValidMoves() {
		super.calcValidMoves();

		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				if (y == 0 ^ x == 0) {
					rays: for (int z = 1; z < Math.max(Game.BOARD_WIDTH, Game.BOARD_HEIGHT); z++) {
						Vector2 pos = new Vector2(position.getX() + (x * z), position.getY() + (y * z));
						if (Board.isInBounds(pos)) {
							Square square = Board.getSquareOnPosition(pos);
							if (square.getPiece() == null) {
								validMoves.add(square);
							} else if (square.getPiece().getSide() != side) {
								validMoves.add(square);
								break rays;
							} else {
								break rays;
							}
						}
					}
				}
			}
		}
	}

}
