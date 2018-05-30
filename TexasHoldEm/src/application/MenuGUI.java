package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
	
	// Pane of components for the wait screen, shown only to hosts when server is started, requires at least 2 players to move on
	public Group waitPane = new Group();
	public Label info = new Label("2 players are required");
	public Label numPlayers = new Label();
	public Button start = new Button("Start");
	ImageView waitPaneBG = new ImageView();
	
	public PlayerGUI play; // Stores the PlayerGUI pane while the server is waiting to start the game
	
	// Client wait pane
	public boolean srvrLive = false;
	public Group clWaitPane = new Group();
	public Label clInfo = new Label("Please wait");
	ImageView clWaitPaneBG = new ImageView();
	
	Table table = new Table(4444);
	
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
        
        setUpWait();
        
        scene = new Scene(mainMenu, 300, 250);
        
        // Add style sheet to Scene
        scene.getStylesheets().add("application/Menu.css");
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	@Override
        	public void handle(WindowEvent e) {
        		Platform.exit();
        		System.exit(0);
        	}
        });
	}
	
	public void setUpWait() {		
		start.setTranslateX(125);
		start.setTranslateY(175);
		
		info.setTranslateX(125);
		info.setTranslateY(125);
		info.setFont(new Font(15));
		info.setTextFill(Paint.valueOf("WHITE"));
		
		clInfo.setTranslateX(25);
		clInfo.setTranslateY(25);
		clInfo.setFont(new Font(15));
		clInfo.setTextFill(Paint.valueOf("WHITE"));
		
		waitPaneBG.setTranslateY(15);
		
		waitPane.getChildren().add(waitPaneBG);
		waitPane.getChildren().add(start);
		waitPane.getChildren().add(info);
		waitPane.getChildren().add(table.getIpPane());
		
		clWaitPane.getChildren().add(clWaitPaneBG);
		clWaitPane.getChildren().add(clInfo);
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
				table.addPlayer(play.getPlayer());
				play.sendJoin();
				
				Thread playStart = new Thread(play);
				playStart.start();
				JLabel status = new JLabel();
				status.setText(play.isLive() + "");
				
				if(table.isLive() == false) {
					Label test = new Label();
					Scene wait = new Scene(clWaitPane, 300, 250);
					primaryStage.setScene(wait);
					AtomicInteger count = new AtomicInteger(-1);
					
					play.srvrLiveProperty.addListener(new ChangeListener<Number>() {
						@Override
						public void changed(final ObservableValue<? extends Number> observable, final Number oldVal, final Number newVal) {
							if(count.getAndSet(newVal.intValue()) == -1) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										if(play.srvrLive == true) {
											count.set(0);
											play.addHand();
											
											GridPane river = table.getRiverPane();
											river.setTranslateX(312);
											river.setTranslateY(180);
							
											play.getPane().getChildren().add(river);
							
											gameScene = play.getScene();
											primaryStage.setScene(gameScene);
										}
										else {
											int val = count.getAndSet(-1);
										}
									}
								});
							}
						}
					});
				/*	test.textProperty().addListener(new ChangeListener<String>() {
						@Override
						public void changed(final ObservableValue<? extends String> observable, final String oldVal, final String newVal) {
							if(newVal.equals("true")) {
								System.out.println(test.getText());
										play.addHand();
										
										GridPane river = table.getRiverPane();
										river.setTranslateX(312);
										river.setTranslateY(180);
						
										play.getPane().getChildren().add(river);
						
										gameScene = play.getScene();
										primaryStage.setScene(gameScene);
							}
							
						}
					}); */
					
				/*	Thread waitThread = new Thread(new Runnable() {
						public void run() {
							while(play.isLive() == false) {
								// do nothing
							}
							if(play.isLive() == true) {
								play.addHand();
				
								GridPane river = table.getRiverPane();
								river.setTranslateX(312);
								river.setTranslateY(180);
				
								play.getPane().getChildren().add(river);
				
								gameScene = play.getScene();
								primaryStage.setScene(gameScene);
							}
						}
					});
					waitThread.start(); */
				}
				else {
					play.addHand();
				
					GridPane river = table.getRiverPane();
					river.setTranslateX(312);
					river.setTranslateY(180);

					play.getPane().getChildren().add(river);

					gameScene = play.getScene();
					primaryStage.setScene(gameScene);
				}
			}
		});
		
		host.setOnAction(event -> {
			Thread srvStart = new Thread(table);
			srvStart.start();
			
			String ipStorage = "0.0.0.0";
		
			try {
				InetAddress ipAddr = InetAddress.getLocalHost();
				ipStorage = ipAddr.getHostAddress();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
		
			play = new PlayerGUI(user.getText(), ipStorage, 4444);
			table.addPlayer(play.getPlayer());
			table.prntPlayers();
			play.prntPlayer();
			play.sendJoin();
			
			Thread playStart = new Thread(play);
			playStart.start();
			Scene wait = new Scene(waitPane, 300, 250);
			primaryStage.setScene(wait);
		/*	Thread srvStart = new Thread(table);
			srvStart.start();
			
			String ipStorage = "0.0.0.0";
			
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
			play.sendJoin();
			
			GridPane river = table.getRiverPane();
			river.setTranslateX(312);
			river.setTranslateY(180);
			
			play.getPane().getChildren().add(river);
			Thread playStart = new Thread(play);
			playStart.start();
			BorderPane pane = new BorderPane();
		//	table.getPotLbl().setTranslateX(450);
		//	table.getPotLbl().setTranslateY(300);
		//	play.getPane().getChildren().add(table.getPotLbl());
			pane.setCenter(play.getPane());
			pane.setTop(table.getIpPane());
			gameScene = new Scene(pane, 1000, 750);
			primaryStage.setScene(gameScene); */
		});
		
		start.setOnAction(event -> {
			if(table.getNumPlayers() > 1) {
				table.setLive(true);
			
		/*		String ipStorage = "0.0.0.0";
			
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
				play.sendJoin();
			
				GridPane river = table.getRiverPane();
				river.setTranslateX(312);
				river.setTranslateY(180);
			
				play.getPane().getChildren().add(river);
				Thread playStart = new Thread(play);
				playStart.start(); */
				table.deal();
				play.addHand();
				table.prntPlayers();
				play.prntPlayer();
				
				GridPane river = table.getRiverPane();
				river.setTranslateX(312);
				river.setTranslateY(180);
			
				play.getPane().getChildren().add(river);
				BorderPane pane = new BorderPane();
			//	table.getPotLbl().setTranslateX(450);
			//	table.getPotLbl().setTranslateY(300);
			//	play.getPane().getChildren().add(table.getPotLbl());
				pane.setCenter(play.getPane());
				pane.setTop(table.getIpPane());
				gameScene = new Scene(pane, 1000, 750);
				primaryStage.setScene(gameScene);
			}
		});
	}
	
	// Sets the image file for the menu's background
	public void addImages() {
		FileInputStream mimg;
		FileInputStream mimg2;
		FileInputStream mimg3;
		try {
			mimg = new FileInputStream("Images/darkGreenCloth.jpg");
			mimg2 = new FileInputStream("Images/darkGreenCloth.jpg"); // To avoid a white screen when there should be a background
			mimg3 = new FileInputStream("Images/darkGreenCloth.jpg"); // Same as above
			menuBG.setImage(new Image(mimg));
			waitPaneBG.setImage(new Image(mimg2));
			clWaitPaneBG.setImage(new Image(mimg3));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void srvrPane() {
		// serverList, connectionList, empty
	}
}
