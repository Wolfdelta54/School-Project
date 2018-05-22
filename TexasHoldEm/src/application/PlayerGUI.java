package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PlayerGUI {
	// Server info storage
	public String serverHost; // Server IP storage
	public int serverPort; // Server port storage
	public String userName; // Username storage
	
	
	// Game Stage GUI components
	public Group gameStage = new Group();
	public GridPane gameActions = new GridPane();
	public ImageView tableImage = new ImageView();
	public Button fold =  new Button("Fold"), bet =  new Button("Bet"), raise =  new Button("Raise"),
			 call =  new Button("Call"), check =  new Button("Check"); // Buttons for the various player actions
	public Label balance = new Label(); // Visual representation of the player's balance
	public TextField betAmount = new TextField("0"); // Used for betting/raising
	public Button stand = new Button("Stand Up"), leave = new Button("Leave Server"); // Buttons for game exiting
	
	public Player player; // Player object
	public int curBet = 0; // Gets the current bet amount
	
	public Scene scene;
	
	public PlayerGUI() {
		setUser("No Name");
		
		addListeners();
        addImages();
        setUpBtns();
        
        // Set balance label text
        balance.setText("$" + player.getBal());
        
        // Set GameActions position
        gameActions.setTranslateX(75);
        gameActions.setTranslateY(625);
        
        // Add background and game actions to main pane
        gameStage.getChildren().add(tableImage);
        gameStage.getChildren().add(gameActions);
        
        scene = new Scene(gameStage, 1000, 700);
	}
	
	public PlayerGUI(String user, String host, int portNumber) {
		setUser(user);
		serverHost = host;
		serverPort = portNumber;
		
		addListeners();
        addImages();
        setUpBtns();
        
        // Set balance label text
        balance.setText("$" + player.getBal());
        
        // Set GameActions position
        gameActions.setTranslateX(75);
        gameActions.setTranslateY(625);
        
        // Add background and game actions to main pane
        gameStage.getChildren().add(tableImage);
        gameStage.getChildren().add(gameActions);
        
        scene = new Scene(gameStage, 1000, 700);
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Group getPane() {
		return gameStage;
	}
	
	// Adds change listener to the bet textfield and mouse listeners onto the buttons
	public void addListeners() {
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
			player.setActive(false);
			player.setCurrent(false);
			balance.setText("$" + player.getBal());
		});
		
		bet.setOnAction(event -> {
			// Keeps the player from betting 0, null, or nothing
			if(!betAmount.getText().trim().equals("") && betAmount.getText() != null && !betAmount.getText().trim().equals("0")) {
				int bet = Integer.parseInt(betAmount.getText());
				player.updateBal(-bet, betAmount);
				player.setCurrent(false);
				balance.setText("$" + player.getBal());
				System.out.println(player.getBal());
			}
		});
		
		raise.setOnAction(event -> {
			// keeps the player from raising 0, null, or nothing
			if(!betAmount.getText().trim().equals("") && betAmount.getText() != null && !betAmount.getText().trim().equals("0")) {
				int raise = Integer.parseInt(betAmount.getText());
				player.updateBal(-(curBet + raise), betAmount);
				player.setCurrent(false);
				balance.setText("$" + player.getBal());
			}
		});
		
		call.setOnAction(event -> {
			// Matches the currently running bet
			player.updateBal(-curBet, betAmount);
			player.setCurrent(false);
			balance.setText("$" + player.getBal());
		});
		
		check.setOnAction(event -> {
			// Only available if there is no current bet
			player.setCurrent(false);
			balance.setText("$" + player.getBal());
		});
	}
	
	// Sets the image file for the gamestage's table
	public void addImages() {
		FileInputStream timg;
		try {
			timg = new FileInputStream("Images/AdobeStock_15009121Poker.png");
			tableImage.setImage(new Image(timg));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Adds the buttons to the pane
	// If there is no current bet than the Check and Bet buttons are present
	// If there is a current bet than the Call and Raise buttons are present
	public void setUpBtns() {
		gameActions.add(balance, 0, 0);
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
		curBet = num;
	}
	
	// Sets the username and instantiates the player object
	public void setUser(String user) {
		userName = user;
		player = new Player(userName);
	}
}
