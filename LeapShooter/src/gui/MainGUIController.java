package gui;

import game.Board;
import game.Card;

import java.util.ArrayList;

import network.GameClient;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Gesture.Type;

import static org.lwjgl.opengl.GL11.*;
import leap.Sample;

public class MainGUIController implements Renderer3D, GButtonListener{

	// General
	private Texture cards, crosshair, gJoker, Joker;
	private float x=50.0f, y=50.0f;
	private double tween = 1.0;
	private boolean draw = true;
	private boolean fired = false;
	private float  prevDist = 9999;
	private float crosshair_rot = 0.0f;
	
	// Game logic
	private ArrayList<Board> boards;
	private int currentRound = 0;
	private int fireX=-1, fireY=-1;
	private int scoreValue = 0;
	private float scoreValueScroll = 0.0f;
	private boolean gameOver = false;
	private int roundTimerMax = 20*60;
	private int roundTimer    = roundTimerMax;
	
	boolean showGameOver= false;
	
	private GButton submitButton;
	
	// Visuals
	private float animationTween = 0.0f; // 0.0 to 1.0 if fadeIn = 1, 1.0 to 2.0 otherwise
	private int fadeIn = 0;
	
	public MainGUIController( ArrayList<Board> boards ) {
		// TODO Auto-generated constructor stub
		this.boards = boards;
	}
	
	public static boolean isFist(Hand hand) {
		int count = 0;
		for (Finger finger : hand.fingers()) {
			if (finger.isExtended())
				count++;
		}		
		return count == 0;
	}
	
	@Override
	public void init() {
		// Load tex
		cards = Application3D.getApp().getResources().loadTexture("res/sprites/classic-playing-cards.png", "cards");
		Application3D.getApp().getAudioUtils().loadSound("res/Weapon.wav", "Gunshot");
		
		crosshair = Application3D.getApp().getResources().loadTexture("res/sprites/crosshair.png", "crosshair" );
		
		gJoker = Application3D.getApp().getResources().loadTexture("res/sprites/gJoker.png", "gJoker");
		Joker = Application3D.getApp().getResources().loadTexture("res/sprites/Joker.png", "Joker");
		
		submitButton = new GButton(650, 500, 140, 60);
		submitButton.addListener(this);
	}

	@Override
	public void update() {
		// Game logic
		if ( gameOver && showGameOver ) {submitButton.update(); return; }
		if ( boards.get(currentRound).isGameOver() || roundTimer <= 0 ){
			fadeIn = 1;
			System.out.println("WINNING");
			// REMOVE BULLETHOLES
			Application3D.getApp().destructAll( bullethole.class );
			
			if ( fadeIn == 1 && animationTween >= 2 ){
				// move onto next round or end
				if ( currentRound < boards.size()-1 ) {
					
					// NEW ROUND
					currentRound ++;
					roundTimer    = roundTimerMax;
					fadeIn = 0;
					animationTween = 0;
					
					
				} else {
					gameOver = true;
					currentRound = 0;
					showGameOver = true; 
				}
			}
		} else {
			if ( animationTween >= 1 && draw) {
				roundTimer --;
			}
		}
		
		
		// Update leap
		updateLeap();
		
	}

	@Override
	public void render3D() {
		//Application3D.getApp().getRenderUtils().drawQuad(0, 0, -512, 512);
	}

	@Override
	public void render2D() {
		// render board
		if ( gameOver && showGameOver ) {
			Application3D.getApp()
			 .getRenderUtils()
			 .setTextAlign(GLFont.FA_CENTER);
			Application3D.getApp()
			 .getRenderUtils()
			 .drawString(
			    ChatColor.RED + "GAME OVER!",
			    Display.getWidth()/2,
			    Display.getHeight()/2 - 50, 
			    FontSize.VERY_LARGE,
			    1.0f
			 );
			Application3D.getApp()
			 .getRenderUtils()
			 .drawString(
			    ChatColor.YELLOW + "Your final score is: "+scoreValue,
			    Display.getWidth()/2,
			    Display.getHeight()/2 + 20, 
			    FontSize.VERY_LARGE,
			    1.0f
			 );
			Application3D.getApp()
			 .getRenderUtils()
			 .setTextAlign(GLFont.FA_LEFT);
			
			// Draw button
			
			
			Application3D.getApp()
			 .getRenderUtils()
			 .drawString(
			    ChatColor.GREEN + "Submit score",
			    submitButton.getButtonX(),
			    submitButton.getButtonY(), 
			    FontSize.LARGE,
			    submitButton.getHover()?0.5f:1.0f
			 );
			
			return;
		}
		if ( !draw ) { 
			Application3D.getApp()
			 .getRenderUtils()
			 .setTextAlign(GLFont.FA_CENTER);
			Application3D.getApp()
			 .getRenderUtils()
			 .drawString(
			    ChatColor.RED + "GAME PAUSED",
			    Display.getWidth()/2,
			    Display.getHeight()/2 - 50, 
			    FontSize.VERY_LARGE,
			    1.0f
			 );
			Application3D.getApp()
			 .getRenderUtils()
			 .drawString(
			    ChatColor.YELLOW + "Place open hand above leap motion detector",
			    Display.getWidth()/2,
			    Display.getHeight()/2 + 20, 
			    FontSize.VERY_LARGE,
			    1.0f
			 );
			Application3D.getApp()
			 .getRenderUtils()
			 .setTextAlign(GLFont.FA_LEFT);
			return;
		}
	    
		renderBoard();
		
		/*Application3D.getApp()
					 .getRenderUtils()
					 .drawString(
					    (fired)?"fired!":"not fired!",
					    50,
					    75, 
					    FontSize.LARGE,
					    1.0f
					 );*/
		
		/*Application3D.getApp()
		 .getRenderUtils()
		 .drawString(
		    String.format( "%20f", prevDist ),
			50,
		    100, 
		    FontSize.LARGE,
		    1.0f
		 );*/

		scoreValueScroll += ((float)scoreValue - scoreValueScroll)*0.075f;
		Application3D.getApp()
		 .getRenderUtils()
		 .drawString(
		    ChatColor.WHITE+"Score: "+ChatColor.GREEN+(int)Math.ceil(scoreValueScroll),
			50,
		    50, 
		    FontSize.LARGE,
		    1.0f
		 );
		
		// draw timer
		Application3D.getApp()
		 .getRenderUtils()
		 .drawString(
				 ChatColor.WHITE+"Timer: "+ChatColor.YELLOW+String.format("%.1f", (float)roundTimer/60.0f ),
			50,
		    100, 
		    FontSize.MEDIUM,
		    1.0f
		 );
		
		
	
		// render crosshair
		crosshair_rot += 0.025f;
		Application3D.getApp()
					.getRenderUtils()
					.drawSpriteRotateExt(x, y, crosshair, 89, 87, 0.70f, 0.70f, crosshair_rot, new GColour(1, 1, 1, 1));
		Application3D.getApp()
					.getRenderUtils()
					.drawSpriteRotateExt(x, y, crosshair, 89, 87, 0.45f, 0.45f, -crosshair_rot, new GColour(1, 1, 1, 1));
	}

	@Override
	public void destroy() {
		
	}
	
	
	private void updateLeap(){
		draw = false;
		// Get information
		Frame frame = Sample.getLastFrame();
		
		if ( frame != null ) {
			for(Hand hand : frame.hands()) {

	            String handType  = hand.isLeft() ? "Left hand" : "Right hand";
	            Vector direction = hand.fingers().fingerType( Finger.Type.TYPE_INDEX).get(0).direction();
	            Vector position  = hand.fingers().fingerType( Finger.Type.TYPE_INDEX).get(0).stabilizedTipPosition();
	            
	            if( isFist(hand) ) { draw=false;  return; }else{ draw = true; }
	            // FRUSTUM POSITIONING
	            int distance_to_screen = 1280;
	            int screen_width  = 1280;
	            int screen_height = 720;
	            
	            float biasX, biasY;
	            biasX = 1.0f;
	            biasY = 1.3f;
	            
	            // Calculate basis
	            float xBasis, yBasis;
	            xBasis = screen_width / 2;
	            yBasis = screen_height / 2;
	            
	            System.out.println( "positionX: "+position.getX() + " positionY: "+position.getY() );
	            
	            // Convert position to screen coordinates
	            int DPI = 100;
	            float mmToInches = 0.0393701f;
	            
	            
	            // DEFINE CENTER POSITION IN INCHES relative to device;
	            float xCenter, yCenter;
	            xCenter = 0.0f;
	            yCenter = 3.0f;
	            
	            
	            // < Note: Need to fix to apply to close range frustum, and project onto far plane >
	            float positionPixelOffsetX, positionPixelOffsetY;
	            positionPixelOffsetX = ((mmToInches*position.getX())+xCenter)*DPI;
	            positionPixelOffsetY = ((mmToInches*-position.getY())+yCenter)*DPI;
	            
	            // Scale position down so that the edge of the scan region does not mean edge of the screen,
	            //	rather the same position on screen:
	            //		-- ASSUME BOX REGION IS +/- 6.5 inches left and right
	            //								+/- 3.5 inches up and down
	            float horizontalNearFarScaleFactorX, verticalNearFarScaleFactorY;
	            horizontalNearFarScaleFactorX = 6.5f / (screen_width/DPI);
	            verticalNearFarScaleFactorY   = 3.5f  / (screen_height/DPI);
	            
	            System.out.println("zpos: " + position.getZ());
	            
	            // Adjust distance_to_screen
	            distance_to_screen += position.getZ()*mmToInches*DPI;
	            
	            // Calculate perspective angle:
	            float perspectiveAngleX, perspectiveAngleY;
	            perspectiveAngleY = (float) (2 * Math.atan( screen_width * 0.5f / distance_to_screen));
	            perspectiveAngleX = (float) (2 * Math.atan( screen_height * 0.5f / distance_to_screen));
	            
	            // Calculate pointing position
	            float xDET, yDET;
	            xDET = ( xBasis+positionPixelOffsetX * horizontalNearFarScaleFactorX ) + 
	            		( screen_width/2 )*( direction.getX() / (perspectiveAngleX/2.0f)  );
	            yDET = ( yBasis+positionPixelOffsetY * verticalNearFarScaleFactorY ) + 
	            		( screen_height/2 )*( direction.getY() / (perspectiveAngleY/2.0f) )*-1;
	            
	            // Simple Interpolation
	            float distance =  (float) Math.sqrt( (xDET - x)*(xDET - x)+(yDET - y)*(yDET - y) );
	            //if ( distance < 32 ) {
		            x += (xDET - x)*0.25;
		            y += (yDET - y)*0.25;
	            /*} else if ( distance < 400 ) {
		            x += (xDET - x)*0.175;
		            y += (yDET - y)*0.175;
	            } else {
		            x += (xDET - x)*0.35;
		           */
	            Finger thumb 	   = hand.fingers().fingerType( Finger.Type.TYPE_THUMB).get(0);
	            boolean THUMB_DOWN = Math.toDegrees( thumb.direction().angleTo(direction)) < 35;
	            System.out.println( "DIR: "+Math.toDegrees(thumb.direction().angleTo(direction)) );
	            System.out.println(THUMB_DOWN);
	            
	            if (  THUMB_DOWN ){
		            if( !fired ){
		            	fired = true;
		            	Application3D.getApp().getAudioUtils().playSound( "Gunshot");
		            	fireX = (int) x;
		            	fireY = (int) y;
		            	Application3D.getApp().createBulletHole(fireX, fireY);
		            }
	            } else {
	            	fired = false;
	            }
            
	            
	            
	            // Limit to data from one hand to avoid misk errors
	            break;
			}
		}
	}
	
	
	private void renderBoard(){
		Board board = boards.get(currentRound);
		
		// BOARD REGION ////////////
		int cardWidth 	= 72;
		int cardHeight 	= 97;
		int paddingX	= 16;
		int paddingY	= 16;
		int maxHeight 	= (int)Display.getHeight()-100;
		int maxWidth 	= (int)((float)maxHeight*((float)cardWidth/(float)cardHeight))+(paddingX*board.getBoardSize());
		int leftX		= (Display.getWidth()-maxWidth)/2;
		int topY		= (Display.getHeight()-maxHeight)/2;
		int scaledCardHeight = (maxHeight-(paddingY*board.getBoardSize()))/board.getBoardSize();
		int scaledCardWidth  = (int) (scaledCardHeight*((float)cardWidth/(float)cardHeight)); // ensures aspect ratio is maintained
		
		
		System.out.println("Area dimensions: "+maxWidth+" "+maxHeight);
		System.out.println("Card size: "+scaledCardWidth+" "+scaledCardHeight);
		////////////////////////////
		
		// Drawing
		int xx, yy;
		xx = leftX;
		yy = topY;
		
		if ( fadeIn == 0 ){
			if ( animationTween < 1 ) {
				animationTween += 0.010f;
			}
		} else if ( fadeIn == 1 ){
			if ( animationTween>=1 && animationTween < 2 ) {
				animationTween += 0.010f;
			} else { 
				animationTween = 0;
			}
			
		}
		
		float alpha = 1.0f;
		if ( animationTween < 0.65f ) {
			alpha = 0.0f;
		} else
		if ( animationTween >= 0.65f && animationTween < 0.90f ) {
			alpha = (animationTween-0.65f)/(0.90f-0.65f);
		}else
		if( animationTween > 1.75f ) {
			alpha = (2-animationTween)/0.25f;
		}
		
		for( int cy = 0; cy < board.getBoardSize(); cy ++ ) {
			xx = leftX;
			for( int cx = 0; cx < board.getBoardSize(); cx ++ ) {
				
				// Get card info
				Card card 		= board.getCard( cx, cy );
				int row   		= convertSuitToImageRow( card.suit );
				int col   		= card.value.intValue()-1;
				boolean shot 	= board.getCardShot( cx, cy );
				
				int drawX = xx, drawY = yy;
				if ( fadeIn == 0 ) {
					drawX = (int) (xx*animationTween);
					drawY = (int) (yy*Math.pow(animationTween, 3.5));
				} else if ( fadeIn == 1 ) {
					drawX = (int) (Display.getWidth()-((Display.getWidth()-xx)*(1-(animationTween-1))));
					drawY = (int) (yy*(2-animationTween));
				}
				
				// Check for fire hover
				if ( fireX >= xx && fireX <= xx+scaledCardWidth ) {
					if ( fireY >= yy && fireY <= yy+scaledCardHeight ) {
						if ( !shot ) {
							scoreValue += board.shoot( cx, cy );
							shot = true;
						}
					}
				}
				
				
				// Draw card
				if ( row >= 0 && row < 4 ) { // NORMAL CARD

					Application3D.getApp().getRenderUtils().drawSpritePartExt(drawX, drawY, (cardWidth+1)*col, (cardHeight+1)*row, cardWidth, cardHeight, cards, 0, 0, (float)scaledCardWidth/(float)cardWidth, (float)scaledCardHeight/(float)cardHeight, new GColour(1, 1, 1, alpha*(shot?0.25f:1)));
					
				} else if ( row == 4 ) { // COLOURED JOKER
					Application3D.getApp().getRenderUtils().drawSpriteExt(drawX, drawY, Joker, 0,0, (float)scaledCardWidth/(float)cardWidth, (float)scaledCardHeight/(float)cardHeight, new GColour(1, 1, 1, alpha*(shot?0.25f:1)));
				} else if ( row == 5 ) { // GRAY JOKE
					Application3D.getApp().getRenderUtils().drawSpriteExt(drawX, drawY, gJoker, 0,0, (float)scaledCardWidth/(float)cardWidth, (float)scaledCardHeight/(float)cardHeight, new GColour(1, 1, 1, alpha*(shot?0.25f:1)));
				}
				
				xx += scaledCardWidth + paddingX;
			}
			yy += scaledCardHeight + paddingY;
		}
		
		// reset fire:
		fireX = -1;
        fireY = -1;
	}
	
	private int convertSuitToImageRow( Card.Suit suit ){
		
		switch( suit ) {
			case CLUBS: 		return 0;
			case DIAMONDS:		return 3;
			case HEARTS:		return 2;
			case JOKER_COLORED:	return 4;
			case JOKER_GREY:	return 5;
			case NONE:			return -1;
			case SPADES:		return 1;
		}
		return -1;
	}

	@Override
	public void onGButtonClick(GButton button) {
		// TODO Auto-generated method stub
		if ( button == submitButton ) {
			/// Submit action here
			System.out.println("score submitted");
			GameClient gc = Sample.getGameClient();
			Application3D.getApp().registerRenderInstance( new GUIHighscores(gc.username, gc.gameName, scoreValue));
			showGameOver = false;
		}
	}

	@Override
	public void onGButtonRClick(GButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGButtonHover(GButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGButtonDown(GButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGButtonMouseEnter(GButton button) {
		// TODO Auto-generated method stub
		
	}

}
