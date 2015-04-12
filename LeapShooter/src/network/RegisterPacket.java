package network;

import org.json.simple.JSONObject;

public class RegisterPacket {
	public JSONObject jsonString = new JSONObject();
	
	public String playerID;
	public String gameID;
	
	public RegisterPacket(String pID, String gID) {
				
		jsonString.put("GameID", gID);
		jsonString.put("Username", pID);
		
		this.playerID = pID;
		this.gameID = gID;
	}
	
	public String serialized() {
		return jsonString.toString();
	}
}
