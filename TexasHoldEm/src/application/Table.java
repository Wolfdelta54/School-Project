package application;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Table implements Runnable /* extends Application */
{
	// Multiplayer variables
	public static int portNumber = 4444;
	
	private int serverPort;
	public List<ClientThread> clients;

	private int pot = 0; // initializes pot to 0
	private ArrayList<Player> players = new ArrayList<Player>(); // list of players and their attributes
	private ArrayList<PlayerGUI> playersGUI = new ArrayList<PlayerGUI>();
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
	public ArrayList<ImageView> riverImgs = new ArrayList<ImageView>(); // Stores the images for the river
	public ArrayList<GridPane> handList = new ArrayList<GridPane>(); // Stores the GridPanes used to hold the images of every player's cards
	public ArrayList<GridPane> playerList = new ArrayList<GridPane>(); // Stores the GridPanes used to hold all of the info for each player
	

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
	
	public GridPane getIpPane() {
		return ipPane;
	}
	
	public Group getPane() {
		return tablePane;
	}
	
	public GridPane getRiverPane() {
		return riverPane;
	}
	
	public void setRiverPane() {
		riverPane = riverCards.getPane();
	}
	
	public void addPlayer(Player player) {
		players.add(player);
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
		
		// Go through the list of players looking for the one who's username matches that of the executor
		for(int i = 0; i < players.size(); i++) {
			if(user.equals(players.get(i).getName())) {
				// Set active to false if the action was Fold
				if(action.equalsIgnoreCase("Fold")) {
					players.get(i).setActive(false);
				}
				// Update balance of the executor and update the current bet
				else if(action.equalsIgnoreCase("Bet") || action.equalsIgnoreCase("Raise") || action.equalsIgnoreCase("Call")) {
					players.get(i).updateBal(0 - amount);
					pot = pot + amount;
					
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
				ClientThread client = new ClientThread(this, socket);
				Thread thread = new Thread(client);
				thread.start();
				clients.add(client);
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