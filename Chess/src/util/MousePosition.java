package util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MousePosition extends MouseMotionAdapter {
	private int mouseX = 0;
	private int mouseY = 0;

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}
}
