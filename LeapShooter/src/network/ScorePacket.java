package network;

import org.json.simple.JSONObject;

public class ScorePacket {
	public JSONObject jsonString = new JSONObject();
	
	public int score;
	public String gameID;
	public Long userID;
	
	public ScorePacket(int score, String gID, Long pID) {
				
		jsonString.put("GameID", gID);
		jsonString.put("Score", score);
		jsonString.put("UserID", pID);
		
		this.score = score;
		this.gameID = gID;
		this.userID = pID;
	}
	
	public String serialized() {
		return jsonString.toString();
	}
}
