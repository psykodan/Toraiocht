package main.toraiocht;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player {
	// coords
	protected int x, y;

	// movement
	private int xSpeed = 0;
	private int ySpeed = 0;
	private int speed = 6;

	private ArrayList<Integer> direction = new ArrayList<Integer>();

	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;

	private int lastDir = 0; // 0 = left, 1 = right, 2 = up, 3 = down

	// player spritesheet
	// private static String character = "assets/characters/player.png";
	private static String character;

	// Images for each animation
	private BufferedImage[] walkingLeft = new BufferedImage[4];
	private BufferedImage[] walkingRight = new BufferedImage[4];
	private BufferedImage[] walkingUp = new BufferedImage[4];
	private BufferedImage[] walkingDown = new BufferedImage[4];
	private BufferedImage[] resting = new BufferedImage[1];

	// These are animation states
	private Animation walkLeft;
	private Animation walkRight;
	private Animation walkUp;
	private Animation walkDown;
	private Animation standing;

	// this acts as current animation showing
	private Animation animation;

	public Player() {

		// load spritesheet
		character = setSpriteSheet(1);
		Sprite.loadSprite(character);

		// append Images for each animation
		for (int i = 0; i < 4; i++) {

			walkingLeft[i] = Sprite.getSprite(i, 1);
			walkingRight[i] = Sprite.getSprite(i, 2);
			walkingUp[i] = Sprite.getSprite(i, 3);
			walkingDown[i] = Sprite.getSprite(i, 0);
		}
		resting[0] = Sprite.getSprite(0, 0);

		// initialise animation states
		walkLeft = new Animation(walkingLeft, 2);
		walkRight = new Animation(walkingRight, 2);
		walkUp = new Animation(walkingUp, 2);
		walkDown = new Animation(walkingDown, 2);
		standing = new Animation(resting, 2);

		animation = standing;

		direction.add(0);
		direction.add(0);
		direction.add(0);
		direction.add(0);

	}

	public void changeSpriteSheet(int x) {
		character = setSpriteSheet(x);
		Sprite.loadSprite(character);

		// append Images for each animation
		for (int i = 0; i < 4; i++) {

			walkingLeft[i] = Sprite.getSprite(i, 1);
			walkingRight[i] = Sprite.getSprite(i, 2);
			walkingUp[i] = Sprite.getSprite(i, 3);
			walkingDown[i] = Sprite.getSprite(i, 0);
		}
		resting[0] = Sprite.getSprite(0, 0);

		// initialise animation states
		walkLeft = new Animation(walkingLeft, 2);
		walkRight = new Animation(walkingRight, 2);
		walkUp = new Animation(walkingUp, 2);
		walkDown = new Animation(walkingDown, 2);
		standing = new Animation(resting, 2);
		
		animation = standing;

	}

	private String setSpriteSheet(int i) {
		String chr = null;

		switch (i) {

		case 1:
			chr = "assets/characters/player.png";
			break;
		case 2:
			chr = "assets/characters/spailpin2.png";
			break;
		case 3:
			chr = "assets/characters/spailpin3.png";
			break;
		case 4:
			chr = "assets/characters/spailpin4.png";
			break;
		case 5:
			chr = "assets/characters/spailpin5.png";
			break;
		case 6:
			chr = "assets/characters/spailpin6.png";
			break;
		default:
		}

		return chr;
	}

	public void update() {

		animation.start();

		if (xSpeed == 0 && ySpeed == 0) {
			animation.stop();
			animation.reset();
		}
		animation.update();
	}

	public void start() {
		animation.start();
	}

	public void stop() {
		animation.stop();
	}

	public void reset() {
		animation.reset();
	}

	public BufferedImage draw() {
		return animation.getSprite();
	}

	public void setPosition(int i, int j) {
		x = i;
		y = j;

	}

	public void left() {

		if (animation != walkLeft) {
			animation = walkLeft;

		}

	}

	public void right() {

		if (animation != walkRight) {
			animation = walkRight;

		}

	}

	public void up() {

		if (animation != walkUp) {
			animation = walkUp;
		}

	}

	public void down() {

		if (animation != walkDown) {
			animation = walkDown;
		}

	}

	public int direction(ArrayList<Integer> d) {

		int dir;
		int x = Math.max(d.get(0), Math.max(d.get(2), Math.max(d.get(1), d.get(3))));

		if (x == 0) {
			dir = -1;
		} else {
			dir = d.indexOf(x);
		}

		return dir;

	}

	public void restX() {
		xSpeed = 0;
		direction.set(0, 0);
		direction.set(1, 0);
	}

	public void restY() {

		ySpeed = 0;
		direction.set(2, 0);
		direction.set(3, 0);
	}

	public void move() {
		int dir = direction(direction);
		switch (dir) {
		case 0:
			if (left == true) {

				xSpeed = -speed;
				ySpeed = 0;
			}
			animation = walkLeft;
			lastDir = 0;
			break;
		case 1:
			if (right == true) {
				xSpeed = speed;
				ySpeed = 0;
			}
			animation = walkRight;
			lastDir = 1;
			break;
		case 2:
			if (up == true) {
				ySpeed = -speed;
				xSpeed = 0;
			}
			animation = walkUp;
			lastDir = 2;
			break;
		case 3:
			if (down == true) {
				ySpeed = speed;
				xSpeed = 0;
			}
			animation = walkDown;
			lastDir = 3;
			break;
		default:
			// code block
		}

		x += xSpeed;
		y += ySpeed;

	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}
	
	public int getYSpeed() {
		return ySpeed;
	}

	public int getXSpeed() {
		return xSpeed;
	}

	public void setRight(boolean c) {
		right = c;
	}

	public void setLeft(boolean c) {
		left = c;
	}

	public void setUp(boolean c) {
		up = c;
	}

	public void setDown(boolean c) {
		down = c;
	}

	public void changeDir(int index, int val) {
		direction.set(index, val);
	}

	public int getDir() {
		int dir = direction(direction);
		return dir;
	}

	public int getLastDir() {
		return lastDir;
	}
	
	public void setSpeed(int s) {
		speed = s;
	}
}
