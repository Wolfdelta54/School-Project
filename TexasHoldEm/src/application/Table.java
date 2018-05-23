package application;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Table extends Application
{

	private int pot = 0; // initializes pot to 0
	private final ArrayList<Player> players; // list of players and their attributes
	private final ArrayList<River> riverCards;
	private final DeckOfCards deck = new DeckOfCards();
	private int port = 4444;
	

	public Table()
	{
		players = new ArrayList<Player>();
		deck.shuffle(); // shuffles deck

	}

	

	public int getPot()
	{		
		return pot; // returns pot
	}

	public void deal()
	{
		for(int i = 0; i < 2; i ++)
		{
			for(int j = 0; j < players.size(); j++)
			{
				players[j].addCard(deck.nextCard()); // adds a card to the player's deck
			}

		}

		
		for(int i = 0; i < 5; i++)
		{
			river[i].addCard(deck.nextCard()); // adds 5 cards to the river

			
		}

	}

	@Override
	public void start(Stage primaryStage) {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World!");
			}
		});

		StackPane root = new StackPane();
		root.getChildren().add(btn);

		Scene scene = new Scene(root, 300, 250);

		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}