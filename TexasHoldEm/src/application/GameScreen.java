package application;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class GameScreen {
	
	
	// Game Stage GUI components
	public Group gameStage = new Group(); // The main GUI pane for the curPlayer
	public GridPane gameActions = new GridPane(); // The GUI pane that keeps the action variables together
	public ImageView tableImage = new ImageView(); // Background image
	public ImageView bgImage = new ImageView();
	public Button fold =  new Button("Fold"), bet =  new Button("Bet"), raise =  new Button("Raise"),
			 call =  new Button("Call"), check =  new Button("Check"); // Buttons for the various curPlayer actions
	public Label balance = new Label(), user = new Label(); // Visual representation of the curPlayer's balance
	public TextField betAmount = new TextField("0"); // Used for betting/raising
	public Button stand = new Button("Stand Up"), leave = new Button("Leave Server"); // Buttons for game exiting
	public Pane spring = new Pane(); // Used to create an empty column
	public SwingNode sNode = new SwingNode();
	public JLabel potLbl = new JLabel("$0");
	public int potVal = 0;
	public int rndBet = 0;
	public boolean isAllIn; // used to determine if a player has gone all in
	public int highestBet = -50; // stores the highest bet
	public int pot; // stores the pot's value
	
	HandCheck hndChk = new HandCheck();
	
	public Player curPlayer; // Player object
	public int curPlayerNum;
	public int curBet = 0; // Gets the current bet amount
	ArrayList<Boolean> hasMatched = new ArrayList<Boolean>(); // Used to determine if betting round has ended
	public int curRnd = 0;
	
	public Scene scene;
	
	// River GUI components
	public GridPane riverPane = new GridPane();
	public ArrayList<SwingNode> riverNodes;
	
	public River river = new River();
	public ArrayList<Player> players;
	public Label curPlayerName = new Label();
	
	public GridPane handPane;
	
	public TableMachine table;
	
	public ArrayList<GridPane> hands = new ArrayList<GridPane>();
	
/*	public static void main(String args[]) {
		launch(args);
	} */
	
	public GameScreen() {
		addListeners();
        addImages();
        setUpBtns();
        
        // Set GameActions position
        gameActions.setTranslateX(75);
        gameActions.setTranslateY(625);
        
        // Add potLbl to the GUI
        	potLbl.setMinimumSize(new Dimension(50, 25));
    		sNode.setTranslateX(450);
    		sNode.setTranslateY(300);
    		sNode.setContent(potLbl);
			
			curPlayerName.setTranslateX(50);
			curPlayerName.setTranslateY(50);
        
        // Add background and game actions to main pane
        gameStage.getChildren().add(bgImage);
        gameStage.getChildren().add(tableImage);
        gameStage.getChildren().add(gameActions);
		gameStage.getChildren().add(sNode);
		gameStage.getChildren().add(curPlayerName);
        
        scene = new Scene(gameStage, 1000, 700);
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Group getPane() {
		return gameStage;
	}
	
	// Sets up the table when first called
	public void setTable(TableMachine table) {
		this.table = table;
		setPlayers(this.table.getPlayers());
		this.table.deal();
        
        riverPane = table.getRiver().getPane();
		
		riverPane.setTranslateX(312);
		riverPane.setTranslateY(180);
		
		gameStage.getChildren().add(riverPane);
		
		blinds();
		showRiver(curRnd);
		
	//	updateRiver();
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	// Used to start the game and to move from player to player
	public void setCurPlayer(int ind) {
		this.curPlayer = players.get(ind);
		this.curPlayer.setCurrent(true);
		curPlayerNum = ind;
		
		handPane = curPlayer.getHandPane();
        
        handPane.setTranslateX(175);
        handPane.setTranslateY(480);
        
        if(gameStage.getChildren().contains(handPane))
        	gameStage.getChildren().remove(handPane);
        
        gameStage.getChildren().add(handPane);
        // Set balance label text
        balance.setText("$" + curPlayer.getBal());
        user.setText(curPlayer.getName());
	//	updateVars();
	}
	
	// Used to determine who the next active (has not folded or gone all in) player is
	public int getNextActive(int curInd) {
		int ind = -1;
		
		while(ind < 0) {
			curInd++;
			
			if(curInd >= players.size()) {
				curInd = 0;
			}
			if(players.get(curInd).getActive() == true && players.get(curInd).getAllIn() == false) {
				ind = curInd;
			}
		}
		
		return ind;
	}
	
	// updates the pot label
	public void updatePot() {
		String potTxt = potLbl.getText();
		potVal = Integer.parseInt(potTxt.substring(potTxt.indexOf("$") + 1));
		if(potVal != pot) {
			potVal = pot;
			potLbl.setText("$" + potVal);
		}
	}
	
	// Adds change listener to the bet textfield and mouse listeners onto the buttons
	public void addListeners() {
		Label errMsg = new Label("Invalid bet amount");
		errMsg.setTranslateX(450);
		errMsg.setTranslateY(500);
		errMsg.setFont(new Font("Courier", 24));
		
		betAmount.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
				// Keeps the user from inputting anythings other than numerical values into the bet textfield
		        if (!newVal.matches("\\d*")) {
		            betAmount.setText(newVal.replaceAll("[^\\d]", ""));
		        }
			}
		});
		
		fold.setOnAction(event -> {
			// Sets the players Active state (true = still in the round, false = not playing in the round)
			if(curPlayer.getCurrent() == true) {
				curPlayer.setActive(false);
				curPlayer.setCurrent(false);
				balance.setText("$" + curPlayer.getBal());
				setCurPlayer(getNextActive(curPlayerNum));
				checkRndEnd();
			}
		});
		
		bet.setOnAction(event -> {
			// The next dozen comments can be applied to several things within this segment, however, to avoid typing them so much
			// I will put them at their first instance
			// Keeps the curPlayer from betting 0, null, nothing, or a negative amount
			if(curPlayer.getCurrent() == true) {
				if(!betAmount.getText().trim().equals("") && betAmount.getText() != null && !betAmount.getText().trim().equals("0")) {
					int bet = Integer.parseInt(betAmount.getText()); // gets the bet
					if(bet < curPlayer.getBal() && bet > 0) { // determines bet validity
						if(gameStage.getChildren().contains(errMsg)) { // removes the error message if it was present
							gameStage.getChildren().remove(errMsg);
						}
						curPlayer.updateBal(-bet, betAmount); // update player info balance
						curPlayer.setCurrent(false); // get ready for the next player
						balance.setText("$" + curPlayer.getBal()); // update the label
						if(bet > curBet)
						curBet = bet;
						pot += bet; // add to the pot
						
						if(bet > highestBet) {
							highestBet = bet; // determine the highest bet
						}
						updateBtns(); // update buttons
						updatePot(); // update pot
						System.out.println(curPlayer + " PlayerGUI"); // Testing line
						rndBet = curPlayer.getRndBet(); // update round bet
						checkRndEnd(); // determine if the betting round has ended
						setCurPlayer(getNextActive(curPlayerNum)); // move onto the next player
					}
					else if(bet == curPlayer.getBal() && bet > 0) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						curPlayer.updateBal(-bet, betAmount);
						curPlayer.setCurrent(false);
						isAllIn = curPlayer.getAllIn();
						balance.setText("$" + curPlayer.getBal());
						if(bet > curBet)
						curBet = bet;
						pot += bet;
						
						if(bet > highestBet) {
							highestBet = bet;
						}
						updateBtns();
						updatePot();
						System.out.println(curPlayer + " PlayerGUI");
						rndBet = curPlayer.getRndBet();
						checkRndEnd();
						setCurPlayer(getNextActive(curPlayerNum));
					}
					else {
						gameStage.getChildren().add(errMsg);
					}
				}
			}
		});
		
		raise.setOnAction(event -> {
			// keeps the curPlayer from raising 0, null, or nothing
			if(curPlayer.getCurrent() == true) {
				if(!betAmount.getText().trim().equals("") && betAmount.getText() != null && !betAmount.getText().trim().equals("0")) {
					int raise = Integer.parseInt(betAmount.getText());
					int bet = curBet + raise;
					if(bet < curPlayer.getBal() && bet > 0) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						curPlayer.updateBal(-bet, betAmount);
						curPlayer.setCurrent(false);
						balance.setText("$" + curPlayer.getBal());
						if(bet > curBet)
						curBet = bet;
						pot += bet;
						
						if(bet > highestBet) {
							highestBet = bet;
						}
						updateBtns();
						updatePot();
						rndBet = curPlayer.getRndBet();
						checkRndEnd();
						setCurPlayer(getNextActive(curPlayerNum));
					}
					else if(bet == curPlayer.getBal() && bet > 0) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						curPlayer.updateBal(-bet, betAmount);
						curPlayer.setCurrent(false);
						isAllIn = curPlayer.getAllIn();
						balance.setText("$" + curPlayer.getBal());
						if(bet > curBet)
						curBet = bet;
						pot += bet;
						
						if(bet > highestBet) {
							highestBet = bet;
						}
						updateBtns();
						updatePot();
						System.out.println(curPlayer + " PlayerGUI");
						rndBet = curPlayer.getRndBet();
						checkRndEnd();
						setCurPlayer(getNextActive(curPlayerNum));
					}
					else {
						gameStage.getChildren().add(errMsg);
					}
				}
			}
		});
		
		call.setOnAction(event -> {
			// Matches the currently running bet
			if(curPlayer.getCurrent() == true) {
				if(curBet < curPlayer.getBal() && curBet > 0) {
					rndBet = curPlayer.getRndBet();
					int bet = curBet - rndBet;
					curPlayer.updateBal(-bet, betAmount);
					if(bet > curBet)
					curBet = bet;
					pot += bet;
					
					if(bet > highestBet) {
						highestBet = bet;
					}
					curPlayer.setCurrent(false);
					balance.setText("$" + curPlayer.getBal());
					updateBtns();
					updatePot();
					checkRndEnd();
					setCurPlayer(getNextActive(curPlayerNum));
				}
				else if(curBet == curPlayer.getBal() && curBet > 0) {
					if(gameStage.getChildren().contains(errMsg)) {
						gameStage.getChildren().remove(errMsg);
					}
					rndBet = curPlayer.getRndBet();
					int bet = curBet - rndBet;
					curPlayer.updateBal(-bet, betAmount);
					if(bet > curBet)
					curBet = bet;
					pot += bet;
					
					if(bet > highestBet) {
						highestBet = bet;
					}
					curPlayer.setCurrent(false);
					isAllIn = curPlayer.getAllIn();
					balance.setText("$" + curPlayer.getBal());
					updateBtns();
					updatePot();
					System.out.println(curPlayer + " PlayerGUI");
					checkRndEnd();
					setCurPlayer(getNextActive(curPlayerNum));
				}
				else if(curBet > curPlayer.getBal() && curBet > 0) {
					if(gameStage.getChildren().contains(errMsg)) {
						gameStage.getChildren().remove(errMsg);
					}
					rndBet = curPlayer.getRndBet();
					int bet = curPlayer.getBal();
					curPlayer.updateBal(-bet, betAmount);
					if(bet > curBet)
						curBet = bet;
					
					pot += bet;
					
					if(bet > highestBet) {
						highestBet = bet;
					}
					curPlayer.setCurrent(false);
					isAllIn = curPlayer.getAllIn();
					balance.setText("$" + curPlayer.getBal());
					updateBtns();
					updatePot();
					System.out.println(curPlayer + " PlayerGUI");
					checkRndEnd();
					setCurPlayer(getNextActive(curPlayerNum));
				}
				else {
					gameStage.getChildren().add(errMsg); // An error message that pops up when a player attempts to place a bet that is too high for them
				}
			}
		});
		
		check.setOnAction(event -> {
			// Only available if there is no current bet
			if(curPlayer.getCurrent() == true) {
				curPlayer.setCurrent(false); // update current state
				balance.setText("$" + curPlayer.getBal()); // update balance
				updateBtns(); // update buttons
				checkRndEnd(); // calls checkRndEnd to see if the betting round has ended
				setCurPlayer(getNextActive(curPlayerNum)); // sets up for the next player
			}
		});
	}
	
	// Sets the image file for the gamestage's table
	public void addImages() {
		FileInputStream timg;
		FileInputStream bgimg;
		try {
			timg = new FileInputStream("Images/AdobeStock_15009121Poker.png");
			bgimg = new FileInputStream("Images/woodTexture.jpg");
			tableImage.setImage(new javafx.scene.image.Image(timg));
			bgImage.setImage(new javafx.scene.image.Image(bgimg));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void prntPlayer() {
		System.out.println(curPlayer); // Prints a list of all player objects
	}
	
	// Adds the buttons to the pane
	// If there is no current bet than the Check and Bet buttons are present
	// If there is a current bet than the Call and Raise buttons are present
	public void setUpBtns() {
		balance.setFont(new Font(12));
		balance.setTextFill(Paint.valueOf("WHITE"));
		

		user.setFont(new Font(12));
		user.setTextFill(Paint.valueOf("WHITE"));
		
		// Add the normal buttons to the GUI
		gameActions.add(user, 0, 0);
		gameActions.add(balance, 1, 0);
		gameActions.add(fold, 0, 1);
		gameActions.add(betAmount, 2, 0);
		
		// Removes the Call and Raise buttons (if present) and adds the Check and Bet buttons
		if(curBet == 0) {
			if(gameActions.getChildren().contains(call)) {
				gameActions.getChildren().remove(call);
			}
			if(gameActions.getChildren().contains(raise)) {
				gameActions.getChildren().remove(raise);
			}
			
			gameActions.add(check, 1, 1);
			gameActions.add(bet, 2, 1);
		}
		
		// Removes the Check and Bet buttons (if present) and adds the Call and Raise buttons
		else if(curBet > 0) {
			if(gameActions.getChildren().contains(check)) {
				gameActions.getChildren().remove(check);
			}
			if(gameActions.getChildren().contains(bet)) {
				gameActions.getChildren().remove(bet);
			}
			
			gameActions.add(call, 1, 1);
			gameActions.add(raise, 2, 1);
		}
	}
	
	// Sets the currently running bet
	public void setCurBet(int num) {
		curBet = num; // returns the instance of the curBet
	}
	
	public Player getPlayer() {
		return curPlayer; // Returns the instance of the curPlayer
	}
	
	public void addToRiver(String cardStr) {
		river.addCard(cardStr); // Self explainatory
		if(river.getCards().size() == 5) {
			river.updateImgs(); // Updates the images when the river has obtained it's fifth card
			riverNodes = river.getCardNodes(); // Second part of river GUI update
		}
		System.out.println("Added to river"); // Testing line
	}
	
	public void addToHand(String card) {
		curPlayer.addCard(card); // Self explainatory
		System.out.println("Added to hand"); // Testing line
	}
	
	public void showRiver(int rnd) {
		System.out.println("showRiver called"); // Testing line
		if(rnd == 0) {
			table.getRiver().showRiver(0); // All cards are hidden
		}
		else if(rnd == 1) {
			table.getRiver().showRiver(1); // First 3 cards are shown
		}
		else if(rnd == 2) {
			table.getRiver().showRiver(2); // First 4 cards are shown
		}
		else if(rnd == 3) {
			table.getRiver().showRiver(3); // All five cards are shown
		}
	}
	
	public void blinds() {
		curPlayer = players.get(players.size() - 2); // sets the second to last player in the array as the Big Blind
		curPlayer.setCurrent(true); // sets current state to true to allow editting of their info
		curPlayer.updateBal(-50, betAmount); // forced bet of 50 to enable betting for the next round
		balance.setText("$" + curPlayer.getBal()); // updates the balance label
		if(50 > curBet)
		curBet = 50;
		pot += 50; // adds 50 to the pot
		
		if(50 > highestBet) {
			highestBet = 50; // sets the highest bet to 50
		}
		updateBtns(); // update buttons
		updatePot(); // update pot
		System.out.println(curPlayer + " PlayerGUI"); // Testing line
		rndBet = curPlayer.getRndBet(); // updates the round bet
		curPlayer.setCurrent(false); // Tells the program that it is the next players turn
		
		curPlayer = players.get(players.size() - 1); // Changes to the next player who will be the small blind
		curPlayer.setCurrent(true); // sets their current state to true to allow editting of their info
		curPlayer.updateBal(-25, betAmount); // forced bet of 25 to enable betting for the next round
		balance.setText("$" + curPlayer.getBal()); // updates their balance
		if(25 > curBet)
		curBet = 25;
		pot += 25;
		
		if(25 > highestBet) {
			highestBet = 25;
		}
		updateBtns(); // updates the buttons
		updatePot(); // updates the pot
		System.out.println(curPlayer + " PlayerGUI"); // Testing line
		rndBet = curPlayer.getRndBet(); // updates round bet variable
		curPlayer.setCurrent(false); // sets their current state to false to tell the program that it is the next players turn
	}
	
	public void checkRndEnd() {
		System.out.println("checkRndEnd called"); // Testing line
		while(hasMatched.size() < players.size()) {
			hasMatched.add(false); // Adds a boolean variable of false to the hasMatched list for every player
		}
		
		for(int i = 0; i < players.size(); i++) {
			// if the player has folded, has gone all in, or has bet the current rounds highest bet, their hasMatched state is set to true
			if(players.get(i).getActive() == false || players.get(i).getAllIn() == true || players.get(i).getRndBet() == highestBet) {
				hasMatched.set(i, true);
			}
			// if not it is set back to false
			else {
				hasMatched.set(i, false);
			}
			System.out.println(hasMatched.get(i)); // Testing line
		}
		
		if(!hasMatched.contains(false)) {
			curRnd++; // Used to determine which betting round the game is in
			System.out.println("checkRndEnd in final if statement"); // Testing line
			if(curRnd > 3) {
				hndChk.setCards(table.getRiver()); // Sets the river cards in the HandCheck class
				hndChk.setPlayers(table.getPlayers()); // Sets the player list in HandCheck
				determineWinner(hndChk.checkHands());
				resetAll(); // Calls resetAll
				table.nextHand(); // Tells table to deal another hand
				highestBet = -50; // Resets the highest bet
				curBet = 0; // Resets the current bet
				updateBtns(); // Resets buttons
				
				for(int i = 0; i < players.size(); i++) {
					players.get(i).rndBet = 0; // Resets the player's round bet
				}
				blinds(); // Calls blinds()
				curRnd = 0;
			}
			else {
				System.out.println("checkRndEnd updating UI");
				showRiver(curRnd); // Updates the River
				highestBet = -50; // Resets the highest bet for the new round of betting
				curBet = 0; // Resets the current bet for the new round of betting
				updateBtns(); // Resets the buttons
				
				for(int i = 0; i < players.size(); i++) {
					players.get(i).rndBet = 0; // resets the player's round bet
				}
			}
		}
	}
	
	// determine the winner
	public void determineWinner(ArrayList<String> results) {
		ArrayList<String> topPlayers = new ArrayList<String>(); // back up storage incase of tie
		String topPlayer = ""; // used to store the name of the person who has the current top hand
		String topHand = "none"; // used to store the current top hand
		int topPow = -1; // used to store the power of the top hand
		
		ArrayList<String> playerNames = new ArrayList<String>(); // used to store all player names and keep them lined up with their respective hand
		ArrayList<String> playerHands = new ArrayList<String>(); // used to store all player hands and keep them lined up with their respective name
		
		for(int i = 0; i < results.size(); i++) {
			String temp = results.get(i); // holds the information that is going to be stored
			playerNames.add(temp.substring(0, temp.indexOf(";"))); // store the name into the array of names
			playerHands.add(temp.substring(temp.indexOf(";") + 1)); // store the hand into the array of hands
		}
		
		for(int i = 0; i < playerNames.size(); i++) {
			String curPlay = playerNames.get(i); // hold the name of the player currently being evaluated
			String curHand = playerHands.get(i); // hold the hand of the player currently being evaluated
			int indOfPow = curHand.indexOf("p-") + 2; // index of power number
			int pow = Integer.parseInt(curHand.substring(indOfPow)); // turn the power number (String) into an int
			
			if(pow > topPow) { // determine if the player has a more powerful hand
				// store info if so
				topPow = pow;
				topHand = curHand.substring(0, curHand.indexOf(" p-"));
				topPlayer = curPlay;
			}
		}
		
		// find the player with the strongest hand and give them the prize
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getName().equals(topPlayer)) {
				players.get(i).updateBal(pot);
				pot = 0;
				updatePot();
			}
		}
	}
	
	public void resetAll() {
		river.resetCard();
		for(int i = 0; i < players.size(); i++) {
			players.get(i).resetHand(); // Reset each players hand
			players.get(i).setActive(true); // Reset each players' active stay
			players.get(i).setStartBal(players.get(i).getBal()); // Set each players' balance to that at the end of the previous hand
		}
	}
	
	public void updateBtns() {
		// Removes the Call and Raise buttons (if present) and adds the Check and Bet buttons
		if(curBet == 0) {
			if(gameActions.getChildren().contains(call)) {
				gameActions.getChildren().remove(call);
			}
			if(gameActions.getChildren().contains(raise)) {
				gameActions.getChildren().remove(raise);
			}
			
			if(!gameActions.getChildren().contains(check)) {
				gameActions.add(check, 1, 1);
			}
			if(!gameActions.getChildren().contains(bet)) {
				gameActions.add(bet, 2, 1);
			}
		}
		
		// Removes the Check and Bet buttons (if present) and adds the Call and Raise buttons
		else if(curBet > 0) {
			if(gameActions.getChildren().contains(check)) {
				gameActions.getChildren().remove(check);
			}
			if(gameActions.getChildren().contains(bet)) {
				gameActions.getChildren().remove(bet);
			}
			
			if(!gameActions.getChildren().contains(call)) {
				gameActions.add(call, 1, 1);
			}
			if(!gameActions.getChildren().contains(raise)) {
				gameActions.add(raise, 2, 1);
			}
		}
	}
}
