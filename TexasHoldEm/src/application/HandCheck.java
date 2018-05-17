package application;

import java.util.ArrayList;
import java.util.Arrays;

public class HandCheck
{
	public ArrayList<Player> players;
	public ArrayList<Card> availableCards; 
	public int pot; 
	public Player winner;
	
	
	public HandCheck()
	{
		players = new ArrayList<Player>(); 
		pot = 0; 
	}
	
	public void addCard(Card card)
	{
		availableCards.add(card);
	}

	public Card getCard(int i)
	{
		return availableCards.get(i);
	}

	public int numCards()
	{
		return availableCards.size();
	}

	public void setPlayers(ArrayList<Player> players)
	{
		this.players = players; 
	}
	
	public void setRiver(River river)
	{
		this.availableCards = river.getCards(); 
	}
	
	//make method to sequence the cards in one hand from lowest to highest SUIT
	public void sortBySuit()
	{
        ArrayList.sort(availableCards, new suitComparator());
    }

	//make method to sequence the cards in one hand from lowest to highest SUIT and then RANK
    public void sortBySuitThenRank()
    {
        ArrayList.sort(availableCards, new rankComparator());
    }

    //make method to sequence the cards in one hand from lowest to highest RANK and then SUIT
    public void sortByRankThenSuit()
    {
        ArrayList.sort(availableCards, new rankComparator());
        ArrayList.sort(availableCards, new suitComparator());
    }
	public String checkHands()
	{
		String result = ""; 
		//ORDER OF SUITS = spades, hearts, clubs, diamond
		
		//RoyalFlush - 10JQKA of same suit
		for(int i=0; i<availableCards.size(); i++)
		{
			if(availableCards.get(i).getCards().getSuit().equals("spades"))
			{
				if(availableCards.get(0).getCards().getValue.equals("10") && availableCards.get(1).getCards().getValue.equals("J") && availableCards.get(2).getCards().getValue.equals("Q") &&
						availableCards.get(3).getCards().getValue.equals("K") && availableCards.get(4).getCards().getValue.equals("A"))
					howWin = "Royal Flush Spades"; 
			}
			if(availableCards.get(i).getCards().getSuit().equals("hearts"))
			{
				if(availableCards.get(0).getCards().getValue.equals("10") && availableCards.get(1).getCards().getValue.equals("J") && availableCards.get(2).getCards().getValue.equals("Q") &&
						availableCards.get(3).getCards().getValue.equals("K") && availableCards.get(4).getCards().getValue.equals("A"))
					howWin = "Royal Flush Hearts";
			}
			if(availableCards.get(i).getCards().getSuit().equals("clubs"))
			{
				if(availableCards.get(0).getCards().getValue.equals("10") && availableCards.get(1).getCards().getValue.equals("J") && availableCards.get(2).getCards().getValue.equals("Q") &&
						availableCards.get(3).getCards().getValue.equals("K") && availableCards.get(4).getCards().getValue.equals("A"))
					howWin = "Royal Flush Clubs";
			}
			
			if(availableCards.get(i).getCards().getSuit().equals("diamonds"))
			{
				if(availableCards.get(0).getCards().getValue.equals("10") && availableCards.get(1).getCards().getValue.equals("J") && availableCards.get(2).getCards().getValue.equals("Q") &&
						availableCards.get(3).getCards().getValue.equals("K") && availableCards.get(4).getCards().getValue.equals("A"))
					howWin = "Royal Flush Diamonds";
			}

		//StraightFlush - sequential numbers of same suit
		
		//FourOfAKind - four of same card
		
		//FullHouse - pair of matching cards + three other matching
		
		//Flush - all same suit
		
		//Straight - sequential order different suit
		
		//ThreeOfAKind - three of the same card
		
		//TwoPairs - two different pairs
		
		//OnePair - one pair of same cards 
		
		//HighHand - highest card wins 
		}
	}
	
}
