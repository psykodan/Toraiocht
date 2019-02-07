package main.toraiocht;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import maps.toraiocht.TNSFanoremore;

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
	private int[][] ground = new int[75][90];
	private int[][] actions = new int[75][90];
	private HashMap<Integer, BufferedImage> terrain;

	// for painting in 32px cells
	private int cellX;
	private int cellY;
	private int scale = 64;
	private int cMoveX = 1;
	private int cMoveY = 1;

	// direction counter
	private int dir = 1;

	// collision detection
	private boolean collDetect = false;
	private boolean collDetectLeft = false;
	private boolean collDetectRight = false;
	private boolean collDetectUp = false;
	private boolean collDetectDown = false;

	// TNS world timer
	private boolean TNSTimeSet = false;
	private int timerMax = 1000;
	private int TNSTime = timerMax;

	private boolean gameOver = false;

	public static void main(String[] args) {
		GameApp game = new GameApp();
	}

	// constructor
	public GameApp() {

		initWorld(currArea);
		initWin();
		PLAYER.setPosition(WindowSize.width / 2, WindowSize.height / 2);
	}

	// Set up window size and orientation
	public void initWin() {
		this.setTitle("Toraiocht");
		WindowSize = new Dimension((480 * (scale / 32)), (480 * (scale / 32)));
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
		// currID = id;
		// currFieldID = f;
		// curr = areaSelect(id);
		// curr = new TNSFanoremore();
		// curr.fieldNum(f);
		curr = new Fanoremore();
		world = new WorldBuilder(curr);
		terrain = world.getTerrain();

		int x = 0;
		int y = 0;
		for (int g = 0; g < 6750; g++) {

			ground[y][x] = world.getGround(g);
			actions[y][x] = world.getActions(g);

			x = (x + 1) % 90;

			if (x == 0) {
				y++;
			}
		}

		// PLAYER.setPosition(400, 300);
	}

	private Field areaSelect(int id) {
		Field f = null;

		switch (id) {
		case 1:
			f = new Fanoremore();
			break;
		case 11:
			f = new TNSFanoremore();
			break;
		default:
		}
		return f;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {

			// TODO tir na sidhe transport

		}

		if (e.getKeyCode() == KeyEvent.VK_Z) {
			// sprint
			PLAYER.setSpeed(12);

		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

			PLAYER.changeDir(1, dir);
			dir++;
			if (collDetectRight == false) {
				PLAYER.setRight(true);
			}
			PLAYER.right();

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

			PLAYER.changeDir(0, dir);
			dir++;
			if (collDetectLeft == false) {
				PLAYER.setLeft(true);
			}
			PLAYER.left();

		} else if (e.getKeyCode() == KeyEvent.VK_UP) {

			PLAYER.changeDir(2, dir);
			dir++;
			if (collDetectUp == false) {
				PLAYER.setUp(true);
			}
			PLAYER.up();

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

			PLAYER.changeDir(3, dir);
			dir++;
			if (collDetectDown == false) {
				PLAYER.setDown(true);
			}
			PLAYER.down();

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			PLAYER.restX();
			dir--;
			PLAYER.setLeft(false);
			PLAYER.setRight(false);

		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
			PLAYER.restY();
			dir--;
			PLAYER.setUp(false);
			PLAYER.setDown(false);

		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			PLAYER.setSpeed(6);

		}

	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true) {

			timer();
			collision();
			action();
			PLAYER.update();
			PLAYER.move();
			cMoveX += PLAYER.getXSpeed();
			cMoveY += PLAYER.getYSpeed();
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

	public void timerStart() {
		TNSTimeSet = true;
	}

	public void timerStop() {
		TNSTimeSet = false;
	}

	public void timer() {

		if (TNSTimeSet == true) {
			TNSTime--;
		}

		if (TNSTime == (timerMax / 5) * 4) {
			PLAYER.changeSpriteSheet(3);
		} else if (TNSTime == (timerMax / 5) * 3) {
			PLAYER.changeSpriteSheet(4);
		} else if (TNSTime == (timerMax / 5) * 2) {
			PLAYER.changeSpriteSheet(5);
		} else if (TNSTime == (timerMax / 5) * 1) {
			PLAYER.changeSpriteSheet(6);
		} else if (TNSTime == 0) {
			gameOver = true;
		}

	}

	public void action() {

		for (int y = 0; y < actions.length; y++) {
			for (int x = 0; x < actions[0].length; x++) {

				if (actions[y][x] != 0) {

					if ((PLAYER.getX() <= ((x) * scale) & PLAYER.getX() >= ((x) * scale) - (scale / 2)
							& PLAYER.getY() + (scale / 2) <= y * scale & PLAYER.getY() + (scale / 2) >= (y - 1) * scale)
							|| (PLAYER.getX() + scale <= ((x - 1) * scale) + (scale / 2)
									& PLAYER.getX() + scale >= ((x - 1) * scale)
									& PLAYER.getY() + (scale / 2) <= y * scale
									& PLAYER.getY() + (scale / 2) >= (y - 1) * scale)
							|| (PLAYER.getY() <= ((y) * scale) & PLAYER.getY() >= ((y) * scale) - (scale / 2)
									& PLAYER.getX() + (scale / 2) <= x * scale
									& PLAYER.getX() + (scale / 2) >= (x - 1) * scale)
							|| (PLAYER.getY() + scale <= ((y - 1) * scale) + (scale / 2)
									& PLAYER.getY() + scale >= ((y - 1) * scale)
									& PLAYER.getX() + (scale / 2) <= x * scale
									& PLAYER.getX() + (scale / 2) >= (x - 1) * scale)) {

						if (TNSTimeSet == true) {
							if (actions[y][x] == 704) {
								PLAYER.changeSpriteSheet(1);
								// TODO real word change
								timerStop();
								TNSTime = timerMax;
							}
						}
					}
				}
			}
		}

	}

	public void collision() {

		collDetect = false;

		for (int y = 0; y < ground.length; y++) {
			for (int x = 0; x < ground[0].length; x++) {

				if (ground[y][x] == 0) {

					if (PLAYER.getX() <= ((x) * scale) & PLAYER.getX() >= ((x) * scale) - (scale / 2)
							& PLAYER.getY() + (scale / 2) <= y * scale
							& PLAYER.getY() + (scale / 2) >= (y - 1) * scale) {
						PLAYER.setLeft(false);

						if (collDetectLeft == false) {
							PLAYER.restX();
						}
						collDetectLeft = true;
						collDetect = true;
					}
					if (PLAYER.getX() + scale <= ((x - 1) * scale) + (scale / 2)
							& PLAYER.getX() + scale >= ((x - 1) * scale) & PLAYER.getY() + (scale / 2) <= y * scale
							& PLAYER.getY() + (scale / 2) >= (y - 1) * scale) {
						PLAYER.setRight(false);

						if (collDetectRight == false) {
							PLAYER.restX();
						}
						collDetectRight = true;
						collDetect = true;
					}
					if (PLAYER.getY() <= ((y) * scale) & PLAYER.getY() >= ((y) * scale) - (scale / 2)
							& PLAYER.getX() + (scale / 2) <= x * scale
							& PLAYER.getX() + (scale / 2) >= (x - 1) * scale) {
						PLAYER.setUp(false);

						if (collDetectUp == false) {
							PLAYER.restY();
						}
						collDetectUp = true;
						collDetect = true;
					}
					if (PLAYER.getY() + scale <= ((y - 1) * scale) + (scale / 2)
							& PLAYER.getY() + scale >= ((y - 1) * scale) & PLAYER.getX() + (scale / 2) <= x * scale
							& PLAYER.getX() + (scale / 2) >= (x - 1) * scale) {
						PLAYER.setDown(false);

						if (collDetectDown == false) {
							PLAYER.restY();
						}
						collDetectDown = true;
						collDetect = true;
					}

				}
			}
		}
		if (collDetect == false) {
			collDetectLeft = false;
			collDetectRight = false;
			collDetectUp = false;
			collDetectDown = false;
			collDetect = false;
		}
	}

	public void paint(Graphics g) {
		if (!isGraphicsInitialised) {
			return;
		}
		g = strategy.getDrawGraphics();

		if (gameOver == false) {
			int counter = 0;
			// g.setColor(Color.black);
			// g.fillRect(0, 0, WindowSize.width, WindowSize.height);

			// World painting method

			// Go through each cell and retrieve tile based on value
			cellX = -scale - cMoveX;
			cellY = -scale - cMoveY;
			for (int p = 0; p < 6750; p++) {

				g.drawImage(terrain.get(world.getPaint1(p)), cellX, cellY, scale, scale, null);

				cellX = (cellX + scale);
				counter++;

				if (counter == 90) {
					cellX = -scale - cMoveX;
					cellY += scale;
					counter = 0;
				}

			}

			counter = 0;
			cellX = -scale - cMoveX;
			cellY = -scale - cMoveY;
			for (int p = 0; p < 6750; p++) {

				g.drawImage(terrain.get(world.getPaint2(p)), cellX, cellY, scale, scale, null);

				cellX = (cellX + scale);
				counter++;

				if (counter == 90) {
					cellX = -scale - cMoveX;
					cellY += scale;
					counter = 0;
				}

			}

			counter = 0;
			cellX = -scale - cMoveX;
			cellY = -scale - cMoveY;
			for (int p = 0; p < 6750; p++) {

				g.drawImage(terrain.get(world.getPaint3(p)), cellX, cellY, scale, scale, null);

				cellX = (cellX + scale);
				counter++;

				if (counter == 90) {
					cellX = -scale - cMoveX;
					cellY += scale;
					counter = 0;
				}

			}

			counter = 0;
			cellX = -scale - cMoveX;
			cellY = -scale - cMoveY;
			for (int p = 0; p < 6750; p++) {

				g.drawImage(terrain.get(world.getPaint4(p)), cellX, cellY, scale, scale, null);

				cellX = (cellX + scale);
				counter++;

				if (counter == 90) {
					cellX = -scale - cMoveX;
					cellY += scale;
					counter = 0;
				}

			}

			// world.draw(g);
			g.drawImage(PLAYER.draw(), WindowSize.width / 2, WindowSize.height / 2, scale, scale, null);

			counter = 0;
			cellX = -scale - cMoveX;
			cellY = -scale - cMoveY;
			for (int p = 0; p < 6750; p++) {

				g.drawImage(terrain.get(world.getForeground1(p)), cellX, cellY, scale, scale, null);

				cellX = (cellX + scale);
				counter++;

				if (counter == 90) {
					cellX = -scale - cMoveX;
					cellY += scale;
					counter = 0;
				}

			}

			counter = 0;
			cellX = -scale - cMoveX;
			cellY = -scale - cMoveY;
			for (int p = 0; p < 6750; p++) {

				g.drawImage(terrain.get(world.getForeground2(p)), cellX, cellY, scale, scale, null);

				cellX = (cellX + scale);
				counter++;

				if (counter == 90) {
					cellX = -scale - cMoveX;
					cellY += scale;
					counter = 0;
				}

			}

			g.dispose();
			strategy.show();
		} else {
			g.setColor(Color.black);
			g.fillRect(0, 0, WindowSize.width, WindowSize.height);
			g.setColor(Color.white);
			Font f = new Font("Arial MS", Font.BOLD, 30);
			g.setFont(f);
			g.drawString("GAME OVER", (WindowSize.width / 2) - 100, WindowSize.height / 2);
			strategy.show();
		}
	}

}
