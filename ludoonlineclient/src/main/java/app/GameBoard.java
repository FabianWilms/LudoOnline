package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the internal game state. Please don't change.
 * 
 * @author U. Hammerschall
 * @version 1.0
 */
public class GameBoard {

	/** number of player independent game fields */
	public static final int NUMBER_OF_FIELDS = 40;
	/** structure of the board */
	private Field[][] board = new Field[11][11];
	/** list of player independent game fields */
	private final List<Field> fields = new ArrayList<Field>();
	/** list of home fields for each player */
	private final List<List<Field>> homes = new ArrayList<List<Field>>();
	/** list of target fields for each player */
	private final List<List<Field>> ends = new ArrayList<List<Field>>();

	/**
	 * initialize all game fields and assign to field structure.
	 */
	public GameBoard() {

		// set board fields

		for (int j = 0; j < 4; j++) {
			Field field = new Field(0);
			board[j][4] = field;
			fields.add(field);
		}

		for (int i = 4; i >= 0; i--) {
			Field field = new Field(0);
			board[4][i] = field;
			fields.add(field);
		}

		Field field = new Field(0);
		board[5][0] = field;
		fields.add(field);

		for (int i = 0; i <= 4; i++) {
			field = new Field(0);
			board[6][i] = field;
			fields.add(field);
		}
		for (int j = 7; j < 11; j++) {
			field = new Field(0);
			board[j][4] = field;
			fields.add(field);
		}

		field = new Field(0);
		board[10][5] = field;
		fields.add(field);

		for (int j = 10; j > 5; j--) {
			field = new Field(0);
			board[j][6] = field;
			fields.add(field);
		}

		for (int i = 7; i < 11; i++) {
			field = new Field(0);
			board[6][i] = field;
			fields.add(field);
		}

		field = new Field(0);
		board[5][10] = field;
		fields.add(field);

		for (int i = 10; i > 5; i--) {
			field = new Field(0);
			board[4][i] = field;
			fields.add(field);
		}

		for (int j = 3; j >= 0; j--) {
			field = new Field(0);
			board[j][6] = field;
			fields.add(field);
		}

		field = new Field(0);
		board[0][5] = field;
		fields.add(field);

		// set home fields

		// red player
		List<Field> p1 = new ArrayList<Field>();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				field = new Field(1);
				board[j][i] = field;
				p1.add(field);
			}
		}
		homes.add(p1);

		// blue player
		List<Field> p2 = new ArrayList<Field>();
		for (int i = 0; i < 2; i++) {
			for (int j = 9; j < 11; j++) {
				field = new Field(2);
				board[j][i] = field;
				p2.add(field);
			}
		}
		homes.add(p2);

		// green player
		List<Field> p3 = new ArrayList<Field>();
		for (int i = 9; i < 11; i++) {
			for (int j = 9; j < 11; j++) {
				field = new Field(3);
				board[j][i] = field;
				p3.add(field);
			}
		}
		homes.add(p3);

		// yellow player
		List<Field> p4 = new ArrayList<Field>();
		for (int i = 9; i < 11; i++) {
			for (int j = 0; j < 2; j++) {
				field = new Field(4);
				board[j][i] = field;
				p4.add(field);
			}
		}
		homes.add(p4);

		// set end fields

		// red player
		List<Field> p5 = new ArrayList<Field>();
		for (int j = 1; j < 5; j++) {
			field = new Field(1);
			board[j][5] = field;
			p5.add(field);
		}
		ends.add(p5);

		// blue player
		List<Field> p6 = new ArrayList<Field>();
		for (int i = 1; i < 5; i++) {
			field = new Field(2);
			board[5][i] = field;
			p6.add(field);
		}
		ends.add(p6);

		// green player
		List<Field> p7 = new ArrayList<Field>();
		for (int j = 9; j > 5; j--) {
			field = new Field(3);
			board[j][5] = field;
			p7.add(field);
		}
		ends.add(p7);

		// yellow player
		List<Field> p8 = new ArrayList<Field>();
		for (int i = 9; i > 5; i--) {
			field = new Field(4);
			board[5][i] = field;
			p8.add(field);
		}
		ends.add(p8);
		
		
	}

	/**
	 * return list of home fields of the selected player.
	 * 
	 * @param index
	 *            of the player.
	 * @return list of home fields.
	 */
	public List<Field> getHomes(int index) {
		return homes.get(index);
	}

	/**
	 * return list of target fields of the selected player.
	 * 
	 * @param index
	 *            of the player.
	 * @return list of target fields.
	 */
	public List<Field> getEnds(int index) {
		return ends.get(index);
	}

	/**
	 * return structure of the game with game field.
	 * 
	 * @return 2-dimensional structure of the game.
	 */
	public Field[][] getBoard() {
		return board;
	}

	/**
	 * return list of simple game fields.
	 * 
	 * @return list of game fields.
	 */
	public List<Field> getFields() {
		return fields;
	}

}
