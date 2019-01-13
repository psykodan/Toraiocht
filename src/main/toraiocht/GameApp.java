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
	Field currArea;
	WorldBuilder world;

	// for painting in 32px cells
	private int cellX;
	private int cellY;
	private int scale = 64;

	private HashMap<Integer, BufferedImage> terrain;
	
	//direction counter
	private int dir = 1;

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
		WindowSize = new Dimension((480*(scale/32)), (320*(scale/32)));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width / 3 - WindowSize.width / 3;
		int y = screensize.height / 3 - WindowSize.height / 3;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		animator = new Thread(this);
		animator.start();
		addKeyListener(this);

		isGraphicsInitialised = true;
	}

	public void initWorld(Field curr) {
		curr = new Fanoremore();
		curr.fieldNum(1);
		world = new WorldBuilder(curr);
		terrain = world.getTerrain();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//PLAYER.setUp(false);
			//PLAYER.setDown(false);
			PLAYER.changeDir(1, dir);
			dir++;
			PLAYER.setRight(true);
			PLAYER.right();
			// PLAYER.start();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			//PLAYER.setUp(false);
			//PLAYER.setDown(false);
			PLAYER.changeDir(0, dir);
			dir++;
			PLAYER.setLeft(true);
			PLAYER.left();
			// PLAYER.start();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			//PLAYER.setLeft(false);
			//PLAYER.setRight(false);
			PLAYER.changeDir(2, dir);
			dir++;
			PLAYER.setUp(true);
			PLAYER.up();
			// PLAYER.start();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			//PLAYER.setLeft(false);
			//PLAYER.setRight(false);
			PLAYER.changeDir(3, dir);
			dir++;
			PLAYER.setDown(true);
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

			g.drawImage(terrain.get(world.getPaint1(p)), cellX, cellY,scale, scale, null);

			cellX = (cellX + scale) % (480*(scale/32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(world.getPaint2(p)), cellX, cellY,scale, scale, null);

			cellX = (cellX + scale) % (480*(scale/32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(world.getObjects(p)), cellX, cellY,scale, scale, null);

			cellX = (cellX + scale) % (480*(scale/32));// space cells 32px

			if (cellX == 0) {
				cellY += scale;
			}

		}

		// world.draw(g);
		g.drawImage(PLAYER.draw(), PLAYER.getX(), PLAYER.getY(),scale, scale, null);

		g.dispose();
		strategy.show();
	}

}
