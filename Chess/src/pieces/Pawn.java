package pieces;

import main.Board;
import main.Side;
import util.Vector2;

public class Pawn extends Piece {

	public Pawn(Side side, int x, int y) {
		super(side, x, y);
		type = Type.PAWN;
		loadImage();
	}

	// Calculates all valid moves
	public void calcValidMoves() {
		super.calcValidMoves();

		int forward = (side == Side.BLACK) ? 1 : -1;
		Vector2 newPos = new Vector2(position.getX(), position.getY() + forward);

		// move forward
		if (Board.isInBounds(newPos) && Board.isPositionEmpty(newPos)) {
			validMoves.add(Board.getSquareOnPosition(newPos));
			newPos = new Vector2(newPos.getX(), newPos.getY() + forward);
			if (timesMoved == 0 && Board.isInBounds(newPos) && Board.isPositionEmpty(newPos)) {
				validMoves.add(Board.getSquareOnPosition(newPos));
			}
		}

		// capturing
		for (int i = -1; i < 2; i += 2) {
			newPos = new Vector2(position.getX() + i, position.getY() + forward);
			if (Board.isInBounds(newPos) && !Board.isPositionEmpty(newPos)
					&& Board.getPieceOnPosition(newPos).getSide() != side) {
				validMoves.add(Board.getSquareOnPosition(newPos));
			}
		}
	}
}
