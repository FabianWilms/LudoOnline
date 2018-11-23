package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a player of the game. Each player has four pawns, a
 * list of home fields, a list of target fields and a reference to the general
 * game fields. A player is responsible for all checks and moves of his or her
 * pawns.
 * 
 * @author U. Hammerschall, F. Holtk�tter, A. Birkel
 * @version 1.0
 */
public class Player {

	/** list of home fields of the player */
	private List<Field> homeFields = new ArrayList<Field>();
	/** list of target fields of the player */
	private List<Field> endFields = new ArrayList<Field>();
	/** list of game fields */
	private List<Field> fields;
	/** index of the player */
	private final int index;
	/** First Global field of this player */
	private int startIndex;
	/** Field before this players finish */
	private int endIndex;
	
	/**
	 * initializes a new player.
	 * 
	 * @param index
	 *            : index that represents color of the player (0 < index <= 4).
	 * @param board
	 *            : player independent game fields
	 * @param startIndex
	 *            : startIndex of this player
	 * @param endIndex
	 *            : endIndex of this player
	 */
	public Player(int index, GameBoard board, Game game, int startIndex, int endIndex) {
		this.index = index;
		this.fields = board.getFields();
		this.homeFields = board.getHomes(index - 1);
		this.endFields = board.getEnds(index - 1);
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		for (Field field : homeFields) {
			Pawn pawn = new Pawn(index);
			field.setPawn(pawn);
		}

	}
	
	/**
	 * Chooses a pawn from this players home to move when a 6 was rolled.
	 */
	public void moveFromHome(){
		for (Field field : homeFields) {
			if (field.getPawn() != null) {
				field.setPawn(null);
				break;
			}
		}
	}
	
	/**
	 * After beeing kicked by another player this method adds a pawn back to this players home fields.
	 */
	public void setPawnOnHome(){
		for (Field newField : homeFields) {
			if (newField.getPawn() == null) {
				newField.setPawn(new Pawn(index));
				break;
			}
		}
	}
	
	/**
	 * Returns the number of rolls this player has, based on the status of his home- and endfields.
	 * 
	 * @return Number of rolls this player has in his next move.
	 */
	public int getRolls(){
		if(this.getHomePawns() + this.getEndPawns() == 4){
			return 3;
		}
		return 1;
	}

	public List<Field> getHomeFields() {
		return homeFields;
	}

	public List<Field> getEndFields() {
		return endFields;
	}

	public List<Field> getFields() {
		return fields;
	}

	public int getIndex() {
		return index;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}
	
	/**
	 * Return the number of pawns this player still has in his homefields.
	 * 
	 * @return Number of pawns still in home.
	 */
	public int getHomePawns(){
		int pawns = 0;
		for(int i = 0; i<4; i++){
			if(homeFields.get(i).getPawn() != null){
				pawns++;
			}
		}
		return pawns;
	}
	
	/**
	 * Return the number of pawns this player has in his endfields.
	 * 
	 * @return Number of pawns in end.
	 */
	public int getEndPawns(){
		int pawns = 0;
		for(int i = 0; i<4; i++){
			if(endFields.get(i).getPawn() != null){
				pawns++;
			}
		}
		return pawns;
	}
	
	public String toString(){
		String output = "";
		output += homeFields.toString();
		output += endFields.toString();
		return output;
	}

}
