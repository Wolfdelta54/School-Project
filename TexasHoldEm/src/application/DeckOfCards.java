package application;

public class DeckOfCards
{
	private Card[] deckOfCards;
	
	public DeckOfCards()
	{
		deckOfCards = new Card[52];
		
		int i = 0;
		for(int suit = Card.DIAMONDS; suit <= Card.SPADES; suit++)
		{
			for(int rank = 1; rank <= 13; rank++)
			{
				deckOfCards[i++] = new Card(suit, rank);
			}
		}
	}
}
