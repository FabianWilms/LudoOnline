package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class PlayerConnection extends Thread {
	
	private int clientID = -1;
	
	private GameServer server;
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public PlayerConnection(GameServer s, Socket client) {
		this.server = s;
		this.client = client;
		try {
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				in.close();
				out.close();
				e.printStackTrace();
			} catch (IOException ex) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {
			while (true) {
				String line = (String) in.readObject();
				if (line != null)
					server.broadcast(line);
			}
		} catch (Exception e) {
			System.out.println("client disconnected");
			server.removeConnection(this);
		}

	}

	public void print(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
		}
	}
	
	public void closeClientConnection(){
		try {
			this.print("DISCONNECT#No more Connections allowed, disonnecting...");
			client.close();
		} catch (IOException e) {
			System.out.println("ERROR in closeClientConnection:");
			e.printStackTrace();
		}
	}
	
	public void setClientID(int id){
		this.clientID = id;
	}
	
	public int getClientID(){
		return clientID;
	}

}
