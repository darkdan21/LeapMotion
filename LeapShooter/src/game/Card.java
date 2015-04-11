package game;

public class Card {	
	public Long value;
	
	public static enum Suit {DIAMONDS, HEARTS, SPADES, CLUBS, JOKER_GREY, JOKER_COLORED, NONE};
	public Suit suit = Suit.NONE;
	
	public Card(Long v, Long s) {
		this.value = v;
		switch (s.intValue()) {
			case 0: this.suit = Suit.DIAMONDS;
					break;
			case 1: this.suit = Suit.HEARTS;
					break;
			case 2: this.suit = Suit.SPADES;
					break;
			case 3: this.suit = Suit.CLUBS;
					break;
			case 4: this.suit = Suit.JOKER_GREY;
					break;
			case 5: this.suit = Suit.JOKER_COLORED;
					break;
			default: this.suit = Suit.NONE;
		}
	}

	public Card(Card card) {
		this.value = card.value;
		this.suit = card.suit;
	}
}
