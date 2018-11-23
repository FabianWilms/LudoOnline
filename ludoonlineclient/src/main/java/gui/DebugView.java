package gui;

import app.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

/**
 * Lower Panel of the View. Conains a button to roll a number that is given by a textinput-field and lets the user play a recorded game.
 * 
 * @author F. Holtk�tter
 * @version 1.0
 */
public class DebugView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	/** button to roll */
	private final JButton button;
	/** Input for a value to be rolled */
	private final JTextField field;
	/** reference to the game */
	private final Game game;
	/** Button to run a prerecorded game */
	JButton runRecord;

	/**
	 * Custom-Constructor. Initializes the panel and its components.
	 * 
	 * @param game
	 *            Reference to the game.
	 */
	public DebugView(final Game game) {
		this.setVisible(false);
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setBackground(Color.black);
		this.game = game;
		this.game.addObserver(this);
		
		button = new JButton("W�rfle dies:");
		button.setBackground(Color.lightGray);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int number = 0;
				if (field.getText().matches(".*\\d.*"))
					number = Integer.valueOf(field.getText());
				if (0 < number && number < 7)
					game.roll(number);
				else {
					game.roll();
					field.setText("Not accepted");
				}

			}
		});
		
		field = new JTextField();
		field.setBorder(BorderFactory.createLineBorder(Color.black));
		field.setPreferredSize(new Dimension(300, 20));

		runRecord = new JButton("Run Record");
		runRecord.setBackground(Color.lightGray);
		runRecord.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					public Void doInBackground() {
						try {
							field.setText("Starting record...");
							Thread.sleep(900);
							field.setEnabled(false);
							BufferedReader reader = new BufferedReader(new FileReader("instructions.txt"));
							String next = reader.readLine();
							field.setText(next);
							while (next != null) {
								String[] array = next.split(",");
								if (array[0].equals("roll")) {
									game.roll(Integer.parseInt(array[1]));
								} else if (array[0].equals("move")) {
									game.move(Integer.parseInt(array[1]));
								}
								next = reader.readLine();
								field.setText(next);
								publish();
								Thread.sleep(20);
							}
							field.setEnabled(true);
							field.setText("Record done!");
							reader.close();
						} catch (Exception e) {
							field.setEnabled(true);
							field.setText("ERR: " + e.getMessage());
						}
						return null;
					}
				};
				worker.execute();
			}
		});
		add(button);
		add(field);
		add(runRecord);
	}

	/**
	 * update the status of this view so while a player is in state move no button can be pressed.
	 */
	public void update(Observable arg0, Object arg1) {
		this.setVisible(game.isInDebugMode());
		// U.u. W�rfel blockierren
		if (game.getNextMove() == 'm' || game.getNextMove() == 'f') {
			button.setEnabled(false);
			runRecord.setEnabled(false);
		} else {
			button.setEnabled(true);
			runRecord.setEnabled(true);
		}
	}

}
