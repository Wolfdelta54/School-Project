package application;

public class Card
{
	public static final int DIAMONDS = 1;
	public static final int CLUBS = 2;
	public static final int HEARTS = 3;
	public static final int SPADES = 4;
	
	/* Suit[0] is not used ("*")
	 * Suit[1] contains the string "d" (DIAMOND)
	 * . . .
	 * Suit[4] contains the string "s" (SPADE)
	 * ---------------------------------------------------------------------------------------------------------------
	 * Rank[0] is not used ("*")
	 * Rank[1] contains the string "A" (ACE)
	 * Rank[2] contains the string "2"
	 * . . .
	 * Rank[11] contains the string "J" (JACK)
	 * Rank[12] contains the string "Q" (QUEEN)
	 * Rank[13] contains the string "K" (KING)
	 * Rank[14] contains the string "A" (ACE) */
	private static final String[] suit = {"*", "d", "c", "h", "s"};
	private static final String[] rank = {"*", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	
	/* value = (10 * rank) + suit
	 * Example: value = (10 * 14(ACE)) + 4(SPADES) = 144 (ACE OF SPADES) */
	private int value;
	
	public Card(int suit, int rank)
	{
		if(rank == 1)
			value = (10 * 14) + suit; // ACE is given rank 14
		else
			value = (10 * rank) + suit;
	}
	
	public int suit()
	{
		return value % 10; // Example: ACE OF SPADES = 144 :: 144 % 10 = 14 r.4
	}
	
	public String StrSuit()
	{
		return suit[value % 10];
	}
	
	public int rank()
	{
		return value / 10; // Example: ACE OF SPADES = 144 :: 144 / 10 = 14
	}
	
	public String StrRank()
	{
		return rank[value / 10];
	}
	
	public String toString()
	{
		return suit[value % 10] + rank[value / 10];
	}
}