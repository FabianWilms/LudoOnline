package gui;

import app.Game;
import app.GameBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main class of the game. Initializes the main components: GameBoard (model),
 * Game (controller) and Views.
 * 
 * @author U.Hammerschall, F. Holtk�tter, A. Birkel
 * @version 1.0
 */
public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private static JMenuBar menuBar;

	private static JMenu game;
	private static JMenu newGame;
	private static JMenuItem twoPlayers;
	private static JMenuItem threePlayers;
	private static JMenuItem fourPlayers;
	private static JMenuItem onlineGame;

	private static JMenu help;
	private static JMenuItem faq;
	private static JMenuItem about;

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 750);
		Image icon = new ImageIcon(getClass().getClassLoader().getResource("mensch.png")).getImage();
		setIconImage(icon);
		setTitle("Ludo");

		menuBar = new JMenuBar();

		game = new JMenu("Game");
		newGame = new JMenu("New Game");
		twoPlayers = new JMenuItem("2 Players");
		twoPlayers.addActionListener(this);
		threePlayers = new JMenuItem("3 Players");
		threePlayers.addActionListener(this);
		fourPlayers = new JMenuItem("4 Players");
		fourPlayers.addActionListener(this);
		onlineGame = new JMenuItem("Play Online");
		onlineGame.addActionListener(this);

		help = new JMenu("Help");
		faq = new JMenuItem("FAQ");
		faq.addActionListener(this);
		about = new JMenuItem("About");
		about.addActionListener(this);
		
		//restartGUI = new JMenuItem("Refresh");
		//restartGUI.addActionListener(this);

		menuBar.add(game);
		menuBar.add(help);
		game.add(newGame);
		newGame.add(twoPlayers);
		newGame.add(threePlayers);
		newGame.add(fourPlayers);
		game.add(onlineGame);
		//game.add(restartGUI);
		help.add(faq);
		help.add(about);

		this.setJMenuBar(menuBar);

		contentPane = new JPanel();
		contentPane.setBackground(Color.black);
		setContentPane(contentPane);

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main frame = new Main();
				
				GameBoard board = new GameBoard();
				Game game = new Game(board, 0);
				BoardView boardView = new BoardView(game, board);
				
				frame.add(new LaunchedView());
				frame.add(boardView, BorderLayout.CENTER);
				frame.setVisible(true);
			}
		});
	}

	public void startAGame(Boolean playOnline, int playersForOfflineGame)throws Exception {
		this.getContentPane().removeAll();
		Game game;
		GameBoard board = new GameBoard();
		
		if (playOnline) {
			String serverIP = JOptionPane.showInputDialog("Please enter IP of the Game-Server", "localhost");
			int serverPort = Integer.valueOf(JOptionPane.showInputDialog("Please enter Port of the Game-Server", "8888"));
			
			game = new Game(board, new Socket(serverIP,serverPort));
			this.getContentPane().add(new OnlineDiceView(game), BorderLayout.NORTH);
			game.runGameThread();
			
		} else {
			game = new Game(board, playersForOfflineGame);
			this.getContentPane().add(new DiceView(game), BorderLayout.NORTH);
			this.getContentPane().add(new DebugView(game), BorderLayout.SOUTH);
		}
		this.getContentPane().add(new BoardView(game, board),BorderLayout.CENTER);
		this.getContentPane().repaint();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent object) {
		try {
			if (object.getSource() == twoPlayers) {
				this.startAGame(false, 2);
			} else if (object.getSource() == threePlayers) {
				this.startAGame(false, 3);
			} else if (object.getSource() == fourPlayers) {
				this.startAGame(false, 4);
			} else if (object.getSource() == onlineGame) {
				this.startAGame(true, 0);
			} else if (object.getSource() == faq) {
				JOptionPane.showMessageDialog(this,
						"Normales Mensch �rgere Dich Nicht");
			} else if (object.getSource() == about) {
				JOptionPane
						.showMessageDialog(
								this,
								"Programmiert im Ramen einer Projektarbeit im 2 Semester Softwareentwicklung an "
								+ "der Hochschule M�nchen\n\nmade by Fabian Holtk�tter");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
}
