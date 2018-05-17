package application;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Player {
	public int bal = 500; // Player's current balance
	public int potIn = 0;
	public String name = ""; // Player's username
//	public Hand hand;
	public boolean isActive = true; // Used to determine if the Player is still in the round
	public boolean isCurrent = false; // Used to determine if the Player is the currently Active Player
	public Button fold = new Button("Fold"), call = new Button("Call"), raise = new Button("Raise"),
			bet = new Button("Bet"), check = new Button("Check"); // Buttons for each different action the Player can take
	public TextField betAmount; // Player inputs the amount they wish to bet/raise
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBal() {
		return bal;
	}
	
/*	public Hand getHand() {
		return this.hand;
	} */
	
	public boolean getActive() {
		return isActive;
	}
	
	public boolean getCurrent() {
		return isCurrent;
	}
	
	public int getBet() {
		return potIn;
	}
	
	public void updateBal(int amount) {
		if(amount < 0) {
			int temp = Math.abs(amount);
			if(temp <= bal && isCurrent == true) {
				bal = bal - temp;
				potIn = temp;
			}
			else {
				betAmount.setText("Invalid Amount");
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				betAmount.setText("");
			}
		}
		else if(amount > 0) {
			bal += amount;
		}
	}
}
