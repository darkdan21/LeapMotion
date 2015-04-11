package network;

import org.json.simple.JSONObject;

public class InitialPacket {
	public JSONObject jsonString = new JSONObject();
	
	public String playerID = "testPlayer";
	public String gameID = "testGame";
	
	public InitialPacket(String pID, String gID) {
				
		jsonString.put("gameID", gID);
		jsonString.put("playerID", pID);
		
		this.playerID = pID;
		this.gameID = gID;
	}
	
	public String serialized() {
		return jsonString.toString();
	}
}
