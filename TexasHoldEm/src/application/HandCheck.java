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
        ArrayList.sort(availableCards, new suitComparator()); //ORDER OF SUITS = spades, hearts, clubs, diamond
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
        ArrayList.sort(availableCards, new suitComparator()); //ORDER OF SUITS = spades, hearts, clubs, diamond
    }
	public String checkHands()
	{
		String result = ""; 
		ArrayList<Integer> rankCounter;
	    ArrayList<Integer> suitCounter;
	    
		// Loop through sorted cards and total ranks
        for(int i=0; i<availableCards.size();i++)
        {
            rankCounter.add(availableCards.get(i).getRank());
            suitCounter.add(availableCards.get(i).getSuit());
        }

        //sort cards for evaluation
        this.sortByRankThenSuit();

        //hands are already sorted by rank and suit for royal and straight flush checks.
        
        //is royal flush?

        //is straight flush?
       
        //is four of a kind?
        
        //is full house?
        
        //is flush?
        
        //is straight?
        
        //is three of a kind?
        
        //is two pair?
        
        //is one pair?
        
        //is highest hand? 
        }

	//ORDER OF SUITS = spades, hearts, clubs, diamond
		
	//RoyalFlush - 10JQKA of same suit
	
	private String royalFlush(ArrayList<Integer> rankCounter, ArrayList<Integer> suitCounter)
	{
		for(int i=0; i<availableCards.size(); i++) //go through all available cards 
		{
				if(availableCards.get(i).getSuit() == 4) //checks to see if all are spades
				{
					if((rankCounter.get(0) == 10 && rankCounter.get(1) == 11 && rankCounter.get(2) == 12 && rankCounter.get(3) == 13 && rankCounter.get(4) == 14 
							||rankCounter.get(0) == 14 && rankCounter.get(1) == 10 && rankCounter.get(2) == 11 && rankCounter.get(3) == 12 && rankCounter.get(4) == 13)) //Goes through rankCounter list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Spades"; 
				}
				
				if(availableCards.get(i).getSuit() == 3) //checks to see if all are hearts
				{
					if((rankCounter.get(0) == 10 && rankCounter.get(1) == 11 && rankCounter.get(2) == 12 && rankCounter.get(3) == 13 && rankCounter.get(4) == 14 
							||rankCounter.get(0) == 14 && rankCounter.get(1) == 10 && rankCounter.get(2) == 11 && rankCounter.get(3) == 12 && rankCounter.get(4) == 13)) //Goes through rankCounter list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Hearts";
				}
				if(availableCards.get(i).getSuit() == 2) //checks to see if all are clubs
				{
					if((rankCounter.get(0) == 10 && rankCounter.get(1) == 11 && rankCounter.get(2) == 12 && rankCounter.get(3) == 13 && rankCounter.get(4) == 14 
							||rankCounter.get(0) == 14 && rankCounter.get(1) == 10 && rankCounter.get(2) == 11 && rankCounter.get(3) == 12 && rankCounter.get(4) == 13)) //Goes through rankCounter list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Clubs";
				}

				if(availableCards.get(i).getSuit() == 1) //checks to see if all are diamond
				{
					if((rankCounter.get(0) == 10 && rankCounter.get(1) == 11 && rankCounter.get(2) == 12 && rankCounter.get(3) == 13 && rankCounter.get(4) == 14 
							||rankCounter.get(0) == 14 && rankCounter.get(1) == 10 && rankCounter.get(2) == 11 && rankCounter.get(3) == 12 && rankCounter.get(4) == 13)) //Goes through rankCounter list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Diamonds";
				}
		}
		
		return ""; 
		
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
