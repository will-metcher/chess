package pieces;

import main.Board;
import main.Side;
import util.Vector2;

public class King extends Piece {

	private boolean inCheck = false;

	public King(Side side, int x, int y) {
		super(side, x, y);
		type = Type.KING;
		loadImage();
	}

	// Calculates all valid moves
	public void calcValidMoves() {
		super.calcValidMoves();

		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				Vector2 pos = new Vector2(position.getX() + x, position.getY() + y);
				if (Board.isInBounds(pos)) {
					Piece p = Board.getPieceOnPosition(pos);
					if (p == null || p.getSide() != side) {
						validMoves.add(Board.getSquareOnPosition(pos));
					}
				}
			}
		}

		// checks whether king can castle
		if (timesMoved > 0 || inCheck) {
			return;
		}

		int y = 0;
		if (side == Side.WHITE) {
			y = 7;
		}

		int[][] xs = { { 1, 2, 3 }, { 5, 6 } };

		for (int l = 0; l < xs.length; l++) {
			boolean valid = true;

			int r = 0;
			if (l == 1) {
				r = 7;
			}

			if (Board.squares[r][y].getPiece() == null || Board.squares[r][y].getPiece().getType() != Type.ROOK) {
				valid = false;
			}

			for (int i = 0; i < xs[l].length; i++) {
				if (Board.squares[xs[l][i]][y].getPiece() != null) {
					valid = false;
				}

				if (Board.doesEnemyPieceControlSquare(Board.squares[xs[l][i]][y], Side.getOtherSide(side))) {
					valid = false;
				}
			}
			if (valid) {
				int x = 2;
				if (l == 1) {
					x = 6;
				}
				validMoves.add(Board.squares[x][y]);
			}
		}
	}

	// check setter
	public void inCheck(boolean check) {
		this.inCheck = check;
	}

	// check getter
	public boolean isInCheck() {
		return this.inCheck;
	}
}
