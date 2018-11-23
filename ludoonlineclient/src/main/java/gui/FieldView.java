package gui;

import app.Field;
import app.Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class for all fields that are visible on the user interface. A visible field
 * visualizes the state of a game field. The game field represents the actual
 * state (Player field or game field? Pawn or no pawn on the field?). During
 * update game field and visible field are synchronized.
 * 
 * @author U. Hammerschall, F. Holtkötter, A. Birkel
 * @version 1.0
 */
public class FieldView extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	/** original color of the field boarder. Should not change */
	private final Color original;
	/** current color of the field. Pawn color or black. */
	private Color color;
	/** pawn on field? */
	private boolean fill = false;
	/** assigned game field */
	private Field field;
	/** game reference to send mouse events to */
	private Game game;
	/** Counter for activating Debug-Mode */
	private int counterActivateDebugMode = 0;
	

	/**
	 * Custom-Constructor. Initializes a visible representation of a game field.
	 * Does not accept mouse events.
	 * 
	 * @param color
	 *            : initial and original color of the field.
	 * @param field
	 *            : assigned game field with state information.
	 * @param game
	 *            : game to send events to.
	 */
	public FieldView(Color color, Field field, Game game) {
		this.color = color;
		this.original = color;
		this.game = game;
		this.field = field;
		setPreferredSize(new Dimension(46,46));
		setBackground(Color.black);
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(color);
		if (fill){
			g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
		}
		else {
			g2D.setStroke(new BasicStroke(2));
			g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
		}
	}

	/**
	 * synchronize state of the visible field with state of the assigned field.
	 */
	public void update() {
		if (field.getPawn() != null) {
			color = BoardView.colors[field.getPawn().getIndex()];
			fill = true;
		} else {
			color = original;
			fill = false;
		}
		repaint(); // repaint panel.
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseClicked(MouseEvent arg0) {
		//Move 
		if(this.field.getIndex() == 0){
			if(game.isOnlineGame()){
				try {
					game.moveOnline(field);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				game.move(field);
			}
		}
		//Debug-Mode aktivieren wenn eines der Homes von Spieler 1 aktiviert wird.
		if(this.original.equals(BoardView.colors[1])){
			counterActivateDebugMode++;
			if(counterActivateDebugMode == 5){
				game.toggleDebugMode();
				game.refresh("activateDebugMode");
				counterActivateDebugMode = 0;
			}
		}
		
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

}
