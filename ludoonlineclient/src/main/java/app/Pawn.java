package app;

/**
 * Class for player pawns. Each player has four pawns.
 * 
 * @author U. Hammerschall, F. Holtk�tter, A. Birkel
 * @version 1.0
 */
public class Pawn {

	/**
	 * index of the player.
	 */
	private final int index;

	/**
	 * Custom-Constructor. initializes new pawn with number of the assigned
	 * player.
	 * 
	 * @param index
	 *            : number of the player (0 < index <= 4).
	 */
	public Pawn(int index) {
		this.index = index;
	}

	/**
	 * return index of the player.
	 * 
	 * @return index of the player.
	 */
	public int getIndex() {
		return index;
	}
	
	public String toString(){
		return String.valueOf(index);
	}

}
