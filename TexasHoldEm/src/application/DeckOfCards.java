package application;

public class DeckOfCards
{
	public static final int NCARDS = 52; // 52 cards in a deck
	
	private Card[] deckOfCards;
	int currentCard; // Top card
	
	// Creates the deck of cards
	public DeckOfCards()
	{
		deckOfCards = new Card[NCARDS];
		
		int i = 0;
		for(int suit = Card.DIAMONDS; suit <= Card.SPADES; suit++) // DIAMONDS = 1 . . . SPADES = 4
		{
			for(int rank = 1; rank <= 13; rank++)
			{
				deckOfCards[i++] = new Card(suit, rank); // Card(DIAMONDS, 1)||(ACE OF DIAMONDS), Card(DIAMONDS, 2)||(2 OF DIAMONDS) . . . Card(SPADES, 13)||(KING OF SPADES)
			}
		}
		currentCard = 0;
	}
	
	// Shuffles the deck
	public void shuffle()
	{
		int x,y,z;
		
		for(x = 0; x < 1000; x++)
		{
			y = (int)(NCARDS * Math.random());
			z = (int)(NCARDS * Math.random());
			
			// Swap the cards (Shuffling)
			Card shuffle = deckOfCards[y];
			deckOfCards[y] = deckOfCards[z];
			deckOfCards[z] = shuffle;
		}
		
		currentCard = 0;
	}
	
	// Locates the top card in the deck
	public Card nextCard()
	{
		Card topCard = deckOfCards[currentCard];
		currentCard++;
		return topCard;
	}
	
	// Sets the index of currentCard back to the top (0)
	public void resetCard()
	{
		currentCard = 0;
	}
}
