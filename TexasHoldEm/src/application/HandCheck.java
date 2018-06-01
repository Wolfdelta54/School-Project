package application;

import java.util.ArrayList;
import java.util.Collections; 

public class HandCheck
{
	public ArrayList<Player> players;
	public ArrayList<Card> availableCards; 
	public ArrayList<Card> allCards = new ArrayList<Card>();
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
	

	public void setPlayers(ArrayList<Player> players)
	{
		int size = players.size(); 
		System.out.println(players.size());
		this.players = players; 
		for(int i=0; i<size; i++)
		{
			this.hands.add(players.get(i).getHand()); 
			this.names.add(players.get(i).getName());		
			System.out.println(players.get(i).getHand().getCards().size());
		}

	}
	
	public void setCards(River river)
	{
		availableCards = new ArrayList<Card>();
		if(!availableCards.isEmpty()) {
			availableCards.clear();
		}
		this.availableCards = river.getCards(); 
		System.out.println(availableCards.size());
		//availableCards.addAll(hand.getCards());
	}
	
	public void setList(int index)
	{
		allCards.clear(); 
	//	allCards.addAll(availableCards);
		//allCards.addAll(hands.get(index).getCards()); 
		for(int i =0; i<availableCards.size(); i++)
		{
			allCards.add(availableCards.get(i));

			System.out.println(availableCards.get(i).toString());
			System.out.println(allCards.toString());
		}
		
		for(int i=0; i<hands.get(index).getCards().size(); i++)
		{
			allCards.add(hands.get(index).getCards().get(i));
			System.out.println(hands.get(index).getCards().get(i).toString());
			System.out.println(allCards.toString());
		}
		
	}
	
	public ArrayList<Card> getList(int ind) {
		ArrayList<Card> temp = new ArrayList<Card>();
		
		for(int i = 0; i < 5; i++) {
			temp.add(availableCards.get(i));
		}
		
		for(int i = 0;i < 2; i++) {
			temp.add(hands.get(ind).getCards().get(i));
		}
		
		return temp;
	}
	
	public ArrayList<String> checkHands()
	{
		
		for(int i=0; i<players.size(); i++)
		{
		String results = ""; 
	//	ArrayList<Card> list = getList(i);
		setList(i); 
		
		int[] rankCounterArray = new int[15];
        int[] suitCounterArray = new int[5];

        // initializations
        for (int j=0;j<rankCounterArray.length;j++)
        {
            rankCounterArray[j] =0;
        }

        for (int j=0;j<suitCounterArray.length;j++)
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
        results = royalFlush(rankCounter, suitCounter);

        //is straight flush?
        if (results.length() == 0)
        	results = straightFlush(rankCounter, suitCounter); 
        //is four of a kind?
        if (results.length() == 0)
        	results = fourOfAKind(rankCounter);
        //is full house?
        if (results.length() == 0)
        	results = fullHouse(rankCounter);
        //is flush?
        if (results.length() == 0)
        	results = flush(rankCounter, suitCounter);
        //is straight?
        if (results.length() == 0)
        {
        	Collections.sort(rankCounter);
        	// re-sort by rank, up to this point we had sorted by rank and suit
        	// but a straight is suit independent.
        //	results = straight(rankCounter);
        	results = straights(allCards);
        }
        //is three of a kind?
        if (results.length() == 0)
        	results = evaluateThreeOfAKind(rankCounter);

        //is two pair?
        if (results.length() == 0)
        	results = evaluateTwoPair(rankCounter);

        //is one pair?
        if (results.length() == 0)
        	results = evaluateOnePair(rankCounter);


        //is highest hand? 
        if (results.length() == 0)
        	results = evaluateHighCard(rankCounter);
			
        System.out.println(players.get(i).getName() + ";" + results);
        handCombos.add(players.get(i).getName() + ";" + results);
		}
        
        return handCombos; 
	}

	//ORDER OF SUITS = spades, hearts, clubs, diamond
		
	//RoyalFlush - 10JQKA of same suit
	private String royalFlush(ArrayList<Integer> rankCounter, ArrayList<Integer> suitCounter) 
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
	/*private String royalFlush() 
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
		
	}*/

		//StraightFlush - sequential numbers of same suit
	private String straightFlush(ArrayList<Integer> rankCounter, ArrayList<Integer> suitCounter)
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
	        
	/*private String straightFlush()
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
	}*/
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

			for (int i=allCards.size()-1; i>3 ; i--)
			{
				if (allCards.get(i).getSuit() == allCards.get(i-1).getSuit() &&
						allCards.get(i).getSuit() == allCards.get(i-2).getSuit() &&
						allCards.get(i).getSuit() == allCards.get(i-3).getSuit() &&
						allCards.get(i).getSuit() == allCards.get(i-4).getSuit())
				{
					result = "Flush " + allCards.get(2).getSuit();
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

	//Straight - sequential order different suit
private String straights(ArrayList<Card> cards)
{
	String result = "";
	int[] ranks = new int[15];
	for(int i = 0; i < 15; i++) {
		ranks[i] = 0;
	}
	// loop through rank array to check for sequence
	for (int i=0; i<cards.size(); i++)
	{
		ranks[cards.get(i).getRank()]++;
	}
	
	for(int i = 1; i <= 10; i++) {
		if(ranks[i] > 0 &&
			ranks[i+1] > 0 &&
			ranks[i+2] > 0 &&
			ranks[i+3] > 0 &&
			ranks[i+4] > 0) {
			result = "Straight: high " + ranks[i+4];
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
				result = "Two Pair: " + /*allCards.get(secondPair).getRank()*/ secondPair + "'s and " + /*allCards.get(firstPair).getRank()*/ firstPair + "'s";
			}
			else 
			{
				result = "Two Pair: " + /*allCards.get(firstPair).getRank()*/ firstPair + "'s and " + /*allCards.get(secondPair).getRank()*/ secondPair + "'s";
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
