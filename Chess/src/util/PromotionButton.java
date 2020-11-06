package util;

import java.awt.Image;

import pieces.Type;

public class PromotionButton {

	private Image image;
	private Type type;
	private Vector2 pos;

	public PromotionButton(Image img, Type type, int x, int y) {
		image = img;
		this.type = type;
		pos = new Vector2(x, y);
	}
}
