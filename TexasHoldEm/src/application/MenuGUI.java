package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuGUI extends Application {
	// Network scanner, scans for available hosts
	public NetworkScanner netscan = new NetworkScanner();
	
	// Multiplayer connectivity variables
	public String userName = ""; // Username storage for multiplayer reasons
	public int port = 4444; // Server port placce holder
	public String ipBase = "10.8.51.X";
	public int machineID;
	
	// Main Menu GUI components
	public Group mainMenu = new Group();
	public GridPane mainMenuOptions = new GridPane();
	public ImageView menuBG = new ImageView(); // Main menu background
	public Button servers = new Button("Join Game"), host = new Button("Host a game"), quit = new Button("Exit Game"); // Main menu buttons
	public TextField user = new TextField(); // Username input
	public Label userLabel = new Label("User Name:");
	public Label title = new Label("Texas Hold'Em");
	public Scene scene;
	public Scene gameScene;
	
	// Server Join components
	public GridPane serverPane = new GridPane();
	public TextField server = new TextField();
	public Label serverLabel = new Label("Server IP:");
	
	@Override
	public void start(Stage primaryStage) {
		// New pane for Username components
		GridPane userPane = new GridPane();
		userPane.add(user, 1, 0);
		userPane.add(userLabel, 0, 0);
		
		// Set up server sub-pane
		serverPane.add(serverLabel, 0, 0);
		serverPane.add(server, 1, 0);
        
		// Button listener for Quit button
        quit.setOnAction(event->
                primaryStage.close());
        
        // Adding IDs to the components
        title.setId("title");
        
        // Adding GUI components to the pane
        mainMenuOptions.add(title, 0, 0);
        mainMenuOptions.add(userPane, 0, 1);
        mainMenuOptions.add(serverPane, 0, 2);
        mainMenuOptions.add(servers, 0, 3);
        mainMenuOptions.add(host, 0, 4);
        mainMenuOptions.add(quit, 0, 5);
        
        // Setting visiblity of Game buttons
        servers.setVisible(false);
        host.setVisible(false);
        
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
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Adds change listener to the UserName textfield
	public void addListeners(Stage primaryStage) {
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
		
		servers.setOnAction(event -> {
			String ipStorage = server.getText();
			String results = netscan.ping(ipStorage);
			
			if(!results.equals("Connecting")) {
				server.setText(results);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				server.setText("");
			}
			else {
				PlayerGUI play = new PlayerGUI(user.getText(), ipStorage, 4444);
				play.sendJoin();
				Thread playStart = new Thread(play);
				playStart.start();
				gameScene = play.getScene();
				primaryStage.setScene(gameScene);
			}
		});
		
		host.setOnAction(event -> {
			Table table = new Table(4444);
			Thread srvStart = new Thread(table);
			srvStart.start();
			
			String ipStorage = "0.0.0.0";
			
			Label test = new Label("This is a test");
			
			try {
				InetAddress ipAddr = InetAddress.getLocalHost();
				ipStorage = ipAddr.getHostAddress();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
			
			PlayerGUI play = new PlayerGUI(user.getText(), ipStorage, 4444);
			table.addPlayer(play.getPlayer());
			table.deal();
			play.addHand();
			Thread playStart = new Thread(play);
			playStart.start();
			BorderPane pane = new BorderPane();
		//	table.getPotLbl().setTranslateX(450);
		//	table.getPotLbl().setTranslateY(300);
		//	play.getPane().getChildren().add(table.getPotLbl());
			pane.setCenter(play.getPane());
			pane.setTop(table.getIpPane());
			gameScene = new Scene(pane, 1000, 750);
			primaryStage.setScene(gameScene);
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
	
	public void srvrPane() {
		// serverList, connectionList, empty
	}
}
