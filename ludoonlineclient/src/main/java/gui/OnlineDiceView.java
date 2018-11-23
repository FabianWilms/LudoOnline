package gui;

import app.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Upper panel of the game view when playing online. Allows interaction of the player with the game.
 * Controls the run of the game.
 * 
 * @author F. Holtk�tter
 * @version 1.0
 */
public class OnlineDiceView extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	/** button to roll */
	private final JButton button;
	/** Shows player 1 */
	private final JLabel player1;
	/** Shows player 2 */
	private final JLabel player2;
	/** Shows player 3 */
	private final JLabel player3;
	/** Shows player 4 */
	private final JLabel player4;
	/** reference to the game */
	private final Game game;
	/** index of the current player */
	private int index = 1;
	/** This clients color */
	private Color clientColor;

	/**
	 * Custom-Constructor. Initializes the panel and its components.
	 * 
	 * @param game
	 *            Reference to the game.
	 */
	public OnlineDiceView(final Game game) {
		this.setPreferredSize(new Dimension(500,90));
		this.setBackground(Color.black);
		this.game = game;
		this.game.addObserver(this);
		this.clientColor = BoardView.colors[game.getClientIndex()];
		this.setBorder(BorderFactory.createLineBorder(clientColor, 3));
		
		player1 = new JLabel("...");
		player1.setForeground(BoardView.colors[1]);
		player1.setFont(player1.getFont().deriveFont(16.0f));
		player1.setPreferredSize(new Dimension(80,40));
		player1.setHorizontalAlignment(SwingConstants.CENTER);
		player1.setBorder(BorderFactory.createLineBorder(Color.black));
		
		player2 = new JLabel("...");
		player2.setForeground(BoardView.colors[2]);
		player2.setFont(player2.getFont().deriveFont(16.0f));
		player2.setPreferredSize(new Dimension(80,40));
		player2.setHorizontalAlignment(SwingConstants.CENTER);
		player2.setBorder(BorderFactory.createLineBorder(Color.black));
		
		player3 = new JLabel("...");
		player3.setForeground(BoardView.colors[3]);
		player3.setFont(player3.getFont().deriveFont(16.0f));
		player3.setPreferredSize(new Dimension(80,40));
		player3.setHorizontalAlignment(SwingConstants.CENTER);
		player3.setBorder(BorderFactory.createLineBorder(Color.black));
		
		player4 = new JLabel("...");
		player4.setForeground(BoardView.colors[4]);
		player4.setFont(player4.getFont().deriveFont(16.0f));
		player4.setPreferredSize(new Dimension(80,40));
		player4.setHorizontalAlignment(SwingConstants.CENTER);
		player4.setBorder(BorderFactory.createLineBorder(Color.black));
		
		switch(game.getClientIndex()){ 
        case 1: 
        	player1.setText("Warten...");
        	break;
        case 2: 
        	player2.setText("Warten...");
        	break;
        case 3: 
        	player3.setText("Warten...");
        	break;
        case 4: 
        	player4.setText("Warten...");
        	break;
        }

		ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource("waiting.gif"));
		
		button = new JButton("", tmpImage);
		button.setBorder(BorderFactory.createLineBorder(Color.black,6));
		button.setContentAreaFilled(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					game.rollOnline();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
				}
			}

		});
		
		add(player1);
		add(player2);
		add(button);
		add(player3);
		add(player4);
	}

	/**
	 * update dice, current player and next move on the user interface.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		index = game.getCurrentPlayer();
		
		// Button anpassen
		ImageIcon tmpImage = new ImageIcon(getClass().getClassLoader().getResource(game.getDice() + ".png"));
		button.setIcon(tmpImage);
		
		player1.setText("Player 1");
		player2.setText("Player 2");
		player3.setText("Player 3");
		player4.setText("Player 4");
		
		switch(game.getClientIndex()){ 
        case 1: 
        	player1.setText("You");
        	break;
        case 2: 
        	player2.setText("You");
        	break;
        case 3: 
        	player3.setText("You");
        	break;
        case 4: 
        	player4.setText("You");
        	break;
        }
		
		if(game.getCurrentPlayer() == 1){
			player1.setForeground(BoardView.colors[1]);
		}else{
			player1.setForeground(BoardView.colors[5]);
		}
		
		if(game.getCurrentPlayer() == 2){
			player2.setForeground(BoardView.colors[2]);
		}else{
			player2.setForeground(BoardView.colors[5]);
		}
		
		if(game.getCurrentPlayer() == 3){
			player3.setForeground(BoardView.colors[3]);
		}else if (game.getNumberOfPlayers() >= 3){
			player3.setForeground(BoardView.colors[5]);
		}else {
			player3.setForeground(Color.black);
		}
		
		if(game.getCurrentPlayer() == 4){
			player4.setForeground(BoardView.colors[4]);
		}else if (game.getNumberOfPlayers() == 4){
			player4.setForeground(BoardView.colors[5]);
		}else{
			player4.setForeground(Color.black);
		}

		// U.u. W�rfel blockierren
		if (game.getNextMove() == 'm') {
			button.setEnabled(false);
		} else if (game.getNextMove() == 'f'){
			button.setEnabled(false);
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						WinnerView frame = new WinnerView(BoardView.colors[index], index);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			button.setEnabled(true);
		}
	}

}
