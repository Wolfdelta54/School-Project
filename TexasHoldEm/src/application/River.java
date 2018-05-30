package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class River
{
	// public static final int NRCARDS = 5; // 5th card (Flop, Turn, and River cards)	
	
	private ArrayList<Card> riverCards;
	public GridPane pane = new GridPane();
	public ImageView card1 = new ImageView();
	public ImageView card2 = new ImageView();
	public ImageView card3 = new ImageView();
	public ImageView card4 = new ImageView();
	public ImageView card5 = new ImageView();
	public ArrayList<ImageView> cards = new ArrayList<ImageView>();
	
	// Creates the river
	public River()
	{
		riverCards = new ArrayList<Card>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		cards.add(card5);
	}
	
	// Returns the cards in the river
	public ArrayList<Card> getCards()
	{
		return riverCards;
	}
	
	// Adds a card to the river
	public void addCard(Card card)
	{
		riverCards.add(card);
	}
	
	// Resets river
	public void resetCard()
	{
		riverCards.clear();
	}
	
	public ArrayList<ImageView> getCardList() {
		return cards;
	}
	
	public GridPane getPane() {
		FileInputStream img1;
		FileInputStream img2;
		FileInputStream img3;
		FileInputStream img4;
		FileInputStream img5;
		try {
			img1 = new FileInputStream(riverCards.get(0).getImage(riverCards.get(0).getSuit(), riverCards.get(0).getRank()));
			img2 = new FileInputStream(riverCards.get(1).getImage(riverCards.get(1).getSuit(), riverCards.get(1).getRank()));
			img3 = new FileInputStream(riverCards.get(2).getImage(riverCards.get(2).getSuit(), riverCards.get(2).getRank()));
			img4 = new FileInputStream(riverCards.get(3).getImage(riverCards.get(3).getSuit(), riverCards.get(3).getRank()));
			img5 = new FileInputStream(riverCards.get(4).getImage(riverCards.get(4).getSuit(), riverCards.get(4).getRank()));
			card1.setImage(new Image(img1));
			card2.setImage(new Image(img2));
			card3.setImage(new Image(img3));
			card4.setImage(new Image(img4));
			card5.setImage(new Image(img5));
		
			card1.setFitWidth(75);
			card1.setPreserveRatio(true);
			card1.setSmooth(true);
			card1.setCache(true);
		
			card2.setFitWidth(75);
			card2.setPreserveRatio(true);
			card2.setSmooth(true);
			card2.setCache(true);
		
			card3.setFitWidth(75);
			card3.setPreserveRatio(true);
			card3.setSmooth(true);
			card3.setCache(true);
		
			card4.setFitWidth(75);
			card4.setPreserveRatio(true);
			card4.setSmooth(true);
			card4.setCache(true);
		
			card5.setFitWidth(75);
			card5.setPreserveRatio(true);
			card5.setSmooth(true);
			card5.setCache(true);
		
			pane.add(card1, 0, 0);
			pane.add(card2, 1, 0);
			pane.add(card3, 2, 0);
			pane.add(card4, 3, 0);
			pane.add(card5, 4, 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pane;
	}
}
