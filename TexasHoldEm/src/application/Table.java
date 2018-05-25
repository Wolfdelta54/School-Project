package application;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Table /* extends Application */
{
	// Multiplayer variables
	public static int portNumber = 4444;
	
	private int serverPort;
	public List<ClientThread> clients;

	private int pot = 0; // initializes pot to 0
	private ArrayList<Player> players = new ArrayList<Player>(); // list of players and their attributes
	private final River riverCards = new River();
	private final DeckOfCards deck = new DeckOfCards();
	private int port = 4444;
	public Label ip = new Label();
	public GridPane ipPane = new GridPane();
	

	public Table()
	{
		this.serverPort = 4444;
		players = new ArrayList<Player>();
		deck.shuffle(); // shuffles deck
	     
        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            ip.setText(ipAddr.getHostAddress());
            ipPane.add(ip, 0, 0);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
	}

	public Table(int portNumber) {
		this.serverPort = portNumber;
		players = new ArrayList<Player>();
		deck.shuffle();
	}
	
	public GridPane getIpPane() {
		return ipPane;
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

	}
	
	public List<ClientThread> getClients() {
		return clients;
	}
	
	public void setPort(int num) {
		portNumber = num;
	}
	
	public void startServer() {
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
		} catch (UnknownHostException e) {
			hostIP = serverSocket.getLocalSocketAddress() + "";
		}
		System.out.println("Server started on port = " + hostIP);
		
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("User: " + " has connected");
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