package app;

import gui.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class GameServer implements Runnable {

	/** Port this Server should be started on */
	private final int port;
	/** Maximum number of players for this game */
	private final int MAX_PLAYERS;
	/** Frame of this servers GUI */
	private final Main frame;
	/** Socket of this server */
	private ServerSocket server;
	/** List of clients that are currently connected to this server */
	private PlayerConnection[] gameClients;
	/** True if the game started */
	private boolean gameStarted;

	
	public GameServer(int port, int maxPlayers, Main frame) {
		this.gameStarted = false;
		this.port = port;
		this.MAX_PLAYERS = maxPlayers;
		this.frame = frame;
		try {
			server = new ServerSocket(this.port);
		} catch (IOException e) {
			try {
				server.close();
			} catch (IOException ex) {
			}
		}
		gameClients = new PlayerConnection[MAX_PLAYERS];
	}

	/**
	 * Thread that waits for clients to connect. 
	 * Connected client then gets added to the list of connected clients.
	 */
	public void run() {
		try {
			while (true) {
				Socket client = server.accept();
				synchronized (this) {this.addConnection(new PlayerConnection(this, client));}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void broadcast(String msg) {
		synchronized (gameClients) {
			for(PlayerConnection next : gameClients){
				next.print(msg);
			}
		}
	}

	/**
	 * Adds a new PlayerConnection to the list of clients if MAX_PLAYERS isn't reached.
	 * When alls players are connected the game is started.
	 * @param c
	 */
	public void addConnection(PlayerConnection c) {
		boolean added = false;
		boolean allClientsReady = false;
		for(int i = 0; !added && i < MAX_PLAYERS; i++){
			if(gameClients[i] == null){
				gameClients[i] = (c);
				c.setClientID(i);
				c.print("COLOR#" + (i+1));
				frame.playerConnected(i);
				added = true;
				if(i==MAX_PLAYERS-1){
					allClientsReady = true;
				}
			} else if(i==MAX_PLAYERS-1){
				c.print("FULL#Server is full. Please try again later.");
				c.closeClientConnection();
			}
		}
		
		if(allClientsReady){
			this.broadcast("START#" + MAX_PLAYERS);
			this.gameStarted = true;
			frame.setGameState(gameStarted);
		}
	}
	
	public void removeConnection(PlayerConnection c){
		System.out.println("Spieler der disconnected: " + c.getClientID());
		gameClients[c.getClientID()] = null;
		frame.playerDisconnected(c.getClientID());
		if(gameStarted){
			this.broadcast("ERROR#Ein Spieler hat die Verbindung geschlossen. Spiel wird beendet...");
			this.gameStarted=false;
			frame.setGameState(gameStarted);
		}
	}
}
