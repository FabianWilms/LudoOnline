package app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

/**
 * Master of the game. Manages game logic and state changes. Represents the
 * interface to the view.
 * 
 * @author U. Hammerschall, F. Holtk�tter, A. Birkel
 * @version 1.0
 */
public class Game extends Observable implements Runnable {

	/** defines number of expected players */
	private int NUMBER_OF_PLAYERS;
	/** list of all players */
	private final List<Player> players = new ArrayList<Player>();
	/** list of all simple game fields */
	private final List<Field> fields;
	/** current value of the dice */
	private int dice;
	/** curren Player */
	private int currentPlayer;
	/** the next move that will happen */
	private char nextMove;
	/** number of rolls a player still has */
	private int rolls;
	/** Boolean-Value to check if this player made a correct move */
	private boolean correctMove;
	/** Boolean that checks if its the first move of the current player */
	private boolean firstMove = true;
	/** Is True if the current Player cant move his pawn into the finish because it's blocked */
	private boolean cantMoveToFinish = false;
	/** Debug-Mode enabled or not */
	private boolean debugMode = false;

	/**
	 * Initializes a new game, selects the first player, sets current dice-value
	 * to zero, nextMove to "roll" and gives the first player three rolls.
	 * 
	 * @param board
	 *            : model of the game.
	 */
	public Game(GameBoard board, int numberOfPlayers) {
		NUMBER_OF_PLAYERS = numberOfPlayers;
		fields = board.getFields();
		// initialize four players
		for (int i = 1; i <= NUMBER_OF_PLAYERS; i++) {
			int startIndex = (i - 1) * 10;
			int endIndex = startIndex - 1;
			if (endIndex == -1) {
				endIndex = 39;
			}
			Player player = new Player(i, board, this, startIndex, endIndex);
			players.add(player);
		}
		// Set first player
		currentPlayer = 1;
		dice = 0;
		nextMove = 'r';
		rolls = 3;
	}

	/**
	 * roll a random number between 1 and 6 and call roll(int number) with this
	 * value. (used by Button click)
	 */
	public void roll() {
		roll((int) ((Math.random()) * 6 + 1));
	}

	/**
	 * handles the rolled number by the current player.
	 * 
	 * @param number
	 *            of dice
	 */
	public void roll(int number) {
		if (nextMove == 'r') {
			dice = number;
			boolean movePossible = isMovePossible();
			if (movePossible) {
				boolean wasAutomatic = moveAutomatic();
				if (wasAutomatic) {
					if (dice == 6) {
						rolls = 2;
					} else {
						rolls = 0;
					}
					nextMove = 'r';
				} else {
					nextMove = 'm';
				}
				// Wenn dies das erste mal war, dass der Spieler gew�rfelt hat,
				// und seine Spielsteine sich im/vor dem Ziel blockieren, darf
				// er drei mal W�rfeln.
			} else if (firstMove && cantMoveToFinish) {
				rolls = 3;
				nextMove = 'r';
			} else {
				nextMove = 'r';
			}
			firstMove = false;
			refresh();
		}
	}

	/**
	 * Checks if a move is possible by the current player with the current value
	 * of dice.
	 * 
	 * @return if move is Possible
	 */
	public boolean isMovePossible() {
		Player current = players.get(currentPlayer - 1);
		int playerStartIndex = current.getStartIndex();
		int playerEndIndex = current.getEndIndex();
		Field startField = fields.get(playerStartIndex);

		// Zun�chst �berpr�fung ob eigene Figur auf dem Startfeld steht und
		// ziehen k�nnte, sonst false, da immer das Startfeld frie gemacht
		// werden muss.
		if (startField.hasPawn()
				&& startField.getPawn().getIndex() == currentPlayer) {
			if (fields.get(playerStartIndex + dice).hasPawn()
					&& fields.get(playerStartIndex + dice).getPawn().getIndex() == currentPlayer) {
				return false;
			}
		}

		// Solange man Figuren im Haus hat, und sich auf dem Startfeld keiner,
		// oder ein gegnerischer Spieler befindet, kann man mit einer 6 immer
		// ziehen.
		if (current.getHomePawns() > 0) {
			if (dice == 6) {
				if (!startField.hasPawn() || startField.hasPawn()
						&& startField.getPawn().getIndex() != currentPlayer) {
					return true;
				}
			}
		}

		// Iteriere �ber das Spielfeld und �berpr�fe f�r jede Figur des
		// aktuellen Spielers ob sie ziehen k�nnte. Sobald eine Figur ziehen
		// kann, wird true zur�ckgeliefert.
		for (int index = 0; index < 40; index++) {
			Field curField = fields.get(index);
			Field nextField;

			// �berpr�fe, ob es sich um eine Figur des aktuellen Spielers
			// handelt.
			if (curField.hasPawn()
					&& curField.getPawn().getIndex() == currentPlayer) {
				// Zuerst �berpr�fung ob ein Zielfeld erreicht wird/werden kann
				if (index >= playerEndIndex - 5 && index <= playerEndIndex
						&& (index + dice) - playerEndIndex >= 1) {
					if (current.getEndPawns() == 0) {
						return true;
					} else {
						int fieldInEnd = (index + dice) - (playerEndIndex + 1);
						if (fieldInEnd <= 3
								&& !current.getEndFields().get(fieldInEnd)
										.hasPawn()) {
							return true;
						} else {
							// Ziel blockiert, also:
							cantMoveToFinish = true;
						}
					}
				}

				// Nun �berpr�fen ob Z�ge auf �ffentliche Felder m�glich sind.
				int tmp;
				if (index + dice > 39) {
					tmp = (index + dice) - 40;
					nextField = fields.get(tmp);
				} else {
					tmp = index + dice;
					nextField = fields.get(tmp);
				}

				// Wenn keine Figur/eine gegnerische Figur auf dem n�chsten Feld
				// steht...
				if (!cantMoveToFinish
						&& (!nextField.hasPawn() || nextField.hasPawn()
								&& nextField.getPawn().getIndex() != currentPlayer)) {
					return true;
				}

				// Wenn man sich im "Zieleinlauf" befindet, aber die W�rfelzahl
				// nicht ausreicht um ins Ziel zu gelangen...
				if (cantMoveToFinish
						&& tmp >= playerEndIndex - 5
						&& tmp <= playerEndIndex
						&& (!nextField.hasPawn() || nextField.hasPawn()
								&& nextField.getPawn().getIndex() != currentPlayer)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Moves automatically if all pawns are in home and dice == 6 or if one of
	 * his pawns blocks his startField. This method then returns true.
	 * 
	 * @return if this is an automatic move
	 */
	public boolean moveAutomatic() {
		Player current = players.get(currentPlayer - 1);
		int playerStartIndex = current.getStartIndex();
		Field startField = fields.get(playerStartIndex);
		Field nextField = fields.get(playerStartIndex + dice);

		// Wenn sich noch Figuren im Haus befinden und dice=6...
		if (current.getHomePawns() > 0 && dice == 6) {
			// ...w�hle eine Figur aus dem Haus und setze sie automatisch auf
			// das Startfeld.
			if (!startField.hasPawn()) {
				current.moveFromHome();
				fields.get(playerStartIndex).setPawn(new Pawn(currentPlayer));
				return true;
			}

			// Wenn Startfeld = gegnerische Figur , schmei�e den gegnerischen
			// Spieler und setze eine Figur aus dem Haus auf das Startfeld.
			if (startField.hasPawn()
					&& startField.getPawn().getIndex() != currentPlayer) {
				kickPlayer(startField);
				current.moveFromHome();
				fields.get(playerStartIndex).setPawn(new Pawn(currentPlayer));
				return true;
			}

			// Wenn Startfeld = eigene Figur bewege die blockierende Figur um 6
			// Felder weiter.
			if (startField.hasPawn()
					&& startField.getPawn().getIndex() == currentPlayer
					&& !nextField.hasPawn()) {
				fields.get(playerStartIndex).setPawn(null);
				nextField.setPawn(new Pawn(currentPlayer));
				return true;
			}
		}

		if (startField.hasPawn()
				&& startField.getPawn().getIndex() == currentPlayer) {
			if (!nextField.hasPawn()) {
				nextField.setPawn(new Pawn(currentPlayer));
			} else if (nextField.getPawn().getIndex() != currentPlayer) {
				kickPlayer(nextField);
				nextField.setPawn(new Pawn(currentPlayer));
			}
			startField.setPawn(null);
			return true;
		}
		return false;
	}

	/**
	 * Removes an enemy players pawn from the field this player wants to move
	 * to.
	 */
	public void kickPlayer(Field field) {
		int enemy = field.getPawn().getIndex();
		players.get(enemy - 1).setPawnOnHome();
	}

	/**
	 * move pawn situated on the field.(Used by player-click)
	 * 
	 * @param field
	 *            : field where pawn is situated.
	 */
	public void move(Field field) {
		move(fields.indexOf(field));
	}

	/**
	 * move pawn situated on the field indicated by index.
	 * 
	 * @param fieldIndex
	 *            : index of the field where pawn is situated.
	 */
	public void move(int fieldIndex) {
		if (nextMove == 'm' && fields.get(fieldIndex).hasPawn()
				&& fields.get(fieldIndex).getPawn().getIndex() == currentPlayer) {
			correctMove = false;
			boolean finishOccupied = false;

			Player current = players.get(currentPlayer - 1);
			int playerEndIndex = current.getEndIndex();
			int fieldIndexInEnd = fieldIndex + dice - playerEndIndex - 1;

			Field curField = fields.get(fieldIndex);
			Field newField;

			// Move auf Zielfeld
			if (fieldIndex >= playerEndIndex - 5
					&& fieldIndex <= playerEndIndex
					&& (fieldIndexInEnd + 1 >= 1)) {
				if (fieldIndexInEnd <= 3
						&& current.getEndPawns() == 0
						|| fieldIndexInEnd <= 3
						&& !current.getEndFields().get(fieldIndexInEnd)
								.hasPawn()) {
					curField.setPawn(null);
					current.getEndFields().get(fieldIndexInEnd)
							.setPawn(new Pawn(currentPlayer));
					correctMove = true;
				} else {
					finishOccupied = true;
				}
			}

			// Move auf normales Feld, au�er es wurde bereits ins Zielfelds
			// gesetzt.
			if (!correctMove && !finishOccupied) {
				// Korrektur f�r Spielfeldgrenze
				if (fieldIndex + dice > 39) {
					newField = fields.get(fieldIndex + dice - 40);
				} else {
					newField = fields.get(fieldIndex + dice);
				}

				if (!newField.hasPawn()) {
					curField.setPawn(null);
					newField.setPawn(new Pawn(currentPlayer));
					correctMove = true;
				} else if (newField.getPawn().getIndex() != currentPlayer) {
					kickPlayer(newField);
					curField.setPawn(null);
					newField.setPawn(new Pawn(currentPlayer));
					correctMove = true;
				} else if (newField.getPawn().getIndex() == currentPlayer) {
					correctMove = false;
				}
			}

			if (correctMove) {
				if (dice == 6) {
					rolls = 2;
				} else {
					rolls = 0;
				}
				refresh();
			}
		}
	}

	/**
	 * Sets the next player as currentPlayer.
	 */
	public void nextPlayer() {
		if (currentPlayer == NUMBER_OF_PLAYERS) {
			currentPlayer = 1;
		} else {
			currentPlayer += 1;
		}
	}

	public void toggleDebugMode(){
		debugMode = !debugMode;
	}
	
	public boolean isInDebugMode(){
		return debugMode;
	}
	
	public int getDice() {
		return dice;
	}

	public int getRolls() {
		return rolls;
	}

	public char getNextMove() {
		return nextMove;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * notify View about changes in the model.
	 */
	public void refresh() {
		// Wenn alle Figuren des aktuellen Spielers im Ziel sind, hat er
		// gewonnen.
		if (players.get(currentPlayer - 1).getEndPawns() == 4) {
			nextMove = 'f';
		}
		// Wenn der aktuelle Spieler korrekt gezogen ist und er auch ziehen
		// durfte...
		if (correctMove && nextMove == 'm') {
			if (dice == 6) {
				rolls = 2;
			} else {
				rolls = 0;
			}
			nextMove = 'r';
		}
		// Wenn der aktuelle Spieler nicht ziehen konnte...
		if (nextMove == 'r') {
			rolls -= 1;
			if (rolls <= 0) {
				nextPlayer();
				rolls = players.get(currentPlayer - 1).getRolls();
				firstMove = true;
				cantMoveToFinish = false;
			}
		}
		correctMove = false; // correctMove zur�cksetzen
		setChanged();
		notifyObservers();
	}
	
	public void refresh(String activate){
		System.out.println(activate);
		setChanged();
		notifyObservers();
	}

	/**
	 * String representation of the current game state
	 */
	public String toString() {
		String output = "";
		output += currentPlayer;
		output += nextMove;
		output += dice;
		for (int i = 0; i < players.size(); i++) {
			output += players.get(i).toString();
		}
		output += fields.toString();
		return output;
	}

	// ###########################Methods for Online-Game############################//
	private boolean onlineGame;
	private int clientPlayer;
	private GameBoard board;
	
	private Socket serverSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread thread;

	
	public Game(GameBoard board, Socket socket) throws Exception {
		onlineGame = true;
		this.board = board;
		fields = board.getFields();
		
		// Mit Server verbinden...
		this.serverSocket = socket;
		out = new ObjectOutputStream(serverSocket.getOutputStream());
		in = new ObjectInputStream(new BufferedInputStream(serverSocket.getInputStream()));
		
		// Erste Nachricht des Server lesen (Index dieses Clients||Spiel bereits voll)
		String[] tmp = ((String) in.readObject()).split("#");
		if(tmp[0].equals("COLOR")){
			clientPlayer = Integer.valueOf(tmp[1]);
		}else if(tmp[0].equals("FULL")){
			throw new Exception(tmp[1]);
		}
	}
	
	/**
	 * Starts the Thread to listen for Server-Messages.
	 */
	public void runGameThread(){
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	/**
	 * Method that waits for the server-response to start the game.
	 * @return True, if game was started correctly.
	 * @throws Exception
	 */
	public boolean waitForStart() throws Exception{
		//Auf Spielstart warten
		String something;
		synchronized(in){
			something = (String) in.readObject();
		}
		String[] tmp = something.split("#");
		if(tmp[0].equals("START")){
			NUMBER_OF_PLAYERS = Integer.valueOf(tmp[1]);
			return true;
		}
		return false;
	}
	
	/**
	 * Starts the game with the server-given number of players.
	 */
	public void initializeGame() {
		// initialize four players
		for (int i = 1; i <= NUMBER_OF_PLAYERS; i++) {
			int startIndex = (i - 1) * 10;
			int endIndex = startIndex - 1;
			if (endIndex == -1) {
				endIndex = 39;
			}
			Player player = new Player(i, board, this, startIndex, endIndex);
			players.add(player);
		}
		// Set first player
		currentPlayer = 1;
		dice = 0;
		nextMove = 'r';
		rolls = 4;
	}

	/**
	 * Checks if this player has the rights to roll and sends the command to the server.
	 * @throws IOException
	 */
	public void rollOnline() throws IOException {
		if(currentPlayer == clientPlayer){
			int rolled = (int) ((Math.random()) * 6 + 1);
			out.writeObject("ROLL#" + rolled);
		}
	}

	/**
	 * Checks if this player has the rights to move and sends the command to the server.
	 * @param field Fieldnumber the player has clicked.
	 * @throws IOException
	 */
	public void moveOnline(Field field) throws IOException {
		if(currentPlayer == clientPlayer && field.getPawn().getIndex() == clientPlayer){
			int indexOfField = fields.indexOf(field);
			out.writeObject("MOVE#" + indexOfField);
		}
	}
	
	public boolean isOnlineGame(){
		return onlineGame;
	}
	
	public int getClientIndex(){
		return clientPlayer;
	}
	
	public int getNumberOfPlayers(){
		return NUMBER_OF_PLAYERS;
	}

	public void run() {
		String command;
		String message;
		try {
			this.waitForStart();
			this.initializeGame();
			this.refresh();
			
			while (true) {
				synchronized(in){
					command = (String) in.readObject();
				}
				if (command != null) {
					String[] tmp = command.split("#");
					command = tmp[0];
					message = tmp[1];

					if (command.equals("ROLL")) {
						this.roll(Integer.valueOf(message));
					}
					if (command.equals("MOVE")) {
						this.move(Integer.valueOf(message));
					}
					if (command.equals("ERROR")) {
						JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
						break;
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
	}

}
