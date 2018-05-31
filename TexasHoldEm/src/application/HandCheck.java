package application;

import java.util.ArrayList;
import java.util.Collections; 

public class HandCheck
{
	public ArrayList<Player> players;
	public ArrayList<Card> availableCards; 
	public ArrayList<Card> allCards;
	public int pot; 
	public Player winner;
	public ArrayList<Hand> hands = new ArrayList<Hand>(); 
	public ArrayList<String> names = new ArrayList<String>();  
	public Hand hand;
	public ArrayList<String> handCombos = new ArrayList<String>();
	
	public HandCheck()
	{
		players = new ArrayList<Player>(); 
		pot = 0; 
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
		for(int i=0; i<players.size(); i++)
		{
			this.hands.add(players.get(i).getHand()); 
			this.names.add(players.get(i).getName());		
			}
			
	}
	
	public void setCards(River river)
	{
		this.availableCards = river.getCards(); 
		//availableCards.addAll(hand.getCards());
	}
	
	public void setList(int index)
	{
		allCards = new ArrayList<Card>(); 
		allCards.addAll(availableCards);
		allCards.addAll(hands.get(index).getCards()); 
		
	}
	
	public ArrayList<String> checkHands()
	{
		
		for(int i=0; i<players.size(); i++)
		{
		String result = ""; 
		setList(i); 
		
		int[] rankCounterArray = new int[15];
        int[] suitCounterArray = new int[5];

        // initializations
        for (int j=0;j<rankCounterArray.length;j++)
        {
            rankCounterArray[j] =0;
        }

        for (int j=4;j<suitCounterArray.length;j++)
        {
            suitCounterArray[j] = 0;
        }

        // Loop through sorted cards and total ranks
        for(int j=0; j<allCards.size();j++)
        {
            rankCounterArray[allCards.get(j).getRank()]++;
            suitCounterArray[allCards.get(j).getSuit()]++;
        }

        ArrayList<Integer> rankCounter = new ArrayList<Integer>(); 
        ArrayList<Integer> suitCounter = new ArrayList<Integer>();

        for(int j=0; j<rankCounterArray.length; j++)
        {
        	rankCounter.add(rankCounterArray[j]);
        }

        for(int j=0; j<suitCounterArray.length; j++)
        {
        	suitCounter.add(suitCounterArray[j]);
        }

        //ex. Queen will return 12. Therefore, array at the 12th position, or index 11, goes up 1 so subtract 1
        // [0,0,0,0,0,0,0,0,0,0,0,0,0,0] 
        // [A,2,3,4,5,6,7,8,9,10,J,Q,K,A]
        // [0,1,2,3,4,5,6,7,8,9,10,11,12,13]
        // need to move ace into the first position
        //ex. spade will return 4. Therefore, array list at index 3 goes up so subtract 1

        //sort cards for evaluation
        Collections.sort(suitCounter);
        Collections.sort(rankCounter);
        //hands are already sorted by rank and suit for royal and straight flush checks.

        //is royal flush?
        result = royalFlush1(rankCounter, suitCounter);

        //is straight flush?
        if (result.length() == 0)
        	result = straightFlush(); 
        //is four of a kind?
        if (result.length() == 0)
        	result = fourOfAKind(rankCounter);
        //is full house?
        if (result.length() == 0)
        	result = fullHouse(rankCounter);
        //is flush?
        if (result.length() == 0)
        	result = flush(rankCounter, suitCounter);
        //is straight?
        if (result.length() == 0)
        {
        	Collections.sort(rankCounter);
        	// re-sort by rank, up to this point we had sorted by rank and suit
        	// but a straight is suit independent.
        	result = straight(rankCounter);
        }
        //is three of a kind?
        if (result.length() == 0)
        	result = evaluateThreeOfAKind(rankCounter);

        //is two pair?
        if (result.length() == 0)
        	result = evaluateTwoPair(rankCounter);

        //is one pair?
        if (result.length() == 0)
        	result = evaluateOnePair(rankCounter);


        //is highest hand? 
        if (result.length() == 0)
        	result = evaluateHighCard(rankCounter);
			
        handCombos.add(players.get(i).getName() + ";" + result);
		}
        
        return handCombos; 
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
			return "Royal Flush" + allCards.get(1).getSuit(); 
		return ""; 
	}
	private String royalFlush() 
	{
		for(int i=0; i<allCards.size(); i++) //go through all available cards 
		{
				if(allCards.get(i).getSuit() == 4) //checks to see if all are spades
				{
					if((allCards.get(0).getRank() == 10 
							&& allCards.get(1).getRank() == 11 
							&& allCards.get(2).getRank() == 12 
							&& allCards.get(3).getRank() == 13 
							&& allCards.get(4).getRank() == 14 ))  //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Spades"; 
				}
				
				if(allCards.get(i).getSuit() == 3) //checks to see if all are hearts
				{
					if((allCards.get(0).getRank() == 10 
							&& allCards.get(1).getRank() == 11 
							&& allCards.get(2).getRank() == 12 
							&& allCards.get(3).getRank() == 13 
							&& allCards.get(4).getRank() == 14 )) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Hearts";
				}
				if(allCards.get(i).getSuit() == 2) //checks to see if all are clubs
				{
					if((allCards.get(0).getRank() == 10 
							&& allCards.get(1).getRank() == 11 
							&& allCards.get(2).getRank() == 12 
							&& allCards.get(3).getRank() == 13 
							&& allCards.get(4).getRank() == 14 )) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
						return "Royal Flush Clubs";
				}

				if(allCards.get(i).getSuit() == 1) //checks to see if all are diamond
				{
					if((allCards.get(0).getRank() == 10 
							&& allCards.get(1).getRank() == 11 
							&& allCards.get(2).getRank() == 12 
							&& allCards.get(3).getRank() == 13 
							&& allCards.get(4).getRank() == 14 )) //Goes through cardRanks list and checks to see if cards meet the value requirements of a royal flush
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
	        	for (int i=allCards.size()-1;i>3;i--)
	        	{
	        		if ((allCards.get(i).getRank()-1 == allCards.get(i-1).getRank() && 
	        				allCards.get(i).getRank()-2 == allCards.get(i-2).getRank() &&
	        				allCards.get(i).getRank()-3 == allCards.get(i-3).getRank() &&
	        				allCards.get(i).getRank()-4 == allCards.get(i-4).getRank()) 
	        				&&
	        				(allCards.get(i).getSuit() == allCards.get(i-1).getSuit() &&
	        				allCards.get(i).getSuit() == allCards.get(i-2).getSuit() &&
	        				allCards.get(i).getSuit() == allCards.get(i-3).getSuit() &&
	        				allCards.get(i).getSuit() == allCards.get(i-4).getSuit())){
	        			// Found royal flush, break and return.
	        			result = "Straight Flush!! " + allCards.get(i).getSuit();
	        			break;
	        		}
	        	}
	        }
	        return result;
	}
	        
	private String straightFlush()
	{
		for (int i=allCards.size()-1;i>3;i--)
		{
			if(allCards.get(i).getSuit() == 4) //checks to see if all are spades
			{
				if ((allCards.get(i).getRank()- 1 == allCards.get(i-1).getRank() && 
						allCards.get(i).getRank()- 2 == allCards.get(i-2).getRank() &&
						allCards.get(i).getRank()- 3 == allCards.get(i-3).getRank() &&
						allCards.get(i).getRank()- 4 == allCards.get(i-4).getRank()))
					return "Straight Flush Spades";
			}
			
			if(allCards.get(i).getSuit() == 3) //checks to see if all are hearts
			{
				if ((allCards.get(i).getRank()- 1 == allCards.get(i-1).getRank() && 
						allCards.get(i).getRank()- 2 == allCards.get(i-2).getRank() &&
						allCards.get(i).getRank()- 3 == allCards.get(i-3).getRank() &&
						allCards.get(i).getRank()- 4 == allCards.get(i-4).getRank()))
					return "Straight Flush Hearts"; 
			}
			
			if(allCards.get(i).getSuit() == 2) //checks to see if all are clubs
			{
				if ((allCards.get(i).getRank()- 1 == allCards.get(i-1).getRank() && 
						allCards.get(i).getRank()- 2 == allCards.get(i-2).getRank() &&
						allCards.get(i).getRank()- 3 == allCards.get(i-3).getRank() &&
						allCards.get(i).getRank()- 4 == allCards.get(i-4).getRank()))
					return "Straight Flush Clubs";
			}
			
			if(allCards.get(i).getSuit() == 1) //checks to see if all are diamonds
			{
				if ((allCards.get(i).getRank()- 1 == allCards.get(i-1).getRank() && 
						allCards.get(i).getRank()- 2 == allCards.get(i-2).getRank() &&
						allCards.get(i).getRank()- 3 == allCards.get(i-3).getRank() &&
						allCards.get(i).getRank()- 4 == allCards.get(i-4).getRank()))
					return "Straight Flush Diamonds";
			}
			
		}
		
		return ""; 
	}
		//FourOfAKind - four of same card
	private String fourOfAKind(ArrayList<Integer> cardRanks)
	{
		String result = "";

		for (int i=0;i<cardRanks.size();i++){
			if (cardRanks.get(i) == 4)
			{
				result = "Four of a Kind ";
				break;
			}
		}   
		return result;
	}
		
		//FullHouse - pair of matching cards + three other matching
	private String fullHouse(ArrayList<Integer> rankCounter)
	{
		String result = "";
		int threeOfKind = -1;
		int twoOfKind = -1;

		for (int i=rankCounter.size(); i>0; i--)
		{
			if ((threeOfKind < 0) || (twoOfKind < 0))
			{
				if ((rankCounter.get(i-1) > 2))
				{
					threeOfKind = i-1;                  
				}
				else if ((rankCounter.get(i-1) > 1))
				{
					twoOfKind = i-1;
				}
			}
			else
			{
				break;
			}
		}

		if ((threeOfKind >= 0) && (twoOfKind >= 0))
		{
			result = "Full House ";
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
					result = "Flush " + availableCards.get(2).getSuit();
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
		int firstPair = -1;
		int secondPair = -1;

		for (int i=rankCounter.size();i>0;i--)
		{
			if ((firstPair < 0) || (secondPair < 0))
			{             
				if (((rankCounter.get(i-1)) > 1) && (firstPair < 0))
				{
					firstPair = i-1;                    
				}
				else if ((rankCounter.get(i-1)) > 1)
				{
					secondPair = i-1;
				}
			}
			else
			{
				// two pair found, break loop.
				break;
			}
		}

		// output
		if ((firstPair >= 0) && (secondPair >= 0))
		{
			if (secondPair == 0)
			{
				// because the aces are the highest cards yet 
				// swap places so aces show first as highest pair
				result = "Two Pair: " + availableCards.get(secondPair).getRank() + "'s and " + availableCards.get(firstPair).getRank() + "'s";
			}
			else 
			{
				result = "Two Pair: " + availableCards.get(firstPair).getRank() + "'s and " + availableCards.get(secondPair).getRank() + "'s";
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
