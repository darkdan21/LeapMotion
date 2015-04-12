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
			int value = multiplier;
			switch(board[x][y].value.intValue())
			{
				case 0:
					value *= 10;
					break;
				case 1:
					value *= 5;
					break;
				case 2:
					value *= -2;
					break;
				case 3:
					value *= -1;
					break;
				case 4:
					value *= 0;
					break;
				case 5:
					value *= 0;
					break;
				case 6:
					value *= 1;
					break;
				case 7:
					value *= 1;
					break;
				case 8:
					value *= 1;
					break;
				case 9:
					value *= 1;
					break;
				case 10:
					value *= 1;
					break;
				case 11:
					value *= 2;
					break;
				case 12:
					value *= 2;
					break;
				case 13:
					value *= 4;
					break;
				default:
					value *= 0;
					break;
			}
			return value;
		}
		return 0;
	}
	
	public boolean isGameOver(){
		return ( shootcount == length*length );
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
