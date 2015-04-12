package network;

import org.json.simple.JSONObject;

public class ScorePacket {
	public JSONObject jsonString = new JSONObject();
	
	public int score;
	public String gameID;
	public String username;
	
	public ScorePacket(int score, String gID, String pID) {
				
		jsonString.put("GameID", gID);
		jsonString.put("Score", score);
		jsonString.put("UserID", pID);
		
		this.score = score;
		this.gameID = gID;
		this.username = pID;
	}
	
	public String serialized() {
		return jsonString.toString();
	}
}
