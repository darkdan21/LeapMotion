package network;

import game.Board;
import game.Card;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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
	private String registerURL;
	private String scoreURL;
	private String username;
	private String gameName;
	private String gameID;
	
	ArrayList<Board> boards;
	
	GameClient(String userName, String gameName) {
		this.username = userName;
		this.gameName = gameName;
	}
	
	public void registerUserAndGetBoard(String url) {

		this.registerURL = url;
		
		RegisterPacket init = new RegisterPacket(this.username,this.gameName);
		
		String request = "data="+init.serialized();		
		System.out.println("Request: "+request);
		String response = sendPOSTRequest(request, url);
		System.out.println("Response: "+response);
		
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
			this.username = (String) jsonObj.get("UserID");
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
	
	public String sendScore(int score, String url) {
		this.scoreURL = url;
		ScorePacket scorePacket = new ScorePacket(score, this.gameID, this.username);
		String request = "data="+scorePacket.serialized();
		System.out.println("Request: "+request);
		String response = sendPOSTRequest(request, url);
		System.out.println("!Response: "+response);
		if (response.equals(new String("wait"))) {
			System.out.println("Start polling");
			response = waitForResult(url);
		}
		return response;
	}
	
	private String waitForResult(String url) {
		String result = sendGETRequest(url);
		System.out.println(result);
		while (result.equals("error")) {
			System.out.println("No results yet...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			result = sendGETRequest(url);
		}
		return result;
	}
	
	public static String sendGETRequest(String serverURL) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(serverURL);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	   }
	
	public static String sendPOSTRequest(String request, String serverURL) {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(serverURL);
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
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {
	    	System.out.println(e.getLocalizedMessage());
	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}
}
