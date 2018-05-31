package application;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class PlayerGUI implements Runnable {
	// Server info storage
	public String serverHost; // Server IP storage
	public int serverPort; // Server port storage
	public String userName; // Username storage
	public String actions = "none"; // Stores the action that the player has done
	
	
	// Game Stage GUI components
	public Group gameStage = new Group(); // The main GUI pane for the player
	public GridPane gameActions = new GridPane(); // The GUI pane that keeps the action variables together
	public ImageView tableImage = new ImageView(); // Background image
	public ImageView bgImage = new ImageView();
	public Button fold =  new Button("Fold"), bet =  new Button("Bet"), raise =  new Button("Raise"),
			 call =  new Button("Call"), check =  new Button("Check"); // Buttons for the various player actions
	public Label balance = new Label(); // Visual representation of the player's balance
	public TextField betAmount = new TextField("0"); // Used for betting/raising
	public Button stand = new Button("Stand Up"), leave = new Button("Leave Server"); // Buttons for game exiting
	public Pane spring = new Pane(); // Used to create an empty column
	public SwingNode sNode = new SwingNode();
	public JLabel potLbl = new JLabel("$0");
	public int potVal = 0;
	public int rndBet = 0;
	public boolean isAllIn;
	
	public boolean wasSent = false;
	
	public Player player; // Player object
	public int curBet = 0; // Gets the current bet amount
	
	public Scene scene;
	public boolean srvrLive = false;
	public IntegerProperty srvrLiveProperty = new SimpleIntegerProperty(0);
	
/*	public static void main(String args[]) {
		launch(args);
	} */
	
	public PlayerGUI() {
		setUser("No Name");
		
		addListeners();
        addImages();
        setUpBtns();
        potAutoUpdate();
        
        srvrLiveProperty.set(0);
        
        // Set GameActions position
        gameActions.setTranslateX(75);
        gameActions.setTranslateY(625);
        
        // Add potLbl to the GUI
    		sNode.setTranslateX(450);
    		sNode.setTranslateY(300);
    		sNode.setContent(potLbl);
			updateVars();
        
        // Add background and game actions to main pane
        gameStage.getChildren().add(tableImage);
        gameStage.getChildren().add(gameActions);
		gameStage.getChildren().add(sNode);
        
        scene = new Scene(gameStage, 1000, 700);
	} 
	
	public PlayerGUI(String user, String host, int portNumber) {
		setUser(user);
		player.setCurrent(true);
		serverHost = host;
		serverPort = portNumber;
		
		addListeners();
        addImages();
        setUpBtns();
        
        // Set balance label text
        balance.setText("$" + player.getBal());
        
        // Set GameActions position
        gameActions.setTranslateX(75);
        gameActions.setTranslateY(600);
        
        // Add potLbl to the GUI
        potLbl.setMinimumSize(new Dimension(75, 25));
		sNode.setTranslateX(450);
		sNode.setTranslateY(300);
		sNode.setContent(potLbl);
		updateVars();
        
        // Set the size of the gaps between each row and column
        gameActions.setHgap(12);
        gameActions.setVgap(12);
        
        // Add background and game actions to main pane
        gameStage.getChildren().add(bgImage);
        gameStage.getChildren().add(tableImage);
        gameStage.getChildren().add(gameActions);
		gameStage.getChildren().add(sNode);
        
        scene = new Scene(gameStage, 1000, 700);
        scene.getStylesheets().add("application/PlayerGUI.css");
        
        // Used for testing the GUI layout and some of the logic that is activated when a button is clicked
//        primaryStage.setScene(scene);
//        primaryStage.show();
	}
	
	public void sendJoin() {
		actions = userName + ";joined";
		System.out.println(actions + "\n");
	}
	
	public String getName() {
		return player.getName();
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Group getPane() {
		return gameStage;
	}
	
	public void potAutoUpdate() {
		String potTxt = potLbl.getText();
		String temp = "$" + potVal;
		
		if(!potTxt.equals(temp)) {
			potLbl.setText(temp);
		}
	}
	
	public boolean isLive() {
		return srvrLive;
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
			if(player.getCurrent() == true) {
				player.setActive(false);
				player.setCurrent(false);
				balance.setText("$" + player.getBal());
				actions = userName + ";fold";
			}
		});
		
		bet.setOnAction(event -> {
			// Keeps the player from betting 0, null, or nothing
			if(player.getCurrent() == true) {
				if(!betAmount.getText().trim().equals("") && betAmount.getText() != null && !betAmount.getText().trim().equals("0")) {
					int bet = Integer.parseInt(betAmount.getText());
					if(bet < player.getBal()) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						player.updateBal(-bet, betAmount);
						player.setCurrent(false);
						balance.setText("$" + player.getBal());
						actions = userName + ";bet;" + bet;
						curBet = bet;
						updateBtns();
						System.out.println(player + " PlayerGUI");
						rndBet = player.getRndBet();
					}
					else if(bet == player.getBal()) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						player.updateBal(-bet, betAmount);
						player.setCurrent(false);
						isAllIn = player.getAllIn();
						balance.setText("$" + player.getBal());
						actions = userName + ";bet;" + bet;
						curBet = bet;
						updateBtns();
						System.out.println(player + " PlayerGUI");
						rndBet = player.getRndBet();
					}
					else {
						gameStage.getChildren().add(errMsg);
					}
				}
			}
		});
		
		raise.setOnAction(event -> {
			// keeps the player from raising 0, null, or nothing
			if(player.getCurrent() == true) {
				if(!betAmount.getText().trim().equals("") && betAmount.getText() != null && !betAmount.getText().trim().equals("0")) {
					int raise = Integer.parseInt(betAmount.getText());
					int bet = curBet + raise;
					if(bet < player.getBal()) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						player.updateBal(-bet, betAmount);
						player.setCurrent(false);
						balance.setText("$" + player.getBal());
						actions = userName + ";raise;" + bet;
						curBet = bet;
						updateBtns();
						rndBet = player.getRndBet();
					}
					else if(bet == player.getBal()) {
						if(gameStage.getChildren().contains(errMsg)) {
							gameStage.getChildren().remove(errMsg);
						}
						player.updateBal(-bet, betAmount);
						player.setCurrent(false);
						isAllIn = player.getAllIn();
						balance.setText("$" + player.getBal());
						actions = userName + ";bet;" + bet;
						curBet = bet;
						updateBtns();
						System.out.println(player + " PlayerGUI");
						rndBet = player.getRndBet();
					}
					else {
						gameStage.getChildren().add(errMsg);
					}
				}
			}
		});
		
		call.setOnAction(event -> {
			// Matches the currently running bet
			if(player.getCurrent() == true) {
				if(curBet < player.getBal()) {
					player.updateBal(-curBet, betAmount);
					player.setCurrent(false);
					balance.setText("$" + player.getBal());
					actions = userName + ";call;" + curBet;
					updateBtns();
					rndBet = player.getRndBet();
				}
				else if(curBet == player.getBal()) {
					if(gameStage.getChildren().contains(errMsg)) {
						gameStage.getChildren().remove(errMsg);
					}
					int bet = curBet - rndBet;
					player.updateBal(-bet, betAmount);
					player.setCurrent(false);
					isAllIn = player.getAllIn();
					balance.setText("$" + player.getBal());
					actions = userName + ";bet;" + bet;
					updateBtns();
					System.out.println(player + " PlayerGUI");
					rndBet = player.getRndBet();
				}
				else {
					gameStage.getChildren().add(errMsg);
				}
			}
		});
		
		check.setOnAction(event -> {
			// Only available if there is no current bet
			if(player.getCurrent() == true) {
				player.setCurrent(false);
				balance.setText("$" + player.getBal());
				actions = userName + ";check";
				updateBtns();
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
			tableImage.setImage(new Image(timg));
			bgImage.setImage(new Image(bgimg));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void prntPlayer() {
		System.out.println(player);
	}
	
	// Adds the buttons to the pane
	// If there is no current bet than the Check and Bet buttons are present
	// If there is a current bet than the Call and Raise buttons are present
	public void setUpBtns() {
		spring.setMinWidth(50);
		
		gameActions.add(balance, 0, 0);
		gameActions.add(fold, 0, 1);
		gameActions.add(betAmount, 2, 0);
		gameActions.add(spring, 3, 1);
		gameActions.add(stand, 4, 0);
		gameActions.add(leave, 4, 1);
        
        // Set balance label text
        balance.setText("$" + player.getBal());
		
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
	
	public void addHand() {
        GridPane hand = player.getHandPane();
        hand.setTranslateX(175);
        hand.setTranslateY(480);
        gameStage.getChildren().add(hand);
        GridPane dummy = player.getDummyHand();
        dummy.setTranslateX(850);
        dummy.setTranslateY(225);
        gameStage.getChildren().add(dummy);
	}
	
	// Sets the currently running bet
	public void setCurBet(int num) {
		curBet = num;
	}
	
	// Sets the username and instantiates the player object
	public void setUser(String user) {
		userName = user;
		System.out.println(user);
		player = new Player(userName);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void updateVars() {
		String potTxt = potLbl.getText();
		potVal = Integer.parseInt(potTxt.substring(potTxt.indexOf("$") + 1));
		if(potVal != player.getCurPot()) {
			potVal = player.getCurPot();
			potLbl.setText("$" + potVal);
		}
		
		if(curBet != player.getCurBet()) {
			curBet = player.getCurBet();
		}
		
		int bal = Integer.parseInt(balance.getText().substring(1));
		if(bal != player.getBal()) {
			bal = player.getBal();
			balance.setText("$" + bal);
		}
		
		Thread srvrChange = new Thread(new Runnable() {
			public void run() {
				int stop = 0;
				while(stop == 0) {
					if(player.isLive() == true) {
						srvrLiveProperty.set(1);
						srvrLive = true;
						System.out.println("srvrLive has changed, PlayerGUI");
						stop = 1;
					}
					else {
						srvrLiveProperty.set(0);
						srvrLive = false;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		srvrChange.start();
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
	
	@Override
	public void run() {
		try {
			Socket socket = new Socket(serverHost, serverPort); // Creates a socket
			Thread.sleep(1000); // Wait for network communication
			
			ServerThread serverThread = new ServerThread(socket, userName); // Creates the serverThread
			Thread serverAccessThread = new Thread(serverThread); // Creates a new thread out of the serverThread
			serverAccessThread.start(); // Starts the thread which allows multiplayer connectivity
			srvrLive = player.isLive();
			
			if(wasSent == false) {
				serverThread.addPlayer(player);
				wasSent = true;
			}
			// as long as the serverAccessThread is running (alive) the program will go through the loop
			while(serverAccessThread.isAlive()) {
				// if actions is not empty than it will send the info
				if(!actions.equalsIgnoreCase("none")) {
					// sends the info
					serverThread.addNextAction(actions);
					// then empties the string to avoid overflow/overload
					actions = "none";
				}
			}
		} catch (IOException ex) {
			// Catches an error that is caused by an unavailable host
			System.err.println("Could not connect to local server");
		} catch (InterruptedException ex) {
			// Catches an error that is caused by an unavailable host
			System.out.println("Connection Interrupred");
		}
	}
}
