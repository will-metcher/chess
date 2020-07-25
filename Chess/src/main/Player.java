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
	 * @param piece the Piece to capture
	 */
	public void capturePiece(Piece piece) {
		capturedPieces.add(piece);
		score += piece.getValue();
	}
	
	public ArrayList<Type> getUniqueTypes() {
		ArrayList<Type> types = new ArrayList<Type>();
		types.add(Type.QUEEN);
		for(Piece p : capturedPieces) {
			if(!types.contains(p.getType())) {
				types.add(p.getType());
			}
		}
		
		return types;
	}
	
	public void getPromotionChoice(int x, int y) {
		Piece p = Board.getPieceByColorAndType(color, getUniqueTypes().get(x + (y*2))).get(0);
		Board.promote(p);
	}

	/**
	 * score getter
	 * 
	 * @return score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * capturedPieces getter
	 * 
	 * @return capturedPieces
	 */
	public ArrayList<Piece> getCapturedPieces() {
		return this.capturedPieces;
	}
}
