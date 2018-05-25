package application;

import java.util.ArrayList;

import javafx.scene.control.TextField;

public class Player {
	public int bal = 500; // Player's current balance
	public int potIn = 0;
	public String name = ""; // Player's username
	public Hand hand;
	public boolean isActive = true; // Used to determine if the Player is still in the round
	public boolean isCurrent = false; // Used to determine if the Player is the currently Active Player
	public String betAmount = "";
	public ArrayList<Card> cards = new ArrayList<Card>();
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
