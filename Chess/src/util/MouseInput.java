package util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.Board;
import main.Game;
import main.GameState;

public class MouseInput extends MouseAdapter {

	/**
	 * Converts screen position to board position, checks if it's on the board and
	 * calls move on Board
	 */
	public void mousePressed(MouseEvent e) {
		Vector2 click = new Vector2(Math.round(e.getX() / Game.SQUARE_SIZE), Math.round(e.getY() / Game.SQUARE_SIZE));

		if (click.getX() < Game.BOARD_WIDTH && click.getY() < Game.BOARD_HEIGHT) {
			if (Game.state == GameState.PLAYING) {
				Board.move(click);
			} else if (Game.state == GameState.PROMOTING) {
				int x = Math.round(e.getX() / (Game.SQUARE_SIZE * (Game.BOARD_WIDTH / 2)));
				int y = Math.round(e.getY() / (Game.SQUARE_SIZE * (Game.BOARD_HEIGHT / 2)));
				Game.getPlayerByColor(Game.playersTurn).getPromotionChoice(x, y);
			}
		}
	}
}
