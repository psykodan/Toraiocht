package maps.toraiocht;

import org.json.JSONException;

public interface Field {
	// interface that defines the most basic requirements for an area

	// defines actions at particular spots
	public void actions(int i);

	// gets the coords of where you can walk
	public int[] getGround();

	// gets the coords and tile type for making up the map
	public int[] getPaint1();

	// gets the coords and tile type for making up the map
	public int[] getPaint2();

	// gets the coords and tile type for making up the map
	public int[] getPaint3();

	// gets the coords and tile type for making up the map
	public int[] getPaint4();

	// gets the coords and tile type for making up the map
	public int[] getForeground1();

	// gets the coords and tile type for making up the map
	public int[] getForeground2();

	// gets the coords and id of action
	public int[] getActions();

	// xml reader
	public int[][] jsonReader(String file) throws JSONException;

	public void fieldNum(int i);

	public String getTexture();
}
