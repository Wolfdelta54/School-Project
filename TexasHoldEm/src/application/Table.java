package application;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingNode;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Table implements Runnable /* extends Application */
{
	// Multiplayer variables
	public static int portNumber = 4444;
	
	private int serverPort;
	public List<ClientThread> clients;
	ArrayList<Boolean> hasMatched = new ArrayList<Boolean>(); // Used to determine if betting round has ended

	private int pot = 0; // initializes pot to 0
	private ArrayList<Player> players = new ArrayList<Player>(); // list of players and their attributes
	private final River riverCards = new River();
	private final DeckOfCards deck = new DeckOfCards();
	private int port = 4444;
	public Label ip = new Label();
	public GridPane ipPane = new GridPane();
	// Betting rounds are as follows: 0 - first bet, right after deal; 1 - first 3 cards of river have been shown;
	//		2 - first 4 cards of river have been shown; 3 - first 5 cards of river have been shown
	// After round 3 of betting the hands of all currently active players are shown and they go though the hand checker
	// Player with the best hand gets the pot then the next hand is dealt and betting commences again
	public int curRnd = 0;
	public final int maxRnds = 3;
	public int highBet = -50; // Stores the highest bet, used to see if all players have bet this amount or have gone all in
	
	// Pane of components that will constantly change
	public Group tablePane = new Group();
	public GridPane riverPane = new GridPane();
	public ArrayList<SwingNode> riverImgs = new ArrayList<SwingNode>(); // Stores the images for the river
	public ArrayList<GridPane> handList = new ArrayList<GridPane>(); // Stores the GridPanes used to hold the images of every player's cards
	public ArrayList<GridPane> playerList = new ArrayList<GridPane>(); // Stores the GridPanes used to hold all of the info for each player
	
	public boolean srvrLive = false;
	

	public Table()
	{
		this.serverPort = 4444;
		players = new ArrayList<Player>();
		deck.shuffle(); // shuffles deck
	}

	public Table(int portNumber) {
		this.serverPort = portNumber;
		players = new ArrayList<Player>();
		deck.shuffle();
	}
	
	public boolean isLive() {
		return srvrLive;
	}
	
	public void setLive(boolean x) {
		srvrLive = x;
	}
	
	public GridPane getIpPane() {
		return ipPane;
	}
	
	public Label getIP() {
		return ip;
	}
	
	public Group getPlayerPane() {
		return tablePane;
	}
	
	public GridPane getRiverPane() {
		return riverCards.getPane();
	}
	
	public int getNumPlayers() {
		return players.size();
	}
	
	public void setRiverPane() {
		riverPane = riverCards.getPane();
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		System.out.println(player);
	}
	
	public void removePlayer(Player player) {
		String toRemove = player.getName();
		
		for(int i = 0; i < players.size(); i++) {
			String user = players.get(i).getName();
			
			if(user.equals(toRemove)) {
				players.remove(i);
			}
		}
	}

	public int getPot()
	{		
		return pot; // returns pot
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public River getRiver() {
		return riverCards;
	}

	public void deal()
	{
		for(int i = 0; i < 2; i ++)
		{
			for(int j = 0; j < players.size(); j++)
			{
				players.get(j).addCard(deck.nextCard()); // adds a card to the player's deck
			}

		}

		
		for(int i = 0; i < 5; i++)
		{
			riverCards.addCard(deck.nextCard()); // adds 5 cards to the river	
		}
		riverCards.updateImgs();
		riverImgs = riverCards.getCardNodes();
		deck.shuffle();
	}
	
	public void doAction(String input) {
	//	input = input.replace("\n", "");
		
		String user = input.substring(0, input.indexOf(";")); // Get the username of who executed the action
		String action = ""; // Get the action executed
		int amount = 0; // If needed, bet amount storage
		
		
		input = input.replace(user + ";", "");
		if(input.indexOf(";") != -1) {
			action = input.substring(0, input.indexOf(";"));
			amount = Integer.parseInt(input.substring(input.indexOf(";") + 1));
		}
		else {
			action = input.substring(input.indexOf(";") + 1);
		}
		ArrayList<String> names = new ArrayList<String>();
		
		// Go through the list of players looking for the one who's username matches that of the executor
		for(int i = 0; i < players.size(); i++) {
			names.add(players.get(i).getName());
			if(user.equals(players.get(i).getName())) {
				// Set active to false if the action was Fold
				if(action.equalsIgnoreCase("Fold")) {
					players.get(i).setActive(false);
				}
				// Update balance of the executor and update the current bet
				else if(action.equalsIgnoreCase("Bet") || action.equalsIgnoreCase("Raise") || action.equalsIgnoreCase("Call")) {
					pot = pot + amount;
					players.get(i).updateBal(0 - amount);
					
					if(amount > highBet) {
						highBet = amount;
					}
					
					for(int j = 0; j < players.size(); j++) {
						players.get(j).setCurBet(amount);
						players.get(j).setPot(pot);
						System.out.println("Current Pot (Table) " + pot);
						System.out.println("Current Bet (Table) " + amount);
					}
					System.out.println(players.get(i) + " Table");
				}
				// Do nothing
				else if(action.equalsIgnoreCase("Check")) {
					System.out.println("Checking is basically doing nothing");
				}
			}
		}
		
		if(!names.contains(user)) {
			addPlayer(new Player(user));
			System.out.println(players.size());
		}
	}
	
	public void prntPlayers() {
		for(int i = 0; i < players.size(); i++) {
			System.out.println(players.get(i));
		}
	}
	
	public void sendStatus() {
		for(int i = 0; i < players.size(); i++) {
			players.get(i).setLive(srvrLive);
		}
	}
	
	public void checkRndEnd() {
		
		while(hasMatched.size() < players.size()) {
			hasMatched.add(false);
		}
		
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getActive() == false || players.get(i).getAllIn() == true || players.get(i).getRndBet() == highBet) {
				hasMatched.set(i, true);
			}
			else {
				hasMatched.set(i, false);
			}
		}
		
		if(hasMatched.contains(false) == false) {
			curRnd++;
			if(curRnd > 3) {
				nextHand();
			}
			else {
				showRiver(curRnd);
			}
		}
	}
	
	public void nextHand() {
		curRnd = 0;
		
		for(int i = 0; i < players.size(); i++) {
			players.get(i).setActive(true);
			players.get(i).setCurrent(false);
			players.get(i).setStartBal(players.get(i).getBal());
		}
		showRiver(0);
		resetCards();
		deal();
	}
	
	public void showRiver(int rnd) {
		if(rnd == 0) {
			riverImgs.get(0).setVisible(false);
			riverImgs.get(1).setVisible(false);
			riverImgs.get(2).setVisible(false);
			riverImgs.get(3).setVisible(false);
			riverImgs.get(4).setVisible(false);
		}
		else if(rnd == 1) {
			riverImgs.get(0).setVisible(true);
			riverImgs.get(1).setVisible(true);
			riverImgs.get(2).setVisible(true);
		}
		else if(rnd == 2) {
			riverImgs.get(3).setVisible(true);			
		}
		else if(rnd == 3) {
			riverImgs.get(4).setVisible(true);
		}
	}
	
	public void resetCards() {
		riverCards.resetCard();
		
		for(int i = 0; i < players.size(); i++) {
			players.get(i).resetHand();
		}
	}
	
	public List<ClientThread> getClients() {
		return clients;
	}
	
	public void setPort(int num) {
		portNumber = num;
	}
	
	@Override
	public void run() {
		clients = new ArrayList<ClientThread>();
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(serverPort);
			acceptClients(serverSocket);
		} catch (IOException ex) {
			System.err.println("Could not listen on port: " + serverPort);
			System.exit(1);
		}
	}
	
	public void acceptClients(ServerSocket serverSocket) {
		String hostIP = "";
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			hostIP = localHost.getHostAddress();
            ip.setText("Server IP: " + localHost.getHostAddress());
            ipPane.add(ip, 0, 0);
		} catch (UnknownHostException e) {
			hostIP = serverSocket.getLocalSocketAddress() + "";
		}
		System.out.println("Server started on port = " + hostIP);
		
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("A new user has connected");
				System.out.println("From: " + socket.getRemoteSocketAddress());
				checkRndEnd();
				ClientThread client = new ClientThread(this, socket);
				Thread thread = new Thread(client);
				thread.start();
				clients.add(client);
				System.out.println(players.size());
			} catch (IOException ex) {
				System.out.println("User: " + " has failed to connect");
			}
		}
	}

/*	@Override
	public void start(Stage primaryStage) {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	} */
}