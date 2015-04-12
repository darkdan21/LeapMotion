package network;

public class Main {
	
	public static void main(String[] args) {
		
		GameClient client = new GameClient(
				"patrick7", 
				"game8");
		client.registerUserAndGetBoard("http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/initialiseGame.php");
		
		// Perform calculation HERE
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Finally submit score
		String scores = client.sendScore(60, "http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/decoder.php");
		System.out.println(client.scores);
		
	}

}
