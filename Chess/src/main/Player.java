package main;

import java.util.ArrayList;

import pieces.Piece;
import pieces.Type;

public class Player {

	private Side color;
	private ArrayList<Piece> capturedPieces;
	private int score = 0;

	public Player(Side side) {
		this.color = side;
		capturedPieces = new ArrayList<Piece>();
	}

	/**
	 * Captures a piece, adding it to capturedPieces and adding it's value to score
	 * 
	 * @param piece the Piece that was captured
	 */
	public void capturePiece(Piece piece) {
		capturedPieces.add(piece);
		score += piece.getValue();
	}

	/**
	 * Promotes the pawn to the piece selected in the promotion menu
	 * 
	 * @param x The x location in the promotion menu grid
	 * @param y The y location in the promotion menu grid
	 */
	public void getPromotionChoice(int x, int y) {
		Piece p = Board.getPieceByColorAndType(color, Type.getPromotableTypes().get(x + (y * 2))).get(0);
		Board.promote(p);
	}

	/**
	 * score getter
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * capturedPieces getter
	 * 
	 * @return capturedPieces
	 */
	public ArrayList<Piece> getCapturedPieces() {
		return capturedPieces;
	}
}
