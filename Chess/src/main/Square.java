package main;

import java.awt.Color;

import pieces.Piece;

public class Square {
	private Piece piece;
	private Color color;

	/**
	 * Square constructor
	 * 
	 * @param color The color used to render the square
	 */
	public Square(Color color) {
		this.color = color;
	}

	// piece setter
	public void setPiece(Piece p) {
		this.piece = p;
	}

	// piece getter
	public Piece getPiece() {
		return this.piece;
	}

	// color getter
	public Color getColor() {
		return this.color;
	}
}
