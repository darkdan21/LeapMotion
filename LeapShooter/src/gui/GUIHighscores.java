package gui;

import game.Score;
import game.Scores;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.lwjgl.opengl.Display;

import leap.Sample;

public class GUIHighscores implements Renderer3D{

	String username, gamename;
	boolean scoresLoaded = false;
	int counter = 0;
	int score = 0;
	
	public GUIHighscores( String username, String gamename, int score ) {
		// TODO Auto-generated constructor stub
		this.username = username;
		this.gamename = gamename;
		this.score = score;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		if ( counter ==2 ) {
			
			//this.scores = Sample.getGameClient().scores;
			Sample.getGameClient().sendScore(score, "http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/decoder.php");
			scoresLoaded = true;
		
		}
		counter ++;
	}

	@Override
	public void render3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render2D() {
		// TODO Auto-generated method stub
		Application3D.getApp().getRenderUtils().setTextAlign(GLFont.FA_CENTER);
		Application3D.getApp().getRenderUtils().drawString(ChatColor.GREEN+"HIGHSCORES", Display.getWidth()/2, 50, FontSize.VERY_LARGE, 1);
		int yy;
		yy = 150;
		
		if ( !scoresLoaded ) {
			// Draw own score
			Application3D.getApp().getRenderUtils().drawString(ChatColor.BLUE+username+ChatColor.WHITE+" "+score, Display.getWidth()/2, yy, FontSize.MEDIUM, 1);
			yy += 30;
		
	    } else {
			
			for( Score s : Sample.getGameClient().scores.scores ){
				// draw other peoples scores
				Application3D.getApp().getRenderUtils().drawString(ChatColor.BLUE+s.username+ChatColor.WHITE+" "+s.value, Display.getWidth()/2, yy, FontSize.MEDIUM, 1);
				yy += 30;
			}
		}
		
		Application3D.getApp().getRenderUtils().setTextAlign(GLFont.FA_LEFT);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
