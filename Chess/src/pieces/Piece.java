package pieces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Side;
import main.Square;
import util.Vector2;

public abstract class Piece {

	protected int timesMoved = 0;

	protected Type type;
	protected Side side;

	private Image img;
	protected ArrayList<Square> validMoves;
	protected Square square;
	protected Vector2 position;

	protected boolean selected = false;

	/**
	 * Piece constructor
	 * 
	 * @param side The color of the piece
	 * @param x    The starting file
	 * @param y    The starting rank
	 */
	public Piece(Side side, int x, int y) {
		validMoves = new ArrayList<Square>();
		this.side = side;
		position = new Vector2(x, y);
	}

	/**
	 * Loads the correct image for the piece type and side
	 */
	protected void loadImage() {
		String imageName = side.toString().charAt(0) + type.toString() + ".png";
		String path = getClass().getClassLoader().getResource(imageName).toString().substring(6);
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Empties validMoves before recalculating valid moves
	 */
	public void calcValidMoves() {
		validMoves.clear();
	}

	/**
	 * Checks whether or not a square is a valid move for the piece
	 * 
	 * @param square The square to check
	 * @return true if the array contains square and false if it does not
	 */
	public boolean isValidMove(Square square) {
		if (validMoves.contains(square)) {
			return true;
		}
		return false;
	}

	// img getter
	public Image getImage() {
		return this.img;
	}

	// Adds a move to the piece
	public void addMove() {
		this.timesMoved++;
	}

	// Removes a move from the piece
	public void removeMove() {
		this.timesMoved--;
	}

	// validMoves getter
	public ArrayList<Square> getValidMoves() {
		return this.validMoves;
	}

	// side getter
	public Side getSide() {
		return this.side;
	}

	// type getter
	public Type getType() {
		return this.type;
	}

	// value getter
	public int getValue() {
		return type.getValue();
	}

	// position getter
	public Vector2 getPosition() {
		return position;
	}

	// position setter
	public void setPosition(Vector2 pos) {
		position = new Vector2(pos);
	}

	// position.x getter
	public int getX() {
		return position.getX();
	}

	// position.y getter
	public int getY() {
		return position.getY();
	}

	public boolean isSelected() {
		return selected;
	}

	public void select() {
		selected = true;
	}

	public void deselect() {
		selected = false;
	}
}