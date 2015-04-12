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
	
	
	
	
	public static void main(String[] args) {
		
		GameClient client = new GameClient(
				"http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/initialiseGame.php",
				"patrick2", 
				"game2");
		client.registerUserAndGetBoard();
		client.sendScore(50);
		
		//System.out.println(init.serialized());
		//System.out.println(response);
		
	}

}
