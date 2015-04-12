package network;

public class Main {
	
	public static void main(String[] args) {
		
		GameClient client = new GameClient(
				"patrick6", 
				"game4");
		client.registerUserAndGetBoard("http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/initialiseGame.php");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String scores = client.sendScore(50, "http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/decoder.php");
		System.out.println(scores);
		//System.out.println(init.serialized());
		//System.out.println(response);
		
	}

}
