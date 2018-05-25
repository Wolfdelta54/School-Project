package application;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Table /* extends Application */
{
	// Multiplayer variables
	public static int portNumber = 4444;
	
	private int serverPort;
	public List<ClientThread> clients;

	private int pot = 0; // initializes pot to 0
	private final ArrayList<Player> players = new ArrayList<Player>(); // list of players and their attributes
	private final ArrayList<River> riverCards = new ArrayList<River>();
	private final DeckOfCards deck = new DeckOfCards();
	private int port = 4444;
	

	public Table()
	{
		this.serverPort = 4444;
		players = new ArrayList<Player>();
	//	deck.shuffle(); // shuffles deck

	}

	public Table(int portNumber) {
		this.serverPort = portNumber;
		players = new ArrayList<Player>();
	//	deck.shuffle();
	}

	public int getPot()
	{		
		return pot; // returns pot
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
		//	river[i].addCard(deck.nextCard()); // adds 5 cards to the river	
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