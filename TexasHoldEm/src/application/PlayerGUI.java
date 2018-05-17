package application;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class PlayerGUI {
	public String userName = ""; // Username storage for multiplayer reasons
	public int port = 4444;
	
	// Main Menu GUI components
	public Group mainMenu = new Group();
	public GridPane mainMenuOptions = new GridPane();
	public Image menuBG = new Image("Images/darkGreenCloth.png"); // Main menu background
	public Button servers = new Button("Connect to a Server"), host = new Button("Host a game"), quit = new Button("Exit Game"); // Main menu buttons
	public TextField user = new TextField(); // Username input
	public Label userLabel = new Label("User Name:");
	
	// Game Stage GUI components
	public Group gameStage = new Group();
	public GridPane gameActions = new GridPane();
	public Image tableImage = new Image("Images/AdobeStock_15009121Poker.png");
	public Button fold =  new Button("Fold"), bet =  new Button("Bet"), raise =  new Button("Raise"),
			 call =  new Button("Call"), check =  new Button("Check"); // Buttons for the various player actions
	public Label balance = new Label(); // Visual representation of the player's balance
	public TextField betAmount = new TextField(); // Used for betting/raising
	public Button stand = new Button("Stand Up"), leave = new Button("Leave Server"); // Buttons for game exiting
	
	public Player player; // Player object
}
