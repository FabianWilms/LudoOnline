package gui;

import app.Field;
import app.Game;
import app.GameBoard;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Lower panel of the user interface. Visualizes game fields, player fields and
 * the current position of the player pawns.
 * 
 * @author U. Hammerschall, F. Holtk�tter, A. Birkel
 * @version 1.0
 */
public class BoardView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	/** available colors */
	public static Color[] colors = new Color[] { Color.WHITE, 
												new Color(0xFF4444), //Red
												new Color(0x33B5E5), //Blue
												new Color(0x99CC00), //Green
												new Color(0xFFBB33), //Yellow
												Color.DARK_GRAY};

	/** list of all fields of the game */
	private List<FieldView> fields = new ArrayList<FieldView>();

	/** reference to the game logic */
	private final Game game;

	/**
	 * Custom-Constructor. Initializes all visible fields of the game and
	 * assigns their internal field representation.
	 * 
	 * @param game
	 *            Reference to the game
	 * @param board
	 *            Reference to the game board
	 */
	public BoardView(Game game, GameBoard board) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		this.setBorder(BorderFactory.createLineBorder(Color.black,10));
		setBackground(Color.black);
		this.game = game;
		this.game.addObserver(this);
		Field[][] model = board.getBoard();

		for (int i = 0; i < model.length; i++) {
			for (int j = 0; j < model.length; j++) {
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = i;
				gbc.gridy = j;
				if (model[i][j] != null) {
					Field field = model[i][j];
					FieldView next = new FieldView(colors[field.getIndex()],field, game);
					next.update();
					fields.add(next);
					add(next, gbc);
				}
			}
		}
	}

	/**
	 * update game state on view.
	 */
	public synchronized void update(Observable arg0, Object arg1) {
			for (FieldView field : fields)
				field.update();
	}

}