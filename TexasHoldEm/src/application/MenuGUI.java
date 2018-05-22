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

public class MenuGUI extends Application {
	// Multiplayer connectivity variables
	public String userName = ""; // Username storage for multiplayer reasons
	public int port = 4444; // Server port placce holder
	public String hostIP = "0.0.0.0"; // Server IP place holder
	
	// Main Menu GUI components
	public Group mainMenu = new Group();
	public GridPane mainMenuOptions = new GridPane();
	public ImageView menuBG = new ImageView(); // Main menu background
	public Button servers = new Button("Connect to a Server"), host = new Button("Host a game"), quit = new Button("Exit Game"); // Main menu buttons
	public TextField user = new TextField(); // Username input
	public Label userLabel = new Label("User Name:");
	public Label title = new Label("Texas Hold'Em");
	
	public Player player; // Player object
	
	@Override
	public void start(Stage primaryStage) {
		// New pane for Username components
		GridPane userPane = new GridPane();
		userPane.add(user, 1, 0);
		userPane.add(userLabel, 0, 0);
        
		// Button listener for Quit button
        quit.setOnAction(event->
                primaryStage.close());
        
        // Adding IDs to the components
        title.setId("title");
        
        // Adding GUI components to the pane
        mainMenuOptions.add(title, 0, 0);
        mainMenuOptions.add(userPane, 0, 1);
        mainMenuOptions.add(servers, 0, 2);
        mainMenuOptions.add(host, 0, 3);
        mainMenuOptions.add(quit, 0, 4);
        
        // Setting visiblity of Game buttons
        servers.setVisible(false);
        host.setVisible(false);
        
        addListeners();
        addImages();
        
        // Set position of MenuOptions
        mainMenuOptions.setTranslateX(50);
        mainMenuOptions.setTranslateY(50);
        
        // Add the Background image and MenuOptions to the main pane
        mainMenu.getChildren().add(menuBG);
        mainMenu.getChildren().add(mainMenuOptions);
        
        Scene scene = new Scene(mainMenu, 300, 250);
        
        // Add style sheet to Scene
        scene.getStylesheets().add("application/Menu.css");
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Adds change listener to the UserName textfield
	public void addListeners() {
		user.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
				if(newVal.trim().equals("") || newVal == null) {
					servers.setVisible(false);
					host.setVisible(false);
				}
				else {
					servers.setVisible(true);
					host.setVisible(true);
				}
			}
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
