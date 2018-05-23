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
	
	public void setCards(River river)
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
		ArrayList<Integer> cardRanks = new ArrayList<Integer>();
	    ArrayList<Integer> cardSuits = new ArrayList<Integer>();
	    
		// Loop through sorted cards and total ranks
        for(int i=0; i<availableCards.size();i++)
        {
            cardRanks.add(availableCards.get(i).getRank());
            cardSuits.add(availableCards.get(i).getSuit());
        }

        //sort cards for evaluation
        this.sortByRankThenSuit();

        //hands are already sorted by rank and suit for royal and straight flush checks.
        
        //is royal flush?
        result = royalFlush(cardRanks, cardSuits);

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
	
	private String royalFlush(ArrayList<Integer> cardRanks, ArrayList<Integer> cardSuits) 
	{
		for(int i=0; i<availableCards.size(); i++) //go through all available cards 
		{
				if(cardSuits.get(i) == 4) //checks to see if all are spades
				{
					if((cardRanks.get(0) == 10 && cardRanks.get(1) == 11 && cardRanks.get(2) == 12 && cardRanks.get(3) == 13 && cardRanks.get(4) == 14 
							||cardRanks.get(0) == 14 && cardRanks.get(1) == 10 && cardRanks.get(2) == 11 && cardRanks.get(3) == 12 && cardRanks.get(4) == 13)) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Spades"; 
				}
				
				if(availableCards.get(i).getSuit() == 3) //checks to see if all are hearts
				{
					if((cardRanks.get(0) == 10 && cardRanks.get(1) == 11 && cardRanks.get(2) == 12 && cardRanks.get(3) == 13 && cardRanks.get(4) == 14 
							||cardRanks.get(0) == 14 && cardRanks.get(1) == 10 && cardRanks.get(2) == 11 && cardRanks.get(3) == 12 && cardRanks.get(4) == 13)) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Hearts";
				}
				if(availableCards.get(i).getSuit() == 2) //checks to see if all are clubs
				{
					if((cardRanks.get(0) == 10 && cardRanks.get(1) == 11 && cardRanks.get(2) == 12 && cardRanks.get(3) == 13 && cardRanks.get(4) == 14 
							||cardRanks.get(0) == 14 && cardRanks.get(1) == 10 && cardRanks.get(2) == 11 && cardRanks.get(3) == 12 && cardRanks.get(4) == 13)) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Clubs";
				}

				if(availableCards.get(i).getSuit() == 1) //checks to see if all are diamond
				{
					if((cardRanks.get(0) == 10 && cardRanks.get(1) == 11 && cardRanks.get(2) == 12 && cardRanks.get(3) == 13 && cardRanks.get(4) == 14 
							||cardRanks.get(0) == 14 && cardRanks.get(1) == 10 && cardRanks.get(2) == 11 && cardRanks.get(3) == 12 && cardRanks.get(4) == 13)) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Diamonds";
				}
		}
		
		return ""; 
		
	}

		//StraightFlush - sequential numbers of same suit
	private String straightFlush(ArrayList<Integer> cardRanks, ArrayList<Integer> cardSuits)
	{
		for (int i=availableCards.size()-1;i>3;i--)
		{
			if(cardSuits.get(i) == 4) //checks to see if all are spades
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Spades";
			}
			
			if(cardSuits.get(i) == 3) //checks to see if all are hearts
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Hearts"; 
			}
			
			if(cardSuits.get(i) == 2) //checks to see if all are clubs
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Clubs";
			}
			
			if(cardSuits.get(i) == 1) //checks to see if all are diamonds
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Diamonds";
			}
			
		}
		
		return ""; 
	}
		//FourOfAKind - four of same card
		
		//FullHouse - pair of matching cards + three other matching
		
		//Flush - all same suit
		
		//Straight - sequential order different suit
		
		//ThreeOfAKind - three of the same card
		
		//TwoPairs - two different pairs
		
		//OnePair - one pair of same cards 
		
		//HighHand - highest card wins 
		
	
}
