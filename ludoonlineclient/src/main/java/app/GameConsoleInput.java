package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class extends the game by giving the option to control it with the command line.
 * There is no
 * 
 * @author F. Holtk�tter, A. Birkel
 *
 */
public class GameConsoleInput extends Thread {
	Game game;
	
	public GameConsoleInput(Game game){
		this.game = game;
	}
	
	@Override
	public void run() {
		while(true){
			try{
			    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			    String next = br.readLine();
			    String[] array = next.split(",");
			    if (array[0].equals("roll")) {
					game.roll(Integer.parseInt(array[1]));
				} else if (array[0].equals("move")) {
					game.move(Integer.parseInt(array[1]));
				} else {
					System.out.println("---Ung�ltiger Befehl!---");
					System.out.println("G�ltige Befehle: move,<Feldnummer> || roll,<W�rfelziffer>");
				}
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

}
