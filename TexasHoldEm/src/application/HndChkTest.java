package application;

public class HndChkTest {
	public static void main(String args[]) {
		
		Player player1 = new Player("Test1");
		Player player2 = new Player("Test2");
		Player player3 = new Player("Test3");
		
		TableDupe table = new TableDupe();
		
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		
		table.deal();
		
		System.out.println("Player 1, card 1: " + player1.getHand().getCards().get(0).toString());	
		System.out.println("Player 1, card 2: " + player1.getHand().getCards().get(1).toString());	
		System.out.println("Player 2, card 1: " + player2.getHand().getCards().get(0).toString());	
		System.out.println("Player 2, card 2: " + player2.getHand().getCards().get(1).toString());	
		System.out.println("Player 3, card 1: " + player3.getHand().getCards().get(0).toString());	
		System.out.println("Player 3, card 2: " + player3.getHand().getCards().get(1).toString());	
		
		for(int i = 0; i < 5; i++)
		{
			System.out.println("River, card " + i + ": " + table.getRiver().getCards().get(i).toString());	
		}
		
		HandCheck hndChk = new HandCheck();
		hndChk.setCards(table.getRiver());
		hndChk.setPlayers(table.getPlayers());
		System.out.println(hndChk.checkHands());
	}
}
