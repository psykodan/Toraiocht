package maps.toraiocht;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fanoremore implements Field {
	private int[][] area;
	// the spritesheet to use
	private static String texture = "assets/terrain/tileset.png";

	public void field1() {

		try {
			int[][] field = jsonReader("assets/terrain/Fanoremore/FanoremoreF1.json");
			area = field;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void field2() {

		try {
			int[][] field = jsonReader("assets/terrain/Fanoremore/FanoremoreF2.json");
			area = field;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void field3() {

		try {
			int[][] field = jsonReader("assets/terrain/Fanoremore/FanoremoreF3.json");
			area = field;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void field4() {

		try {
			int[][] field = jsonReader("assets/terrain/Fanoremore/FanoremoreF4.json");
			area = field;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void field5() {

		try {
			int[][] field = jsonReader("assets/terrain/Fanoremore/FanoremoreF5.json");
			area = field;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void field6() {

		try {
			int[][] field = jsonReader("assets/terrain/Fanoremore/FanoremoreF6.json");
			area = field;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void actions(int i) {

		if (i == 1) {
			field1();
		}

	}

	@Override
	public int[] getGround() {
		return area[0];
	}

	@Override
	public int[] getPaint1() {
		return area[1];
	}

	@Override
	public int[] getPaint2() {
		return area[2];
	}

	@Override
	public int[] getForeground() {
		return area[3];
	}

	@Override
	public int[] getObjects() {
		return area[4];
	}

	@Override
	public int[] getActions() {
		return area[5];
	}

	@Override
	public int[][] jsonReader(String filename) throws JSONException {
		Object data = null;
		String file = null;
		int[][] field = new int[6][];
		try {
			file = new String(Files.readAllBytes(Paths.get(filename)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject(file);

		JSONArray arr = obj.getJSONArray("layers");
		for (int i = 0; i < arr.length(); i++) {
			data = arr.getJSONObject(i).get("data");

			String dataS = data.toString();
			String[] dataSA = dataS.split("[ \\[ , \\] ]");
			int[] dataI = new int[289];

			for (int s = 1; s < 290; s++) {
				dataI[s - 1] = Integer.parseInt(dataSA[s]);
			}
			field[i] = dataI;

		}

		return field;

	}

	@Override
	public void fieldNum(int i) {
		switch (i) {
		case 1:
			field1();
			break;
		case 2:
			field2();
			break;
		case 3:
			field3();
			break;
		case 4:
			field4();
			break;
		case 5:
			field5();
			break;
		case 6:
			field6();
			break;
		default:
		}

	}
	@Override
	public String getTexture() {
		return texture;
	}

}
