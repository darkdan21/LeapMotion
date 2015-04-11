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

public class Main {
	
	public static String executePost(String targetURL, String urlParameters) {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");

	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  

	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
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
	
	public static ArrayList<Board> parseJSONBoardPacket(String packet) {
		JSONParser parser = new JSONParser();
		ArrayList<Board> boards = new ArrayList<Board>();
		
		try {
			JSONObject boardObj = (JSONObject) parser.parse(packet);
			JSONObject dataObj = (JSONObject) boardObj.get("data");
			JSONArray handsArr = (JSONArray) dataObj.get("hands");
			for (int i =0; i<handsArr.size(); i++) {
				JSONObject hand = (JSONObject) handsArr.get(i);
				System.out.println(hand);
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
		
		return boards;
	}
	
	public static void main(String[] args) {
		InitialPacket init = new InitialPacket("game1", "patrick");
//		JSONObject gameObj = new JSONObject();
//		
//		gameObj.put("GameID", "game1");
//		gameObj.put("UserID", "patrick");
//		
		String request = "data="+init.serialized();
		String response = executePost("http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/initialiseGame.php", request);
		//System.out.println(init.serialized());
		System.out.println(response);
		ArrayList<Board> boards = parseJSONBoardPacket(response);
		
		System.out.print(boards);

	}

}
