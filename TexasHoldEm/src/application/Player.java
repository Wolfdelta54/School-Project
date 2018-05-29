package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Player {
	public int bal = 500; // Player's current balance
	public int potIn = 0;
	public String name = ""; // Player's username
	public Hand hand;
	public boolean isActive = true; // Used to determine if the Player is still in the round
	public boolean isCurrent = false; // Used to determine if the Player is the currently Active Player
	public String betAmount = "";
	public ArrayList<Card> cards = new ArrayList<Card>();
	public int curBet = 0;
	public GridPane handImgs = new GridPane(); // Easy GUI storage for cards in the hand
	public ImageView card1 = new ImageView();
	public ImageView card2 = new ImageView();
	public GridPane dummyHand = new GridPane(); // Easy GUI storage for hidden cards
	public ImageView dummy1 = new ImageView();
	public ImageView dummy2 = new ImageView();
	public int pot = 0;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCurPot() {
		return pot;
	}
	
	public int getBal() {
		return bal;
	}
	
	public Hand getHand() {
		return this.hand;
	} 
	
	public boolean getActive() {
		return isActive;
	}
	
	public boolean getCurrent() {
		return isCurrent;
	}
	
	public int getBet() {
		return potIn;
	}
	
	public int getCurBet() {
		return curBet;
	}
	
	public void setCurBet(int amount) {
		curBet = amount;
	}
	
	public void setPot(int newPot) {
		pot = newPot;
		System.out.println("Current pot (Player) " + pot);
	}
	
	public GridPane getHandPane() {
		FileInputStream img1;
		FileInputStream img2;
		try {
			img1 = new FileInputStream(cards.get(0).getImage(cards.get(0).getSuit(), cards.get(0).getRank()));
			img2 = new FileInputStream(cards.get(1).getImage(cards.get(1).getSuit(), cards.get(1).getRank()));
			card1.setImage(new Image(img1));
			card2.setImage(new Image(img2));
		
			card1.setFitWidth(75);
			card1.setPreserveRatio(true);
			card1.setSmooth(true);
			card1.setCache(true);
		
			card2.setFitWidth(75);
			card2.setPreserveRatio(true);
			card2.setSmooth(true);
			card2.setCache(true);
		
			handImgs.add(card1, 0, 0);
			handImgs.add(card2, 1, 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return handImgs;
	}
	
	public GridPane getDummyHand() {
		FileInputStream img1;
		FileInputStream img2;
		try {
			img1 = new FileInputStream(cards.get(0).getImage(0, 0));
			img2 = new FileInputStream(cards.get(1).getImage(0, 0));
			dummy1.setImage(new Image(img1));
			dummy2.setImage(new Image(img2));
		
			dummy1.setFitWidth(75);
			dummy1.setPreserveRatio(true);
			dummy1.setSmooth(true);
			dummy1.setCache(true);
		
			dummy2.setFitWidth(75);
			dummy2.setPreserveRatio(true);
			dummy2.setSmooth(true);
			dummy2.setCache(true);
		
			dummyHand.add(dummy1, 0, 0);
			dummyHand.add(dummy2, 1, 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dummyHand;
	}
	
	public void updateBal(int amount) {
		if(amount < 0) {
			int temp = Math.abs(amount);
			if(temp <= bal && isCurrent == true) {
				bal = bal - temp;
				potIn = temp;
			}
			else if(isCurrent == false) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if(amount > 0) {
			bal += amount;
		}
	}
	
	public void updateBal(int amount, TextField betAmount) {
		if(amount < 0) {
			int temp = Math.abs(amount);
			if(temp <= bal && isCurrent == true) {
				bal = bal - temp;
				potIn = temp;
			}
			else if(isCurrent == false) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				betAmount.setText("0");
			}
			else {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				betAmount.setText("0");
			}
		}
		else if(amount > 0) {
			bal += amount;
		}
	}
	
	public void addCard(Card card) {
		cards.add(card);
		
		if(cards.size() == 2) {
			hand = new Hand();
			hand.addCard(cards.get(0));
			hand.addCard(cards.get(1));
		}
	} 
	
	public void setActive(boolean status) {
		isActive = status;
	}
	
	public void setCurrent(boolean status) {
		isCurrent = status;
	}
}
