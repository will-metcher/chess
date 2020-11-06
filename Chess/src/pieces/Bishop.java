package pieces;

import main.Board;
import main.Game;
import main.Side;
import main.Square;
import util.Vector2;

public class Bishop extends Piece {

	public Bishop(Side side, int x, int y) {
		super(side, x, y);
		type = Type.BISHOP;
		loadImage();
	}

	// Calculates all valid moves
	public void calcValidMoves() {
		super.calcValidMoves();

		for (int y = -1; y < 2; y += 2) {
			for (int x = -1; x < 2; x += 2) {
				rays: for (int z = 1; z < Math.max(Game.BOARD_WIDTH, Game.BOARD_HEIGHT); z++) {
					Vector2 pos = new Vector2(position.getX() + (x * z), position.getY() + (y * z));
					if (Board.isInBounds(pos)) {
						Piece p = Board.getPieceOnPosition(pos);
						Square sqr = Board.getSquareOnPosition(pos);
						if (p == null) {
							validMoves.add(sqr);
						} else if (p.getSide() != side) {
							validMoves.add(sqr);
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
