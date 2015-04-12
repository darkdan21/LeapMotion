package network;

import game.Board;
import game.Card;
import game.Score;
import game.Scores;

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
	private String username;
	private String gameName;
	private String gameID;
	
	public ArrayList<Board> boards;
	public Scores scores;
	
	public GameClient(String userName, String gameName) {
		this.username = userName;
		this.gameName = gameName;
	}
	
	public void registerUserAndGetBoard(String url) {

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
			int level = 1;
			for (int i =0; i<handsArr.size(); i++) {
				JSONObject hand = (JSONObject) handsArr.get(i);

				ArrayList<Card> cards = new ArrayList<Card>();
				JSONArray cardsArr =  (JSONArray) hand.get("cards");
				for (int j=0; j<cardsArr.size(); j++) {
					
					
					JSONObject cardObj = (JSONObject) cardsArr.get(j);
					Long suit = (Long) cardObj.get("suit");
					Long value = (Long) cardObj.get("number");
					cards.add(new Card(value,suit));
				}
				boards.add(new Board(cards,level++));
			}
			
		} catch(ParseException pe) {
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	    }
		
		this.boards = boards;
	}
	
	// Recursive polling...wtf...well, it's a hackathon :-P
	public String sendScore(int score, String url) {
		ScorePacket scorePacket = new ScorePacket(score, this.gameID, this.username);
		String request = "data="+scorePacket.serialized();
		System.out.println("Request: "+request);
		String response = sendPOSTRequest(request, url);
		System.out.println("Response: "+response);
		
		setScores(response);
		
		while (this.scores == null) {
			System.out.println("Start polling");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			response = sendScore(score, url);
		}
		
		setScores(response);
		
		return response;
	}
	
		
	private void setScores(String packet) {
		if (!packet.equals("wait") && !packet.equals("error")) {
			JSONParser parser = new JSONParser();
			ArrayList<Score> scores = new ArrayList<Score>();
			
			try {
				JSONObject jsonObj = (JSONObject) parser.parse(packet);
				JSONArray scoresArr = (JSONArray) jsonObj.get("Scores");
				for (int i =0; i<scoresArr.size(); i++) {
					JSONObject scoreObj = (JSONObject) scoresArr.get(i);
					Score score = new Score((Long)scoreObj.get("score"), (String)scoreObj.get("UserID"));
					scores.add(score);
				}
				this.scores = new Scores(scores);
			} catch(ParseException pe) {
		         System.out.println("position: " + pe.getPosition());
		         System.out.println(pe);
		    }
		}
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
