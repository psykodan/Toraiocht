package main.toraiocht;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import maps.toraiocht.Fanoremore;
import maps.toraiocht.Field;

public class GameApp extends JFrame implements Runnable, KeyListener {

	// Window size
	private static Dimension WindowSize;

	// Thread for game animation
	private Thread animator;
	// Thread delay for sleep time
	private final int DELAY = 40;

	// Graphic buffering to prevent flickering
	private static boolean isGraphicsInitialised = false;
	private BufferStrategy strategy;

	// Player
	private Player PLAYER = new Player();

	// Initial world
	private Field currArea;
	private WorldBuilder world;
	private int[][] ground = new int[10][15];
	private HashMap<Integer, BufferedImage> terrain;

	// for painting in 32px cells
	private int cellX;
	private int cellY;
	private int scale = 64;

	// direction counter
	private int dir = 1;

	// collision detection
	private boolean collDetectLeft = false;
	private boolean collDetectRight = false;
	private boolean collDetectUp = false;
	private boolean collDetectDown = false;

	public static void main(String[] args) {
		GameApp game = new GameApp();
	}

	// constructor
	public GameApp() {
		initWorld(currArea);
		initWin();

	}

	// Set up window size and orientation
	public void initWin() {
		this.setTitle("Toraiocht");
		WindowSize = new Dimension((480 * (scale / 32)), (320 * (scale / 32)));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width / 3 - WindowSize.width / 3;
		int y = screensize.height / 3 - WindowSize.height / 3;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);

		createBufferStrategy(3);
		strategy = getBufferStrategy();

		animator = new Thread(this);
		animator.start();
		addKeyListener(this);

		isGraphicsInitialised = true;
	}

	public void initWorld(Field curr) {
		curr = new Fanoremore();
		curr.fieldNum(2);
		world = new WorldBuilder(curr);
		terrain = world.getTerrain();

		int x = 0;
		int y = 0;
		for (int g = 0; g < 150; g++) {

			ground[y][x] = world.getGround(g);

			x = (x + 1) % 15;

			if (x == 0) {
				y++;
			}
		}
		
		PLAYER.setPosition(400, 300);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// PLAYER.setUp(false);
			// PLAYER.setDown(false);
			PLAYER.changeDir(1, dir);
			dir++;
			if (collDetectRight == false) {
				PLAYER.setRight(true);
			}
			PLAYER.right();
			// PLAYER.start();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// PLAYER.setUp(false);
			// PLAYER.setDown(false);
			PLAYER.changeDir(0, dir);
			dir++;
			if (collDetectLeft == false) {
				PLAYER.setLeft(true);
			}
			PLAYER.left();
			// PLAYER.start();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			// PLAYER.setLeft(false);
			// PLAYER.setRight(false);
			PLAYER.changeDir(2, dir);
			dir++;
			if (collDetectUp == false) {
				PLAYER.setUp(true);
			}
			PLAYER.up();
			// PLAYER.start();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			// PLAYER.setLeft(false);
			// PLAYER.setRight(false);
			PLAYER.changeDir(3, dir);
			dir++;
			if (collDetectDown == false) {
				PLAYER.setDown(true);
			}
			PLAYER.down();
			// PLAYER.start();

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			PLAYER.restX();
			dir--;
			PLAYER.setLeft(false);
			PLAYER.setRight(false);
			// PLAYER.stop();
			// PLAYER.reset();

		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
			PLAYER.restY();
			dir--;
			PLAYER.setUp(false);
			PLAYER.setDown(false);
			// PLAYER.stop();
			// PLAYER.reset();

		}

	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true) {

			PLAYER.update();
			PLAYER.move();
			collision();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {

				String msg = String.format("Thread interrupted: %s", e.getMessage());

				JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}

			beforeTime = System.currentTimeMillis();
		}
	}

	public void collision() {
		collDetectLeft = false;
		collDetectRight = false;
		collDetectUp = false;
		collDetectDown = false;

		for (int y = 0; y < ground.length; y++) {
			for (int x = 0; x < ground[0].length; x++) {

				if (ground[y][x] == 0) {

					if (PLAYER.getX()+(scale/1.5) >= (x * scale) && PLAYER.getX()+(scale/2.5) < ((x + 1) * scale)
							&& PLAYER.getY()+(scale) >= (y * scale) && PLAYER.getY()+(scale/1.45) < ((y + 1) * scale)) {

						int direction = PLAYER.getDir();

						if (direction == 0) {
							PLAYER.setLeft(false);
							collDetectLeft = true;
							PLAYER.restX();
							PLAYER.setPosition(PLAYER.getX()+4, PLAYER.getY());
						} else if (direction == 1) {
							PLAYER.setRight(false);
							collDetectRight = true;
							PLAYER.restX();
							PLAYER.setPosition(PLAYER.getX()-4, PLAYER.getY());
						} else if (direction == 2) {
							PLAYER.setUp(false);
							collDetectUp = true;
							PLAYER.restY();
							PLAYER.setPosition(PLAYER.getX(), PLAYER.getY()+4);
						} else if (direction == 3) {
							PLAYER.setDown(false);
							collDetectDown = true;
							PLAYER.restY();
							PLAYER.setPosition(PLAYER.getX(), PLAYER.getY()-4);
						}

					}

				}
			}
		}
	}

	public void paint(Graphics g) {
		if (!isGraphicsInitialised) {
			return;
		}
		g = strategy.getDrawGraphics();

		// g.setColor(Color.black);
		// g.fillRect(0, 0, WindowSize.width, WindowSize.height);

		// World painting method

		// Go through each cell and retrieve tile based on value
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(world.getPaint1(p)), cellX, cellY, scale, scale, null);

			cellX = (cellX + scale) % (480 * (scale / 32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(world.getPaint2(p)), cellX, cellY, scale, scale, null);

			cellX = (cellX + scale) % (480 * (scale / 32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(world.getObjects(p)), cellX, cellY, scale, scale, null);

			cellX = (cellX + scale) % (480 * (scale / 32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}

		// world.draw(g);
		g.drawImage(PLAYER.draw(), PLAYER.getX(), PLAYER.getY(), scale, scale, null);

		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(world.getForeground(p)), cellX, cellY, scale, scale, null);

			cellX = (cellX + scale) % (480 * (scale / 32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}
		
		g.dispose();
		strategy.show();
	}

}
