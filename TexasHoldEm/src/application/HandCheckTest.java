package application;

import java.util.ArrayList;

public class HandCheckTest
{
	public static void main(String args[])
	{
		Player test = new Player("Darian");
		Player test2 = new Player("Thomas");
		Player test3 = new Player("Garret");
		 
		Table table = new Table();
		table.addPlayer(test);
		table.addPlayer(test2);
		table.addPlayer(test3);
		table.deal();
		
		HandCheck check = new HandCheck();
		check.setPlayers(table.getPlayers());
		check.setCards(table.getRiver());
		
		System.out.println(check.checkHands());
	}
}
