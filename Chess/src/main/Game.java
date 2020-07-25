package main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import pieces.King;
import pieces.Piece;
import pieces.Type;
import util.MouseInput;
import util.MousePosition;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private final String TITLE = "Chess- ";

	public static final int SQUARE_SIZE = 100;
	public static final int BOARD_WIDTH = 8;
	public static final int BOARD_HEIGHT = 8;
	private final int WIDTH = SQUARE_SIZE * BOARD_WIDTH;
	private final int HEIGHT = SQUARE_SIZE * (BOARD_HEIGHT + 1);
	public static final int CAPTURED_SIZE = 50;

	private boolean running = false;

	private final JFrame frame;
	private Thread thread;
	private static Player white;
	private static Player black;
	private final MousePosition mousePosition;

	// private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

	public static Side playersTurn = Side.WHITE;
	public static GameState state = GameState.PLAYING;
	
	public static String projectPath;

	// Initializes the JFrame and instantiates the MouseListener, Board and Players
	public Game() {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.getContentPane().setMaximumSize(new Dimension(WIDTH, HEIGHT));
		frame.getContentPane().setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		this.addMouseListener(new MouseInput());
		mousePosition = new MousePosition();
		this.addMouseMotionListener(mousePosition);
		frame.add(this);
		frame.setVisible(true);
		
		projectPath = new File("").getAbsolutePath();

		new Board();

		white = new Player(Side.WHITE);
		black = new Player(Side.BLACK);
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		// draw HUD
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// draw board and pieces
		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				g.setColor(Board.squares[x][y].getColor());
				g.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

				if (Board.squares[x][y].getPiece() != null) {
					Piece p = Board.squares[x][y].getPiece();
					if (!p.isSelected()) {
						g.drawImage(p.getImage(), p.getPosition().getX() * SQUARE_SIZE,
								p.getPosition().getY() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
					}
				}
			}
		}

		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(4));

		// draw turn indicator
		int indicatorX = 0;
		int indicatorY = SQUARE_SIZE * BOARD_HEIGHT;

		if (playersTurn == Side.BLACK) {
			indicatorX = SQUARE_SIZE * BOARD_WIDTH / 2;
		}

		g.drawRect(indicatorX, indicatorY, SQUARE_SIZE * (BOARD_WIDTH / 2), SQUARE_SIZE);

		// draw selected pieces on cursor
		if (Board.selectedPiece != null) {
			g.drawImage(Board.selectedPiece.getImage(), mousePosition.getMouseX() - SQUARE_SIZE / 2,
					mousePosition.getMouseY() - SQUARE_SIZE / 2, SQUARE_SIZE, SQUARE_SIZE, null);
		}

		// draw king in check
		King bk = (King) Board.getPieceByColorAndType(Side.BLACK, Type.KING).get(0);
		King wk = (King) Board.getPieceByColorAndType(Side.WHITE, Type.KING).get(0);

		if (bk != null && bk.isInCheck()) {
			g.drawRect(bk.getPosition().getX() * SQUARE_SIZE, bk.getPosition().getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}

		if (wk != null && wk.isInCheck()) {
			g.drawRect(wk.getPosition().getX() * SQUARE_SIZE, wk.getPosition().getY() * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}

		// draw captured pieces for white
		for (int w = 0; w < white.getCapturedPieces().size(); w++) {
			Piece p = white.getCapturedPieces().get(w);
			int x = (w % 8) * CAPTURED_SIZE;
			int y = (int) (SQUARE_SIZE * BOARD_HEIGHT + (SQUARE_SIZE / 2 * Math.floor(w / 8)));
			g.drawImage(p.getImage(), x, y, CAPTURED_SIZE, CAPTURED_SIZE, null);
		}

		// draw captured pieces for black
		for (int b = 0; b < black.getCapturedPieces().size(); b++) {
			Piece p = black.getCapturedPieces().get(b);
			int x = (b % 8 * CAPTURED_SIZE) + (SQUARE_SIZE * BOARD_WIDTH / 2);
			int y = (int) (SQUARE_SIZE * BOARD_HEIGHT + (SQUARE_SIZE / 2 * Math.floor(b / 8)));
			g.drawImage(p.getImage(), x, y, CAPTURED_SIZE, CAPTURED_SIZE, null);
		}

		if (state == GameState.PROMOTING) {
			ArrayList<Type> types = getPlayerByColor(playersTurn).getUniqueTypes();
			for (int i = 0; i < types.size(); i++) {
				int x = i % 2 * BOARD_WIDTH / 2 * SQUARE_SIZE;
				int y = Math.round(i / 2) * BOARD_HEIGHT / 2 * SQUARE_SIZE;
				int sx = BOARD_WIDTH / 2 * SQUARE_SIZE;
				int sy = BOARD_HEIGHT / 2 * SQUARE_SIZE;
				g.setColor(Color.DARK_GRAY);
				g.fillRect(x, y, sx, sy);
				g.drawImage(Board.getPieceByColorAndType(playersTurn, types.get(i)).get(0).getImage(), x, y, sx, sy,
						null);
			}
		}

		g.dispose();
		bs.show();

	}

	/**
	 * Returns a player based on the specified Side
	 * 
	 * @param color The Side of the player to be returned
	 * @return The player of specified Side
	 */
	public static Player getPlayerByColor(Side color) {
		if (color == Side.WHITE) {
			return white;
		}
		return black;
	}

	/**
	 * Switches to the other players turn after each successful move
	 */
	public static void switchTurn() {
		if (playersTurn == Side.WHITE) {
			playersTurn = Side.BLACK;
		} else {
			playersTurn = Side.WHITE;
		}
	}

	/**
	 * Plays the 'clack' sound when a piece is moved
	 */
	public static void playSound() {
		try {
			File soundPath = new File(projectPath+"/res/sound.wav");

			if (!soundPath.exists()) {
				System.out.println("path not found");
				return;
			}

			AudioInputStream input = AudioSystem.getAudioInputStream(soundPath);
			Clip clip = AudioSystem.getClip();
			clip.open(input);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double ticks = 60.0;
		double ns = 1000000000 / ticks;
		double delta = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				// update();
				delta--;
			}
			if (running) {
				render();
			}
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// show fps
				frame.setTitle(TITLE + "FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	public static void main(String[] args) {
		new Game().start();
	}

}
