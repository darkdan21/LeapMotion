package game;

import java.util.ArrayList;

public class Board {
	private int length = 0;
	private Card board[][] = new Card[length][length];
	private Card shotCards[][] = new Card[length][length];
	
	public Board(ArrayList<Card> cards) {
		
		this.length = (int) Math.sqrt(cards.size());
		board= new Card[length][this.length];
		shotCards = new Card[this.length][this.length];
		int counter = 0;
		for (Card c : cards) {
			board[counter/length][counter%length] = c;
			shotCards[counter/length][counter%length] = new Card(0L, -1L);
			counter++;
		}
	}
	
	public void shoot(int x, int y) {
		this.shotCards[x][y] = board[x][y];
	}
	
	public int getScore() {
		int score = 0;
		
		for (int i=0; i<length; i++) {
			for (int j=0; j<length; j++) {
				Card card = shotCards[i][j];
				if (card.suit != Card.Suit.NONE)
					score += card.value;					
			}
		}
		
		return score;
	}
}
