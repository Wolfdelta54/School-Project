package application;

import java.util.ArrayList;

public class River
{
	// public static final int NRCARDS = 5; // 5th card (Flop, Turn, and River cards)	
	
	private ArrayList<Card> riverCards;
	
	// Creates the river
	public River()
	{
		riverCards = new ArrayList<Card>();
	}
	
	// Returns the cards in the river
	public ArrayList<Card> getCards()
	{
		return riverCards;
	}
	
	// Adds a card to the river
	public void addCard(Card card)
	{
		riverCards.add(card);
	}
	
	// Resets river
	public void resetCard()
	{
		riverCards.clear();
	}
}
