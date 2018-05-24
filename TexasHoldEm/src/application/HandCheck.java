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
	
	
	public String checkHands()
	{
		String result = ""; 
		ArrayList<Integer> rankCounter = new ArrayList<Integer>();
		for(int i=0; i<14; i++)
			rankCounter.add(0);
		
	    ArrayList<Integer> suitCounter = new ArrayList<Integer>();
	    for(int i=0; i<4; i++)
			suitCounter.add(0);
	    
		// Loop through sorted cards and total ranks
        for(int i=0; i<availableCards.size();i++)
        {
        	if(availableCards.get(i).getRank() == 14)
        		rankCounter.set(0, (1+rankCounter.get(i))); 
            rankCounter.set(availableCards.get(i).getRank() -1,(1+rankCounter.get(i))); //ex. Queen will return 12. Therefore, array at the 12th position, or index 11, goes up 1 so subtract 1
            // [0,0,0,0,0,0,0,0,0,0,0,0,0,0] 
            // [A,2,3,4,5,6,7,8,9,10,J,Q,K,A]
            // [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
            // need to move ace into the first position
            suitCounter.set(availableCards.get(i).getSuit() -1,(1+suitCounter.get(i))); //ex. spade will return 4. Therefore, array list at index 3 goes up so subtract 1
        }

        //sort cards for evaluation

        //hands are already sorted by rank and suit for royal and straight flush checks.
        
        //is royal flush?
        result = royalFlush1(rankCounter, suitCounter);

        //is straight flush?
        if (result == null || result.length() == 0)
        	result = straightFlush(); 
        //is four of a kind?
        if (result == null || result.length() == 0)
        	result = fourOfAKind(rankCounter);
        //is full house?
        if (result == null || result.length() == 0)
        	result = fullHouse(rankCounter);
        //is flush?
        if (result == null || result.length() == 0)
        	result = flush(rankCounter, suitCounter);
        //is straight?
        if (result == null || result.length() == 0)
        {
        	// re-sort by rank, up to this point we had sorted by rank and suit
        	// but a straight is suit independent.
        	result = straight(rankCounter);
        }
        //is three of a kind?
        if (result == null || result.length() == 0)
        	result = evaluateThreeOfAKind(rankCounter);

        //is two pair?
        if (result == null || result.length() == 0)
        	result = evaluateTwoPair(rankCounter);

        //is one pair?
        if (result == null || result.length() == 0)
        	result = evaluateOnePair(rankCounter);


        //is highest hand? 
        if (result == null || result.length() == 0)
        	result = evaluateHighCard(rankCounter);
        
        return result; 
	}

	//ORDER OF SUITS = spades, hearts, clubs, diamond
		
	//RoyalFlush - 10JQKA of same suit
	private String royalFlush1(ArrayList<Integer> rankCounter, ArrayList<Integer> suitCounter) 
	{
		if((rankCounter.get(9)>= 1 &&       	//10
                rankCounter.get(10) >= 1 &&   //Jack
                rankCounter.get(11) >= 1 &&  //Queen
                rankCounter.get(12) >= 1 &&  //King
                rankCounter.get(0) >= 1)    //Ace
                && (suitCounter.get(0) > 4 || suitCounter.get(1) > 4 ||
                        suitCounter.get(2) > 4 || suitCounter.get(3) > 4))
			return "Royal Flush"; 
		return ""; 
	}
	private String royalFlush() 
	{
		for(int i=0; i<availableCards.size(); i++) //go through all available cards 
		{
				if(availableCards.get(i).getSuit() == 4) //checks to see if all are spades
				{
					if((availableCards.get(0).getRank() == 10 
							&& availableCards.get(1).getRank() == 11 
							&& availableCards.get(2).getRank() == 12 
							&& availableCards.get(3).getRank() == 13 
							&& availableCards.get(4).getRank() == 14 ))  //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Spades"; 
				}
				
				if(availableCards.get(i).getSuit() == 3) //checks to see if all are hearts
				{
					if((availableCards.get(0).getRank() == 10 
							&& availableCards.get(1).getRank() == 11 
							&& availableCards.get(2).getRank() == 12 
							&& availableCards.get(3).getRank() == 13 
							&& availableCards.get(4).getRank() == 14 )) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Hearts";
				}
				if(availableCards.get(i).getSuit() == 2) //checks to see if all are clubs
				{
					if((availableCards.get(0).getRank() == 10 
							&& availableCards.get(1).getRank() == 11 
							&& availableCards.get(2).getRank() == 12 
							&& availableCards.get(3).getRank() == 13 
							&& availableCards.get(4).getRank() == 14 )) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Clubs";
				}

				if(availableCards.get(i).getSuit() == 1) //checks to see if all are diamond
				{
					if((availableCards.get(0).getRank() == 10 
							&& availableCards.get(1).getRank() == 11 
							&& availableCards.get(2).getRank() == 12 
							&& availableCards.get(3).getRank() == 13 
							&& availableCards.get(4).getRank() == 14 )) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Diamonds";
				}
		}
		
		return ""; 
		
	}

		//StraightFlush - sequential numbers of same suit
	private String straightFlush1(ArrayList<Integer> rankCounter, ArrayList<Integer> suitCounter)
	{
	        String result = "";

	        if (suitCounter.get(0) > 4 || suitCounter.get(1) > 4 ||
	        		suitCounter.get(2) > 4 || suitCounter.get(4) > 4)
	        {
	        	// min. requirements for a straight flush have been met.
	        	// Loop through available cards looking for 5 consecutive cards of the same suit,
	        	// start in reverse to get the highest value straight flush
	        	for (int i=availableCards.size()-1;i>3;i--){
	        		if ((availableCards.get(i).getRank()-1 == availableCards.get(i-1).getRank() && 
	        				availableCards.get(i).getRank()-2 == availableCards.get(i-2).getRank() &&
	        				availableCards.get(i).getRank()-3 == availableCards.get(i-3).getRank() &&
	        				availableCards.get(i).getRank()-4 == availableCards.get(i-4).getRank()) 
	        				&&
	        				(availableCards.get(i).getSuit() == availableCards.get(i-1).getSuit() &&
	        				availableCards.get(i).getSuit() == availableCards.get(i-2).getSuit() &&
	        				availableCards.get(i).getSuit() == availableCards.get(i-3).getSuit() &&
	        				availableCards.get(i).getSuit() == availableCards.get(i-4).getSuit())){
	        			// Found royal flush, break and return.
	        			result = "Straight Flush!! " + availableCards.get(i).getSuit();
	        			break;
	        		}
	        	}
	        }
	        return result;
	}
	        
	private String straightFlush()
	{
		for (int i=availableCards.size()-1;i>3;i--)
		{
			if(availableCards.get(i).getSuit() == 4) //checks to see if all are spades
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Spades";
			}
			
			if(availableCards.get(i).getSuit() == 3) //checks to see if all are hearts
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Hearts"; 
			}
			
			if(availableCards.get(i).getSuit() == 2) //checks to see if all are clubs
			{
				if ((availableCards.get(i).getRank()- 1 == availableCards.get(i-1).getRank() && 
						availableCards.get(i).getRank()- 2 == availableCards.get(i-2).getRank() &&
						availableCards.get(i).getRank()- 3 == availableCards.get(i-3).getRank() &&
						availableCards.get(i).getRank()- 4 == availableCards.get(i-4).getRank()))
					return "Straight Flush Clubs";
			}
			
			if(availableCards.get(i).getSuit() == 1) //checks to see if all are diamonds
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
	private String fourOfAKind(ArrayList<Integer> cardRanks)
	{
			if((cardRanks.get(0) == cardRanks.get(1) && cardRanks.get(0) == cardRanks.get(2) && cardRanks.get(0) == cardRanks.get(3)) ||
					(cardRanks.get(1) == cardRanks.get(2) && cardRanks.get(1) == cardRanks.get(3) && cardRanks.get(1) == cardRanks.get(4)))
				return "Four of a Kind"; 
			return ""; 
			
	}
		
		//FullHouse - pair of matching cards + three other matching
	private String fullHouse(ArrayList<Integer> rankCounter)
	{
		String result = "";
		int threeOfKindRank = -1;
		int twoOfKindRank = -1;

		for (int i=rankCounter.size(); i>0; i--)
		{
			if ((threeOfKindRank < 0) || (twoOfKindRank < 0))
			{
				if ((rankCounter.get(i-1) > 2))
				{
					threeOfKindRank = i-1;                  
				}
				else if ((rankCounter.get(i-1) > 1))
				{
					twoOfKindRank = i-1;
				}
			}
			else
			{
				break;
			}
		}

		if ((threeOfKindRank >= 0) && (twoOfKindRank >= 0))
		{
			result = "Full House: ";
		}

		return result;
	}
		//Flush - all same suit
	private String flush(ArrayList<Integer> rankCounter, ArrayList<Integer> suitCounter)
	{
		String result = "";

		// verify at least 1 suit has 5 cards or more.
		if (suitCounter.get(0) > 4 || suitCounter.get(1) > 4 ||
				suitCounter.get(2) > 4 || suitCounter.get(3) > 4)
		{

			for (int i=availableCards.size()-1; i>3 ; i--)
			{
				if (availableCards.get(i).getSuit() == availableCards.get(i-1).getSuit() &&
						availableCards.get(i).getSuit() == availableCards.get(i-2).getSuit() &&
						availableCards.get(i).getSuit() == availableCards.get(i-3).getSuit() &&
						availableCards.get(i).getSuit() == availableCards.get(i-4).getSuit())
				{
					result = "Flush!! ";
					break;
				}
			}           
		}


		return result;
	}

		//Straight - sequential order different suit
	private String straight(ArrayList<Integer> rankCounter)
	{
		String result = "";

		// loop through rank array to check for sequence
		for (int i=rankCounter.size(); i>4; i--)
		{
			if ((rankCounter.get(i-1) > 0) &&
					(rankCounter.get(i-2) > 0) &&
					(rankCounter.get(i-3) > 0) &&
					(rankCounter.get(i-4) > 0) &&
					(rankCounter.get(i-5) > 0))
			{
				result = "Straight ";
				break;
			}
		}
		return result;
	}

		//ThreeOfAKind - three of the same card
	private String evaluateThreeOfAKind(ArrayList<Integer> rankCounter)
	{
		String result = "";

		// find three of the same card by looping through
		for (int i=rankCounter.size(); i>0; i--)
		{
			if (rankCounter.get(i-1) > 2){
				result = "Three of a Kind ";
				break;
			}
		}
		return result;
	}
		//TwoPairs - two different pairs
	private String evaluateTwoPair(ArrayList<Integer> rankCounter)
	{
		String result = "";
		int firstPairRank = -1;
		int secondPairRank = -1;

		for (int i=rankCounter.size();i>0;i--)
		{
			if ((firstPairRank < 0) || (secondPairRank < 0))
			{             
				if (((rankCounter.get(i-1)) > 1) && (firstPairRank < 0))
				{
					firstPairRank = i-1;                    
				}
				else if ((rankCounter.get(i-1)) > 1)
				{
					secondPairRank = i-1;
				}
			}
			else
			{
				// two pair found, break loop.
				break;
			}
		}

		// populate output
		if ((firstPairRank >= 0) && (secondPairRank >= 0))
		{
			if (secondPairRank == 0)
			{
				// Aces serve as top rank but are at the bottom of the rank array
				// swap places so aces show first as highest pair
				result = "Two Pair: " + availableCards.get(secondPairRank).getRank() + "'s and " + availableCards.get(firstPairRank).getRank() + "'s";
			}
			else 
			{
				result = "Two Pair: " + availableCards.get(firstPairRank).getRank() + "'s and " + availableCards.get(secondPairRank).getRank() + "'s";
			}           
		}

		return result;
	}
		//OnePair - one pair of same cards 
	private String evaluateOnePair(ArrayList<Integer> rankCounter)
	{
		String result = "";

		for (int i=rankCounter.size();i>0;i--)
		{
			if((rankCounter.get(i-1)) > 1)
			{
				result = "One Pair ";    
				break;
			}
		}
		return result;
	}
		//HighHand - highest card wins 
	 private String evaluateHighCard(ArrayList<Integer> rankCounter)
	 {
	        String result = "";

	        for (int i=rankCounter.size();i>0;i--)
	        {
	            if((rankCounter.get(i-1)) > 0)
	            {
	                result = "High Card ";
	                break;
	            }
	        }
	        return result;
	    }

	
}
