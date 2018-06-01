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

public class TableMachine /* extends Application */
{

	private int pot = 0; // initializes pot to 0
	private ArrayList<Player> players = new ArrayList<Player>(); // list of players and their attributes
	ArrayList<Boolean> hasMatched = new ArrayList<Boolean>(); // Used to determine if betting round has ended
	private final River riverCards = new River();
	private final DeckOfCards deck = new DeckOfCards();
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
	

	public TableMachine()
	{
		players = new ArrayList<Player>();
		deck.shuffle(); // shuffles deck
	}

	public TableMachine(int portNumber) {
		players = new ArrayList<Player>();
		deck.shuffle();
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
	
	// the deal method that gives each player 2 cards, and puts 5 cards in the river
	public void deal()
	{
		for(int i = 0; i < 2; i ++)
		{
			for(int j = 0; j < players.size(); j++)
			{
				Card nextCrd = deck.nextCard();
				players.get(i).addCard(nextCrd); // adds a card to the player's deck
			}

		}
		
		for(int i = 0; i < 5; i++)
		{
			Card nextCrd = deck.nextCard();
			riverCards.addCard(nextCrd); // adds 5 cards to the river
		}
		
		riverPane = riverCards.getPane();
		riverImgs = riverCards.getCardNodes();
		deck.shuffle();
	}
	
	// prints a list of all players
	public void prntPlayers() {
		for(int i = 0; i < players.size(); i++) {
			System.out.println(players.get(i));
		}
	}
	
	// resets all cards, resets all player info, and deals the next hand(s) and river
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
	
	// shows the river
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
	
	// resets all cards
	public void resetCards() {
		riverCards.resetCard();
		
		for(int i = 0; i < players.size(); i++) {
			players.get(i).resetHand();
		}
	}
}