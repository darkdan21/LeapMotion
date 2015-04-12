package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scores {
	public ArrayList<Score> scores;
	
	public void addScore(Score s) {
		scores.add(s);
	}
	
	public Scores(ArrayList<Score> scores) {
		this.scores = scores;
		Collections.sort(this.scores, Collections.reverseOrder());
	}
		
	public ArrayList<String> getPlayernames() {
		ArrayList<String> result = new  ArrayList<String>();
		
		for (Score s : this.scores) {
			result.add(s.username);
		}
		
		return result;
	}
	
	public Score getWinner() {
		Score max = this.scores.get(0);
		for (Score s : this.scores) {
			if (s.value > max.value) {
				max = s;
			}
		}
		return max;
	}
	
	public String toString() {
		String result = "Scores:\n";
		for (Score s : this.scores) {
			result += "\t"+s+"\n";
		}
		return result;		
	}
}
