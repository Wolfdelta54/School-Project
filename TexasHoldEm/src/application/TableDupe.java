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

public class TableDupe 
{
	// Multiplayer variables
	

	private int pot = 0; // initializes pot to 0
	private ArrayList<Player> players = new ArrayList<Player>(); // list of players and their attributes
	private final River riverCards = new River("tester");
	private final DeckOfCards deck = new DeckOfCards();
	
	// Betting rounds are as follows: 0 - first bet, right after deal; 1 - first 3 cards of river have been shown;
	//		2 - first 4 cards of river have been shown; 3 - first 5 cards of river have been shown
	// After round 3 of betting the hands of all currently active players are shown and they go though the hand checker
	// Player with the best hand gets the pot then the next hand is dealt and betting commences again
	public int curRnd = 0;
	public final int maxRnds = 3;
	public int highBet = -50; // Stores the highest bet, used to see if all players have bet this amount or have gone all in
	
	// Pane of components that will constantly change
	
	
	public String change = "none";
	public boolean hasChange = false;
	public ArrayList<String> changeStorage = new ArrayList<String>();
	
	public boolean srvrLive = false;
	

	public TableDupe()
	{
		
		players = new ArrayList<Player>();
		deck.shuffle(); // shuffles deck
	}

	public TableDupe(int portNumber) {
		
		players = new ArrayList<Player>();
		deck.shuffle();
	}
	
	public boolean isLive() {
		return srvrLive;
	}
	
	public void setLive(boolean x) {
		srvrLive = x;
		System.out.println("Table > setLive");
	}
	
	public int getNumPlayers() {
		return players.size();
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
				Card nextCrd = deck.nextCard();
				players.get(j).addCard(nextCrd); // adds a card to the player's deck
			}

		}

		
		for(int i = 0; i < 5; i++)
		{
		//	for(int j = 0; j < players.size(); j++) {
				Card nextCrd = deck.nextCard();
				riverCards.addCard(nextCrd);
		//	}
		}
		
		deck.shuffle();
	}
	
	public void prntPlayers() {
		for(int i = 0; i < players.size(); i++) {
			System.out.println(players.get(i));
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