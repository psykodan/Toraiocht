package main.toraiocht;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import maps.toraiocht.Field;

@SuppressWarnings("serial")
public class WorldBuilder extends JFrame {

	// class that takes a townlands field and creates it

	// for holding the values of the field array
	private int[] ground;
	private int[] paint1;
	private int[] paint2;
	private int[] paint3;
	private int[] paint4;
	private int[] foreground1;
	private int[] foreground2;
	private int[] actions;

	// mapping terrain tiles from a spritesheet to a number to coincide with the
	// array
	private static HashMap<Integer, BufferedImage> terrain = new HashMap<Integer, BufferedImage>();

	private BufferedImage blank = null;

	// for painting in 32px cells
	private int cellX;
	private int cellY;

	// the spritesheet to use
	private static String texture;

	public WorldBuilder(Field currArea) {
		// load the spritesheet
		texture = currArea.getTexture();
		Sprite.loadSprite(texture);

		try {
			blank = ImageIO.read(new File("assets/terrain/blank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// load the values of the current field
		ground = currArea.getGround();
		paint1 = currArea.getPaint1();
		paint2 = currArea.getPaint2();
		paint3 = currArea.getPaint3();
		paint4 = currArea.getPaint4();
		foreground1 = currArea.getForeground1();
		foreground2 = currArea.getForeground2();
		actions = currArea.getActions();

		// mapping the terrain
		int id = 1;
		for (int g = 0; g < 32; g++) {
			for (int h = 0; h < 32; h++) {
				terrain.put(id, Sprite.getSprite(h, g));
				id++;
			}
		}
		terrain.put(0, blank);

	}

	public HashMap<Integer, BufferedImage> getTerrain() {
		return terrain;
	}

	public int getGround(int p) {
		return ground[p];
	}

	public int getPaint1(int p) {
		return paint1[p];
	}

	public int getPaint2(int p) {
		return paint2[p];
	}

	public int getPaint3(int p) {
		return paint3[p];
	}

	public int getPaint4(int p) {
		return paint4[p];
	}

	public int getForeground1(int p) {
		return foreground1[p];
	}

	public int getForeground2(int p) {
		return foreground2[p];
	}

	public int getActions(int p) {
		return actions[p];
	}

}
