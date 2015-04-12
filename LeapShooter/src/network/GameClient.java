package network;

import game.Board;
import game.Card;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameClient{
	private String serverURL;
	private String userName;
	private Long userID;
	private String gameName;
	private String gameID;
	
	ArrayList<Board> boards;
	
	GameClient(String serverURL, String userName, String gameName) {
		this.serverURL = serverURL;
		this.userName = userName;
		this.gameName = gameName;
	}
	
	public void registerUserAndGetBoard() {
		RegisterPacket init = new RegisterPacket(this.gameName, this.userName);
		
		String request = "data="+init.serialized();
		String response = this.sendPostRequest(request);
		System.out.println(response);
		
		if (response != null) {
			this.setBoards(response); 
			this.setGameAndUserID(response);
			//System.out.print(boards);
		}
	}
		
	private void setGameAndUserID(String packet) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(packet);
			this.gameID = (String) jsonObj.get("GameID");
			this.userID = (Long) jsonObj.get("UserID");
		}catch(ParseException pe) {
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	    }
	}

	private void setBoards(String packet) {
		JSONParser parser = new JSONParser();
		ArrayList<Board> boards = new ArrayList<Board>();
		
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(packet);
			JSONObject dataObj = (JSONObject) jsonObj.get("data");
			JSONArray handsArr = (JSONArray) dataObj.get("hands");
			for (int i =0; i<handsArr.size(); i++) {
				JSONObject hand = (JSONObject) handsArr.get(i);

				ArrayList<Card> cards = new ArrayList<Card>();
				JSONArray cardsArr =  (JSONArray) hand.get("cards");
				for (int j=0; j<cardsArr.size(); j++) {
					
					
					JSONObject card = (JSONObject) cardsArr.get(j);
					Long suit = (Long) card.get("suit");
					Long value = (Long) card.get("number");
					cards.add(new Card(value,suit));
				}
				boards.add(new Board(cards));
			}
			
		} catch(ParseException pe) {
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	    }
		
		this.boards = boards;
	}
	
	public void sendScore(int score) {
		ScorePacket scorePacket = new ScorePacket(score, this.gameID, this.userID);
		String request = "data="+scorePacket.serialized();
		System.out.println("Request: "+request);
		String response = this.sendPostRequest(request);
		System.out.println("Response: "+response);
	}
	
	public String sendPostRequest(String request) {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(this.serverURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");

	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(request.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  

	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (request);
	      wr.flush ();
	      wr.close ();

	      //Get Response    
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {
	   System.out.println(e.getLocalizedMessage());
	      //e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}
}
