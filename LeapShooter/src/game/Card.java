package game;

public class Card {	
	public int value = 0;
	
	public static enum Suit {DIAMONDS, HEARTS, SPADES, CLUBS, NONE};
	public Suit suit = Suit.NONE;
	
	public Card(int v, Suit s) {
		this.value = v;
		this.suit = s;
	}

	public Card(Card card) {
		this.value = card.value;
		this.suit = card.suit;
	}
}
