package networkgame.server.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import networkgame.server.app.GameServer;

/**
 * Main class of the game-server.
 * 
 * @author F. Holtk�tter
 * @version 1.0
 */
public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private JLabel serverIP;
	private JLabel serverPort;
	private JLabel numberOfPlayers;
	
	private JLabel player1 = new JLabel("Player 1");
	private JLabel player2 = new JLabel("Player 2");
	private JLabel player3 = new JLabel("Player 3");
	private JLabel player4 = new JLabel("Player 4");
	
	private JLabel gameState = new JLabel("Game Status: Waiting for Players...");
	
	private List<JLabel> players = new ArrayList<JLabel>();
	public static Color[] colors = new Color[] { Color.WHITE, 
		new Color(0xFF4444), //Red
		new Color(0x33B5E5), //Blue
		new Color(0x99CC00), //Green
		new Color(0xFFBB33), //Yellow
		Color.DARK_GRAY};

	public Main(int port, int players) throws UnknownHostException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 180);
		setResizable(false);
		Image icon = new ImageIcon(getClass().getResource(
				"/resources/server.png")).getImage();
		setIconImage(icon);
		setTitle("Server - Ludo");
		
		this.players.add(player1);
		this.players.add(player2);
		this.players.add(player3);
		this.players.add(player4);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.black);
		
		serverIP = new JLabel("IP: " + InetAddress.getLocalHost() + " |");
		serverPort = new JLabel("Port: " + port + " |");
		numberOfPlayers = new JLabel("Spieleranzahl: " + players);
		serverIP.setForeground(colors[0]);
		serverPort.setForeground(colors[0]);
		numberOfPlayers.setForeground(colors[0]);
		
		contentPane.add(serverIP, BorderLayout.NORTH);
		contentPane.add(serverPort, BorderLayout.NORTH);
		contentPane.add(numberOfPlayers, BorderLayout.NORTH);
		
		for(int i = 0; i < players; i++){
			JLabel next = this.players.get(i);
			next.setForeground(colors[0]);
			next.setFont(next.getFont().deriveFont(16.0f));
			next.setHorizontalAlignment(SwingConstants.CENTER);
			
			next.setPreferredSize(new Dimension(80,40));
			next.setBorder(BorderFactory.createDashedBorder(colors[5], 3, 2, 1, false));
			
			contentPane.add(next, BorderLayout.CENTER);
		}
		
    	gameState.setBorder(BorderFactory.createLineBorder(Color.orange,3));
    	gameState.setForeground(colors[0]);
    	gameState.setPreferredSize(new Dimension(350,40));
    	gameState.setFont(gameState.getFont().deriveFont(16.0f));
    	gameState.setHorizontalAlignment(SwingConstants.CENTER);
    	contentPane.add(gameState, BorderLayout.SOUTH);
		
		setContentPane(contentPane);
	}
	
	/**
	 * Updates the view when a player connects.
	 * @param index Index of the Player in the list of connections.
	 */
	public void playerConnected(int index){
		JLabel next = players.get(index);
		next.setBorder(BorderFactory.createLineBorder(colors[index+1],3));
	}
	
	/**
	 * Updates the View when a player disconnects.
	 * @param index Index of the Player in the list of connections.
	 */
	public void playerDisconnected(int index){
		JLabel next = players.get(index);
		next.setBorder(BorderFactory.createDashedBorder(colors[5], 3, 2, 1, false));
	}
	
	/**
	 * Updates the View depending on the state of the game.
	 * @param gameStarted State of the game(Started=true, Waiting=false;
	 */
	public void setGameState(boolean gameStarted){
		if(gameStarted){
			gameState.setText("Game Status: Running...");
        	gameState.setBorder(BorderFactory.createLineBorder(Color.green,3));
		}else{
			gameState.setText("Game Status: Waiting for Players...");
        	gameState.setBorder(BorderFactory.createLineBorder(Color.yellow,3));
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				int serverPort = Integer.valueOf(JOptionPane.showInputDialog("Please enter the Port the Server should run on.", "8888"));
				int numberOfPlayers = -1;
				while(numberOfPlayers < 0){
						numberOfPlayers = JOptionPane.showOptionDialog(null,
						"Something", 
						"Title", 
						JOptionPane.DEFAULT_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						new Integer[]{2,3,4}, 
						null);
				}
				
				Main frame;
				try {
					frame = new Main(serverPort, numberOfPlayers+2);
					new Thread(new GameServer(serverPort, numberOfPlayers+2, frame)).start();
					frame.setVisible(true);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
