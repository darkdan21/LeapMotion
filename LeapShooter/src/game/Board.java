package game;

import java.util.ArrayList;

public class Board {
	private int length = 0;
	private Card board[][] = new Card[length][length];
	private Card shotCards[][] = new Card[length][length];
	private int shootcount = 0;
	private int multiplier = 1;
	
	public Board(ArrayList<Card> cards, int multiplier) {
		this.multiplier = multiplier;
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
	
	public long shoot(int x, int y) {
		if ( !this.getCardShot(x, y)) {
			this.shotCards[x][y] = board[x][y];
			shootcount ++;
			return multiplier * board[x][y].value;
		}
		return 0;
	}
	
	public boolean isGameOver(){
		return ( shootcount == length*length );
	}
	
	public int getScore() {
		int score = 0;
		
		for (int i=0; i<length; i++) {
			for (int j=0; j<length; j++) {
				Card card = shotCards[i][j];
				if (card.suit != Card.Suit.NONE)
					score += this.multiplier * card.value;					
			}
		}
		
		return score;
	}
	
	public int getBoardSize(){
		return length;
	}
	
	public Card getCard( int x, int y ) {
		return board[x][y];
	}
	
	public boolean getCardShot( int x, int y ){
		return shotCards[x][y].equals( board[x][y] );
	}
}
