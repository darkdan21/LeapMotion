package gui;

import game.Board;

import java.util.ArrayList;

import leap.Sample;
import network.Main;

public class MainGameMenu implements Renderer3D, GButtonListener{

	GButton buttonPlay;
	private boolean hidden = false;
	
	@Override
	public void init() {
		buttonPlay = new GButton( 50, 50, 150, 50 );
		buttonPlay.addListener(this);
	}

	@Override
	public void update() {
		if ( hidden ) return;
		buttonPlay.update();
	}

	@Override
	public void render3D() {
		
	}

	@Override
	public void render2D() {
		if ( hidden ) return;
		Application3D.getApp().getRenderUtils().drawString("Play", buttonPlay.getButtonX(), buttonPlay.getButtonY(), FontSize.VERY_LARGE, buttonPlay.getHover()?0.5f:1);

	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void onGButtonClick(GButton button) {
		if ( hidden ) return;
		if ( button == buttonPlay ) {
			// Do something when play is pressed
			System.out.println("CLICKED!");
			Sample.getGameClient().registerUserAndGetBoard("http://ec2-52-10-80-90.us-west-2.compute.amazonaws.com/Functions/initialiseGame.php");
			ArrayList<Board> boards = Sample.getGameClient().boards;
			MainGUIController guiGame = new MainGUIController( boards );
			Application3D.getApp().registerRenderInstance(guiGame);
			this.hide();
		}
	}

	@Override
	public void onGButtonRClick(GButton button) {
		
	}

	@Override
	public void onGButtonHover(GButton button) {
		
	}

	@Override
	public void onGButtonDown(GButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGButtonMouseEnter(GButton button) {
		// TODO Auto-generated method stub
		
	}
	
	public void hide(){
		hidden = true;
	}
	
	public void show(){
		hidden = false;
	}

}
