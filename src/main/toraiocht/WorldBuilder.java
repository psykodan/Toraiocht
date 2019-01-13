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
	private int[] objects;
	private int[] actions;

	// mapping terrain tiles from a spritesheet to a number to coincide with the
	// array
	private static HashMap<Integer, BufferedImage> terrain = new HashMap<Integer, BufferedImage>();

	private BufferedImage blank = null;

	// for painting in 32px cells
	private int cellX;
	private int cellY;

	// the spritesheet to use
	private static String texture = "assets/terrain/terrain_atlas.png";

	public WorldBuilder(Field currArea) {
		// load the spritesheet
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
		objects = currArea.getObjects();
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

	// Method that is invoked from main programme
	public void draw(Graphics g) {

		// Go through each cell and retrieve tile based on value
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(paint1[p]), cellX, cellY, null);

			cellX = (cellX + 32) % 480;// space cells 32px

			if (cellX == 0) {
				cellY += 32;
			}

		}
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(paint2[p]), cellX, cellY, null);

			cellX = (cellX + 32) % 480;// space cells 32px

			if (cellX == 0) {
				cellY += 32;
			}

		}
		cellX = 0;
		cellY = 0;
		for (int p = 0; p < 150; p++) {

			g.drawImage(terrain.get(objects[p]), cellX, cellY, null);

			cellX = (cellX + 32) % 480;// space cells 32px

			if (cellX == 0) {
				cellY += 32;
			}

		}

	}

	public int[] getGround() {
		return ground;
	}

	public Object getPaint1(int p) {
		return paint1[p];
	}

	public Object getPaint2(int p) {
		return paint2[p];
	}

	public Object getObjects(int p) {
		return objects[p];
	}

}
