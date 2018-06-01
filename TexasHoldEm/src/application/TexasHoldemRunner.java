package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TexasHoldemRunner  extends Application {
	// Main Menu GUI components
	public Group mainMenu = new Group();
	public GridPane mainMenuOptions = new GridPane();
	public ImageView menuBG = new ImageView(); // Main menu background
	public Button start = new Button("Start Game"), next = new Button("Next"), quit = new Button("Exit Game"); // Main menu buttons
	public TextField user = new TextField(); // Username input
	public Label userLabel = new Label("User Name:");
	public Label title = new Label("Texas Hold'Em");
	public Scene scene;
	public Scene gameScene;
	public TextField numPlayers = new TextField();
	public Label numPlyrLbl = new Label("Number of players: ");
	public ArrayList<TextField> users;
	public ArrayList<Label> userLbls;
	public ArrayList<Player> players;
	
	TableMachine table = new TableMachine();
	GameScreen gmScreen = new GameScreen();
	
	@Override
	public void start(Stage primaryStage) {
		// New pane for Username components
		GridPane setupPane = new GridPane();
		setupPane.add(numPlayers, 1, 0);
		setupPane.add(numPlyrLbl, 0, 0);
        
		// Button listener for Quit button
        quit.setOnAction(event->
                primaryStage.close());
        
        // Adding IDs to the components
        title.setId("title");
        
        // Adding GUI components to the pane
        mainMenuOptions.add(title, 0, 0);
        mainMenuOptions.add(setupPane, 0, 1);
        mainMenuOptions.add(next, 0, 3);
        mainMenuOptions.add(quit, 0, 5);
        
        addListeners(primaryStage);
        addImages();
        
        // Set position of MenuOptions
        mainMenuOptions.setTranslateX(50);
        mainMenuOptions.setTranslateY(50);
        
        // Add the Background image and MenuOptions to the main pane
        mainMenu.getChildren().add(menuBG);
        mainMenu.getChildren().add(mainMenuOptions);
        
        scene = new Scene(mainMenu, 300, 250);
        
        // Add style sheet to Scene
        scene.getStylesheets().add("application/Menu.css");
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // set on close request, shuts down all running threads when the program is closed
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	@Override
        	public void handle(WindowEvent e) {
        		Platform.exit();
        		System.exit(0);
        	}
        });
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Adds change listener to the UserName textfield
	public void addListeners(Stage primaryStage) {
		numPlayers.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
		        if (!newVal.matches("\\d*")) {
		        	if(Integer.parseInt(newVal) > 8)
		        		numPlayers.setText("8");
		        	else if(Integer.parseInt(newVal) < 1)
			        		numPlayers.setText("1");
		        }
			}
		});
		
		// moves onto the screen for inputting player names
		next.setOnAction(event -> {
			int playerCnt = Integer.parseInt(numPlayers.getText());
			if(playerCnt > 0 && playerCnt < 9) {
				users = new ArrayList<TextField>(playerCnt); // instantiate text fields
				userLbls = new ArrayList<Label>(playerCnt); // instantiate labels
				players = new ArrayList<Player>(playerCnt); // instantiate players
				
				GridPane plyrPane = new GridPane();
				
				for(int i = 0; i < playerCnt; i++) {
					userLbls.add(new Label("Player " + i + " name: ")); // set labels
					plyrPane.add(userLbls.get(i), 0, i); // add labels to pane
					users.add(new TextField()); // set textfields
					plyrPane.add(users.get(i), 1, i); // add textfields to pane
				}
				
				plyrPane.add(start, 0, playerCnt); // adds start button to pane
				
				Scene plyrScene = new Scene(plyrPane, 300, 250);
				primaryStage.setScene(plyrScene);
			}
		});
		
		start.setOnAction(event -> {
			for(int i = 0; i < users.size(); i++) {
				players.add(new Player(users.get(i).getText())); // create a new player object
				table.addPlayer(players.get(i)); // add the new player object to the table
			}
			gmScreen.setTable(table); // pass table to gmScreen
			gmScreen.setCurPlayer(0); // set gmScreen current player to the player in index 0 of players
			primaryStage.setScene(gmScreen.getScene()); // switch to gmScreen
		});
	}
	
	// Sets the image file for the menu's background
	public void addImages() {
		FileInputStream mimg;
		try {
			mimg = new FileInputStream("Images/darkGreenCloth.jpg");
			menuBG.setImage(new Image(mimg));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
