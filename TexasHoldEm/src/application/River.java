package application;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.GridPane;

public class River
{
	// public static final int NRCARDS = 5; // 5th card (Flop, Turn, and River cards)	
	
	private ArrayList<Card> riverCards;
	public GridPane pane = new GridPane();
	public ImageIcon card1;
	public ImageIcon card2;
	public ImageIcon card3;
	public ImageIcon card4;
	public ImageIcon card5;
	public ArrayList<ImageIcon> cards = new ArrayList<ImageIcon>();
	
	public SwingNode card1Node = new SwingNode();
	public SwingNode card2Node = new SwingNode();
	public SwingNode card3Node = new SwingNode();
	public SwingNode card4Node = new SwingNode();
	public SwingNode card5Node = new SwingNode();
	public ArrayList<SwingNode> cardNodes = new ArrayList<SwingNode>();

	public JLabel card1Lbl = new JLabel();
	public JLabel card2Lbl = new JLabel();
	public JLabel card3Lbl = new JLabel();
	public JLabel card4Lbl = new JLabel();
	public JLabel card5Lbl = new JLabel();
	
	// Creates the river
	public River()
	{
		riverCards = new ArrayList<Card>();
		cards.add(card1);
		cards.add(card2);
		cards.add(card3);
		cards.add(card4);
		cards.add(card5);
		
		cardNodes.add(card1Node);
		cardNodes.add(card2Node);
		cardNodes.add(card3Node);
		cardNodes.add(card4Node);
		cardNodes.add(card5Node);
		
		card1Node.setContent(card1Lbl);
		card2Node.setContent(card2Lbl);
		card3Node.setContent(card3Lbl);
		card4Node.setContent(card4Lbl);
		card5Node.setContent(card5Lbl);
		
		pane.add(card1Node, 0, 0);
		pane.add(card2Node, 1, 0);
		pane.add(card3Node, 2, 0);
		pane.add(card4Node, 3, 0);
		pane.add(card5Node, 4, 0);
	}
	public River(String tester){
		riverCards = new ArrayList<Card>();
	}
	// Returns the cards in the river
	public ArrayList<Card> getCards()
	{
		return riverCards;
	}
	
	public ArrayList<SwingNode> getCardNodes() {
		return cardNodes;
	}
	
	// Adds a card to the river
	public void addCard(Card card)
	{
		riverCards.add(card);
	}
	
	// Adds a card to the river
	public void addCard(String card)
	{
		System.out.println(card);
		if(card.indexOf(",") != -1) {
			int suit = Integer.parseInt(card.substring(0, card.indexOf(",")));
			int rank = Integer.parseInt(card.substring(card.indexOf(",") + 1));
		
			addCard(new Card(suit, rank));
		}
	}
	
	// Resets river
	public void resetCard()
	{
		riverCards.clear();
	}
	
	public ArrayList<ImageIcon> getCardList() {
		return cards;
	}
	
	public void updateImgs() {
	//	FileInputStream img1;
	//	FileInputStream img2;
	//	FileInputStream img3;
	//	FileInputStream img4;
	//	FileInputStream img5;
	//	try {
		//	img1 = new FileInputStream(riverCards.get(0).getImage(riverCards.get(0).getSuit(), riverCards.get(0).getRank()));
		//	img2 = new FileInputStream(riverCards.get(1).getImage(riverCards.get(1).getSuit(), riverCards.get(1).getRank()));
		//	img3 = new FileInputStream(riverCards.get(2).getImage(riverCards.get(2).getSuit(), riverCards.get(2).getRank()));
		//	img4 = new FileInputStream(riverCards.get(3).getImage(riverCards.get(3).getSuit(), riverCards.get(3).getRank()));
		//	img5 = new FileInputStream(riverCards.get(4).getImage(riverCards.get(4).getSuit(), riverCards.get(4).getRank()));
		
			card1 = new ImageIcon(riverCards.get(0).getImage(riverCards.get(0).getSuit(), riverCards.get(0).getRank()));
			card2 = new ImageIcon(riverCards.get(1).getImage(riverCards.get(1).getSuit(), riverCards.get(1).getRank()));
			card3 = new ImageIcon(riverCards.get(2).getImage(riverCards.get(2).getSuit(), riverCards.get(2).getRank()));
			card4 = new ImageIcon(riverCards.get(3).getImage(riverCards.get(3).getSuit(), riverCards.get(3).getRank()));
			card5 = new ImageIcon(riverCards.get(4).getImage(riverCards.get(4).getSuit(), riverCards.get(4).getRank()));
			
			Image card1Img = card1.getImage();
			Image card1ImgFin = card1Img.getScaledInstance(75, 109, Image.SCALE_SMOOTH);
			card1.setImage(card1ImgFin);
			
			Image card2Img = card2.getImage();
			Image card2ImgFin = card2Img.getScaledInstance(75, 109, Image.SCALE_SMOOTH);
			card2.setImage(card2ImgFin);
			
			Image card3Img = card3.getImage();
			Image card3ImgFin = card3Img.getScaledInstance(75, 109, Image.SCALE_SMOOTH);
			card3.setImage(card3ImgFin);
			
			Image card4Img = card4.getImage();
			Image card4ImgFin = card4Img.getScaledInstance(75, 109, Image.SCALE_SMOOTH);
			card4.setImage(card4ImgFin);
			
			Image card5Img = card5.getImage();
			Image card5ImgFin = card5Img.getScaledInstance(75, 109, Image.SCALE_SMOOTH);
			card5.setImage(card5ImgFin);
			
			card1Lbl.setIcon(card1);
			card2Lbl.setIcon(card2);
			card3Lbl.setIcon(card3);
			card4Lbl.setIcon(card4);
			card5Lbl.setIcon(card5);
		
	/*		card1.setFitWidth(75);
			card1.setPreserveRatio(true);
			card1.setSmooth(true);
			card1.setCache(true);
		
			card2.setFitWidth(75);
			card2.setPreserveRatio(true);
			card2.setSmooth(true);
			card2.setCache(true);
		
			card3.setFitWidth(75);
			card3.setPreserveRatio(true);
			card3.setSmooth(true);
			card3.setCache(true);
		
			card4.setFitWidth(75);
			card4.setPreserveRatio(true);
			card4.setSmooth(true);
			card4.setCache(true);
		
			card5.setFitWidth(75);
			card5.setPreserveRatio(true);
			card5.setSmooth(true);
			card5.setCache(true); */
	//	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}		
	}
	
	public GridPane getPane() {
		return pane;
	}
}
