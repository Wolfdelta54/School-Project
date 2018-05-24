package application;

import java.util.ArrayList;

public class Hand
{
	// public static final int NHCARDS = 2; // 2 cards in a hand
	
	private ArrayList<Card> handCards;
	
	// Creates the players hand
	public Hand()
	{
		handCards = new ArrayList<Card>();
	}
	
	// Returns the cards in the players hand
	public ArrayList<Card> getCards()
	{
		return handCards;
	}
	
	// Adds a card to the players hand
	public void addCard(Card card)
	{
		handCards.add(card);
	}
	
	// Resets the players hand
	public void resetCard()
	{
		handCards.clear();
	}
}
