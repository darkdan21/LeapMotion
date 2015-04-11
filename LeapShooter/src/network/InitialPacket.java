package network;

import org.json.simple.JSONObject;

public class InitialPacket {
	public JSONObject jsonString = new JSONObject();
	
	public String playerID;
	public String gameID;
	
	public InitialPacket(String pID, String gID) {
				
		jsonString.put("GameID", gID);
		jsonString.put("UserID", pID);
		
		this.playerID = pID;
		this.gameID = gID;
	}
	
	public String serialized() {
		return jsonString.toString();
	}
}
