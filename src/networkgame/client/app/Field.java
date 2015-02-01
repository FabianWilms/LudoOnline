package networkgame.client.app;

/**
 * Class for all types of fields: game fields and player fields. A field may
 * contain a pawn. A field can be assigned to a special player.
 * 
 * @author U. Hammerschall
 * @version 1.0
 */
public class Field {

	/** index of the field (for color) */
	private final int index;
	/** indicates if a pawn is on the field. */
	private Pawn pawn;

	/**
	 * Custom-Constructor.
	 * 
	 * @param index
	 *            of the field.
	 */
	public Field(int index) {
		this.index = index;
	}

	/**
	 * return index of the field
	 * 
	 * @return 0 if field is a game field, return index of the player if field
	 *         is a player field.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * return pawn on the field.
	 * 
	 * @return pawn if available otherwise null.
	 */
	public Pawn getPawn() {
		return pawn;
	}

	/**
	 * set pawn on the field
	 * 
	 * @param pawn
	 *            to set on the field.
	 */
	public void setPawn(Pawn pawn) {
		this.pawn = pawn;
	}
	
	/**
	 * Returns if this field has a pawn on it.
	 * 
	 * @return if this field has pawn situated on it.
	 */
	public boolean hasPawn(){
		return this.getPawn()!=null;
	}
	
	/**
	 * Returns a String representation of this field.
	 * 
	 * @return String-Rep of this pawn
	 */
	public String toString(){
		if(pawn == null)
			return String.valueOf(0);
		else
			return pawn.toString();
	}

}
