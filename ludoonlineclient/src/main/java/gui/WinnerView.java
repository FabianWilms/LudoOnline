package gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Main class of the game. Initializes the main components: GameBoard (model),
 * Game (controller) and Views.
 * 
 * @author F.Holtk�tter
 * @version 1.0
 */
public class WinnerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public WinnerView(Color winnerColor, int winner){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(225, 300, 400, 150);
		Image icon = new ImageIcon(getClass().getClassLoader().getResource("mensch.png")).getImage();
		setIconImage(icon);
		setTitle("Gewonnen!");
		contentPane = new JPanel();
		contentPane.setBackground(winnerColor);
		setContentPane(contentPane);
		
		JLabel lbl = new JLabel("Spieler " + winner + " hat gewonnen!");
		lbl.setFont (lbl.getFont ().deriveFont (32.0f));
		JLabel lbl2 = new JLabel("Herzlichen Gl�ckwunsch!");
		lbl2.setFont (lbl2.getFont ().deriveFont (28.0f));
		this.add(lbl);
		this.add(lbl2);
	}

}
