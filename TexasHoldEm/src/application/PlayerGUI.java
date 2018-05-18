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

public class PlayerGUI extends Application {
	public String userName = ""; // Username storage for multiplayer reasons
	public int port = 4444;
	
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
	public int curBet = 0;
	
	@Override
	public void start(Stage primaryStage) {
        
        addListeners();
        addImages();
        
        Scene scene = new Scene(gameStage, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void addListeners() {
		betAmount.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
		        if (!newVal.matches("\\d*")) {
		            betAmount.setText(newVal.replaceAll("[^\\d]", ""));
		        }
			}
		});
		
		fold.setOnAction(event -> {
			player.setActive(false);
			player.setCurrent(false);
		});
		
		bet.setOnAction(event -> {
			if(!betAmount.getText().trim().equals("") && betAmount.getText() != null) {
				int bet = Integer.parseInt(betAmount.getText());
				player.updateBal(-bet, betAmount);
				player.setCurrent(false);
			}
		});
		
		raise.setOnAction(event -> {
			if(!betAmount.getText().trim().equals("") && betAmount.getText() != null) {
				int raise = Integer.parseInt(betAmount.getText());
				player.updateBal(-(curBet + raise), betAmount);
				player.setCurrent(false);
			}
		});
		
		call.setOnAction(event -> {
			if(!betAmount.getText().trim().equals("") && betAmount.getText() != null) {
				player.updateBal(-curBet, betAmount);
				player.setCurrent(false);
			}
		});
		
		check.setOnAction(event -> {
			player.setCurrent(false);
		});
	}
	
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
	
	public void setCurBet(int num) {
		curBet = num;
	}
}
