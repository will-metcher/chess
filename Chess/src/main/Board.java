package main;

import java.awt.Color;
import java.util.ArrayList;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Type;
import util.Vector2;

public class Board {

	public static Square[][] squares = new Square[Game.BOARD_WIDTH][Game.BOARD_HEIGHT];
	public static Piece selectedPiece = null;

	private final Color BLACK_SQUARE_COLOR = new Color(10, 100, 85);
	private final Color WHITE_SQUARE_COLOR = new Color(255, 255, 240);

	private static boolean wasKingInCheck = false;

	private static Vector2 promotionPos;

	public Board() {
		for (int x = 0; x < squares.length; x++) {
			for (int y = 0; y < squares[x].length; y++) {
				Color color = BLACK_SQUARE_COLOR;
				if (x % 2 == y % 2) {
					color = WHITE_SQUARE_COLOR;
				}
				squares[x][y] = new Square(color);
			}
		}
		setupBoard();
	}

	/**
	 * Instantiates and adds the correct Pieces to the correct Square
	 */
	public void setupBoard() {
		squares[0][0].setPiece(new Rook(Side.BLACK, 0, 0));
		squares[1][0].setPiece(new Knight(Side.BLACK, 1, 0));
		squares[2][0].setPiece(new Bishop(Side.BLACK, 2, 0));
		squares[3][0].setPiece(new Queen(Side.BLACK, 3, 0));
		squares[4][0].setPiece(new King(Side.BLACK, 4, 0));
		squares[5][0].setPiece(new Bishop(Side.BLACK, 5, 0));
		squares[6][0].setPiece(new Knight(Side.BLACK, 6, 0));
		squares[7][0].setPiece(new Rook(Side.BLACK, 7, 0));

		int y = Game.BOARD_HEIGHT - 1;

		squares[0][y].setPiece(new Rook(Side.WHITE, 0, y));
		squares[1][y].setPiece(new Knight(Side.WHITE, 1, y));
		squares[2][y].setPiece(new Bishop(Side.WHITE, 2, y));
		squares[3][y].setPiece(new Queen(Side.WHITE, 3, y));
		squares[4][y].setPiece(new King(Side.WHITE, 4, y));
		squares[5][y].setPiece(new Bishop(Side.WHITE, 5, y));
		squares[6][y].setPiece(new Knight(Side.WHITE, 6, y));
		squares[7][y].setPiece(new Rook(Side.WHITE, 7, y));

		for (int i = 0; i < Game.BOARD_WIDTH; i++) {
			squares[i][1].setPiece(new Pawn(Side.BLACK, i, 1));
			squares[i][y - 1].setPiece(new Pawn(Side.WHITE, i, y - 1));
		}

		calcAllValidMoves();
	}

	/**
	 * Returns true if the parameter is within the bounds of the board
	 * 
	 * @param vec The Vector2 object to be checked
	 * @return true if vec is within the bounds and false if it is not
	 */
	public static boolean isInBounds(Vector2 vec) {
		if (vec.getX() <= Game.BOARD_WIDTH - 1 && vec.getX() >= 0 && vec.getY() <= Game.BOARD_HEIGHT - 1
				&& vec.getY() >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * Loops through all pieces and calls calcValidMoves()
	 */
	public static void calcAllValidMoves() {
		for (int x = 0; x < squares.length; x++) {
			for (int y = 0; y < squares[0].length; y++) {
				if (squares[x][y].getPiece() != null) {
					squares[x][y].getPiece().calcValidMoves();
				}
			}
		}
	}

	/**
	 * Detecting whether or not the specified king is in currently in check or not
	 * 
	 * @param king The king to be examined
	 * @return true if the king is in check and false if it is not
	 */
	public static boolean isInCheck(King king) {
		for (int x = 0; x < squares.length; x++) {
			for (int y = 0; y < squares[0].length; y++) {
				Piece p = squares[x][y].getPiece();
				if (p != null && p.getSide() != king.getSide()) {
					for (Square sqr : p.getValidMoves()) {
						if (getSquareOnPosition(king.getPosition()).equals(sqr)) {
							king.inCheck(true);
							return true;
						}
					}
				}
			}
		}

		king.inCheck(false);
		return false;
	}

	/**
	 * A method to check whether or not an enemy piece controls the specified square
	 * 
	 * @param square The square which will be checked
	 * @param side   The color of the pieces to check
	 * @return true if a piece of specified color can move onto square and false if
	 *         no pieces can
	 */
	public static boolean doesEnemyPieceControlSquare(Square square, Side side) {
		for (int x = 0; x < squares.length; x++) {
			for (int y = 0; y < squares[0].length; y++) {
				Piece p = squares[x][y].getPiece();
				if (p != null && p.getSide() == side) {
					for (Square sqr : p.getValidMoves()) {
						if (sqr.equals(square)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Returns an ArrayList of all of the pieces of specified color and type
	 * 
	 * @param color The color of the wanted pieces
	 * @param type  The type of the wanted pieces
	 * @return ArrayList contained all matching pieces
	 */
	public static ArrayList<Piece> getPieceByColorAndType(Side color, Type type) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for (int x = 0; x < squares.length; x++) {
			for (int y = 0; y < squares[0].length; y++) {
				Piece p = squares[x][y].getPiece();
				if (p != null && p.getSide() == color && p.getType() == type) {
					pieces.add(p);
				}
			}
		}
		return pieces;
	}

	/**
	 * Checks whether or not the selected move was valid, moves the pieces if it is,
	 * otherwise returns the back to their starting positions
	 * 
	 * @param click The position the player clicked on
	 */
	public static void move(Vector2 click) {
		Piece piece = getPieceOnPosition(click);
		Square clickedSquare = getSquareOnPosition(click);

		if (selectedPiece == null) {
			if (piece != null && piece.getSide() == Game.playersTurn) {
				selectedPiece = piece;
				selectedPiece.select();
			}
			return;
		}
		if (selectedPiece.isValidMove(clickedSquare)) {
			Vector2 startingSquarePos = new Vector2(selectedPiece.getPosition());

			clickedSquare.setPiece(selectedPiece);

			// set starting square's piece to null
			getSquareOnPosition(startingSquarePos).setPiece(null);

			selectedPiece.setPosition(click);
			clickedSquare.setPiece(selectedPiece);

			calcAllValidMoves();

			if (isInCheck((King) getPieceByColorAndType(Game.playersTurn, Type.KING).get(0))) {
				if (piece != null) {
					piece.setPosition(click);
				}
				// undo
				clickedSquare.setPiece(piece);
				selectedPiece.setPosition(startingSquarePos);
				getSquareOnPosition(startingSquarePos).setPiece(selectedPiece);
				selectedPiece.deselect();
				selectedPiece = null;
				calcAllValidMoves();
				return;
			}

			// tried to castle
			if (selectedPiece.getType() == Type.KING) {
				int dif = startingSquarePos.getX() - click.getX();
				if (Math.abs(dif) > 1) {
					if (!wasKingInCheck) {
						int y = 0;
						if (selectedPiece.getSide() == Side.WHITE) {
							y = 7;
						}
						int x = 0;
						int side = 3;
						if (dif < 0) {
							x = 7;
							side = 5;
						}
						Piece rook = squares[x][y].getPiece();
						squares[side][y].setPiece(rook);
						rook.setPosition(new Vector2(side, y));
						squares[x][y].setPiece(null);
						rook.addMove();
					} else {
						selectedPiece.setPosition(startingSquarePos);
						getSquareOnPosition(startingSquarePos).setPiece(selectedPiece);
						getPieceByColorAndType(Side.WHITE, Type.KING).get(0).removeMove();
					}
				}
			}

			if (piece != null) {
				Game.getPlayerByColor(Game.playersTurn).capturePiece(piece);
			}
			selectedPiece.addMove();
			Game.playSound();
			calcAllValidMoves();
			isInCheck((King) getPieceByColorAndType(Game.playersTurn, Type.KING).get(0));
			wasKingInCheck = isInCheck(
					(King) getPieceByColorAndType(Side.getOtherSide(Game.playersTurn), Type.KING).get(0));

			// TODO Allow player to select piece to promote pawn to
			// promotion
			if (selectedPiece.getType() == Type.PAWN) {
				int y = 0;
				if (selectedPiece.getSide() == Side.BLACK) {
					y = 7;
				}
				if (selectedPiece.getY() == y) {
					promotionPos = selectedPiece.getPosition();
					Game.state = GameState.PROMOTING;
				}
			}
			if (Game.state == GameState.PLAYING) {
				Game.switchTurn();
			}
		}
		selectedPiece.deselect();
		selectedPiece = null;

	}

	public static void promote(Piece p) {
		switch (p.getType()) {
		case QUEEN:
			getSquareOnPosition(promotionPos)
					.setPiece(new Queen(Game.playersTurn, promotionPos.getX(), promotionPos.getY()));
			break;
		case ROOK:
			getSquareOnPosition(promotionPos)
					.setPiece(new Rook(Game.playersTurn, promotionPos.getX(), promotionPos.getY()));
			break;
		case KNIGHT:
			getSquareOnPosition(promotionPos)
					.setPiece(new Knight(Game.playersTurn, promotionPos.getX(), promotionPos.getY()));
			break;
		case BISHOP:
			getSquareOnPosition(promotionPos)
					.setPiece(new Bishop(Game.playersTurn, promotionPos.getX(), promotionPos.getY()));
			break;
		}

		promotionPos = null;
		Game.state = GameState.PLAYING;
		Game.switchTurn();
	}

	/**
	 * Returns a Square from squares at the specified index
	 * 
	 * @param pos The index of the wanted Square
	 * @return The square from the array at the specified index
	 */
	public static Square getSquareOnPosition(Vector2 pos) {
		return squares[pos.getX()][pos.getY()];
	}

	/**
	 * Returns the piece on the square at the specified position
	 * 
	 * @param pos The index of the square to get the piece from
	 * @return The pieces on the square at the specified index
	 */
	public static Piece getPieceOnPosition(Vector2 pos) {
		return squares[pos.getX()][pos.getY()].getPiece();
	}

	/**
	 * Checks whether or not their is a piece on the square at the specified
	 * position
	 * 
	 * @param pos The position to check for a piece
	 * @return true if there is a piece and false if there is not
	 */
	public static boolean isPositionEmpty(Vector2 pos) {
		if (getSquareOnPosition(pos).getPiece() == null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the index of a Square in the squares array
	 * 
	 * @param square The square to get the index from
	 * @return The index as a Vector2 if the Square was found in the array,
	 *         otherwise null
	 */
	public static Vector2 getPositionFromSquare(Square square) {
		for (int x = 0; x < squares.length; x++) {
			for (int y = 0; y < squares[0].length; y++) {
				if (squares[x][y].equals(square)) {
					return new Vector2(x, y);
				}
			}
		}
		return null;
	}
}