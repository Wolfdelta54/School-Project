package application;

public class Card
{
	private static final int DIAMONDS = 1;
	private static final int CLUBS = 2;
	private static final int HEARTS = 3;
	private static final int SPADES = 4;
	
	/* Suit[0] is not used ("*")
	 * Suit[1] contains the string "d" (DIAMOND)
	 * . . .
	 * Suit[4] contains the string "s" (SPADE)
	 * 
	 * Rank[0] & Rank[1] is not used ("*")
	 * Rank[2] contains the string "2"
	 * . . .
	 * Rank[11] contains the string "J" (JACK)
	 * Rank[12] contains the string "Q" (QUEEN)
	 * Rank[13] contains the string "K" (KING)
	 * Rank[14] contains the string "A" (ACE) */
	private static final String[] suit = {"*", "d", "c", "h", "s"};
	private static final String[] rank = {"*", "*", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	
	private int cardSuit;
	private int cardRank;
	
	public Card(int cardSuit, int cardRank)
	{
		
	}
}
