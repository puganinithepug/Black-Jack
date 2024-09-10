import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //storing
import java.util.Random; //shuffling
import javax.swing.*;



public class Blackjack{
	
	
	private class Card {
		
		String value;
		String type;
			
		Card(String value, String type){
				this.value = value;
				this.type = type;
			}
			
		public int getValue() {
				if("AJQK".contains(value)) {
					if(value =="A") {
						return 11;
					}
					
					return 10;
				}
				
				return Integer.parseInt(value);
			}
			
		public boolean isAce() {
			
				return value.equals("A");
				
			}
		
		public String getImagepath() {
			return "./cards/" + toString() + ".png";
		}
			
		public String toString() {
				return value + "-" + type;
			}
		}
	
	ArrayList<Card> deck;
	Random random = new Random(); //shuffling
	
	//dealer
	
	Card hidden;
	ArrayList<Card> dealerHand;
	int dealerSum;
	int aceCount;
	
	//player
	ArrayList<Card> playerHand;
	int playerAce;
	int playerSum;
	
	//window
	int boardw = 600;
	int boardh =boardw;
	
	//card, should be a 1.4 ratio to avoid stretch etc
	int cardw = 110;
	int cardh = 154;
	
	
	JFrame frame = new JFrame("Black Jack");
	
JPanel gamePanel = new JPanel() {
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			try {
			
			//draw hidden
			Image hiddencard = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
			if(!stayButton.isEnabled()) {
				hiddencard = new ImageIcon(getClass().getResource(hidden.getImagepath())).getImage();
			}
			g.drawImage(hiddencard, 20, 20, cardw, cardh, null);
			
			//draw dealer hand
			for (int i =0; i<dealerHand.size(); i++) {
				Card card = dealerHand.get(i);
				Image cardIm = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();
				g.drawImage(cardIm, cardw+25 +(cardw +5)*i, 20, cardw, cardh, null);
			}
			
			//draw player
			for (int i =0; i<playerHand.size(); i++) {
				Card card = playerHand.get(i);
				Image cardIm = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();
				g.drawImage(cardIm, 20 +(cardw +5)*i, 320, cardw, cardh, null);
			}
			
			if(!stayButton.isEnabled()) {
				dealerSum = decAceDealer();
				playerSum = decAce();
				System.out.println(dealerSum);
				System.out.println(playerSum);
				
				String msg = "";
				if(playerSum > 21 || (dealerSum<=21 && dealerSum>playerSum)) {
					msg ="Next One is Definitely BlackJack!";
				}
				else if(dealerSum >21 || (playerSum <=21 && dealerSum < playerSum)) {
					msg = "Fortune Magnet!";
				}
				else if(dealerSum == playerSum) {
					msg = "Tied Up";
				}
				
				g.setFont(new Font("Times New Roman", Font.BOLD, 30));
				g.setColor(Color.red);
				g.drawString(msg, 120, 260);
				
			}
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	

		}
	};
	
	JPanel buttonPanel = new JPanel();
	JButton hitButton = new JButton("Hit");
	JButton stayButton = new JButton("Stay");
	
	
	Blackjack(){
		startGame();
		frame.setVisible(true);
		frame.setSize(boardw, boardh);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.setLayout(new BorderLayout());
		gamePanel.setBackground(new Color(13, 92, 34));
		frame.add(gamePanel);
		
		hitButton.setFocusable(false);
		buttonPanel.add(hitButton);
		stayButton.setFocusable(false);
		buttonPanel.add(stayButton);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		hitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				Card card = deck.remove(deck.size()-1);
				playerSum += card.getValue();
				playerAce+= card.isAce()?1:0;
				playerHand.add(card);
				if(decAce() > 21) {
					hitButton.setEnabled(false);
				}
				
				gamePanel.repaint();

			}
		});
		
		stayButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				hitButton.setEnabled(false);
				stayButton.setEnabled(false);
				while(dealerSum <17) {
					Card card = deck.remove(deck.size() -1);
					dealerSum+= card.getValue();
					aceCount+= card.isAce()?1:0;
					dealerHand.add(card);
				}
				gamePanel.repaint();
			}
		});
		
		gamePanel.repaint();

	}
	
	public void startGame(){
		buildDeck();
		shuffleDeck();
		dealerHand = new ArrayList<Card>();
		dealerSum = 0;
		aceCount = 0;
		
		hidden =deck.remove(deck.size() - 1);
		dealerSum += hidden.getValue();
		aceCount += hidden.isAce()?1:0;
		
		Card card = deck.remove(deck.size() -1);
		dealerSum += card.getValue();
		aceCount += card.isAce()?1:0;
		dealerHand.add(card);
		
		System.out.println(hidden);
		System.out.println(dealerHand);
		System.out.println(aceCount);
		System.out.println(dealerSum);

		
		//player
		
		playerHand =new ArrayList<Card>();
		playerSum =0;
		playerAce = 0;
		
		for(int i= 0; i<2; i++) {
			Card card1 =  deck.remove(deck.size()-1);
			playerSum += card1.getValue();
			playerAce += card1.isAce()?1:0;
			playerHand.add(card1);
		}
		
		System.out.println(playerHand);
		System.out.println(playerAce);
		System.out.println(playerSum);
		
	}
 
	
	public void buildDeck() {
		deck = new ArrayList<Card>();
		String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		String[] types = {"C", "D", "H", "S"};
		
		for (int i = 0; i<types.length; i++) {
			for (int j = 0; j<values.length; j++) { 
				Card card = new Card(values[j], types[i]);
				deck.add(card);
			}
		}
		
		System.out.println(deck);
	}
	
	public void shuffleDeck(){
		
		for (int i = 0; i<deck.size(); i++) {
			int j = random.nextInt(deck.size()); //rand int 0 to 51
			Card curCard = deck.get(i);
			Card randCard = deck.get(j);
			deck.set(i, randCard); //swapping out the cur for the rand
			deck.set(j, curCard);
		}
		
		System.out.println(deck);

	}
	
	public int decAce() {
		while(playerSum>21 && playerAce>0) {
			playerSum -=10;
			playerAce -=1;
		}
		return playerSum;
	}
	
	
	public int decAceDealer() {
		while(dealerSum>21 && aceCount>0) {
			dealerSum -=10;
			aceCount -=1;
		}
		return dealerSum;
	}

}
	
