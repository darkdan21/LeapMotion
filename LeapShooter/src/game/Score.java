package game;

public class Score implements Comparable<Score>{
	public Long value;
	public String username;
	
	public Score(Long value, String username) {
		this.value = value;
		this.username = username;
	}
	
	public String toString() {
		return this.username + ": "+value;
	}

	@Override
	public int compareTo(Score s) {
		// TODO Auto-generated method stub
		return this.value > s.value?0:1;
	}
}
