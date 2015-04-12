package gui;

import game.Board;
import game.Card;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Gesture.Type;

import leap.Sample;

public class MainGUIController implements Renderer3D{

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
	
	public MainGUIController( ArrayList<Board> boards ) {
		// TODO Auto-generated constructor stub
		this.boards = boards;
	}
	
	@Override
	public void init() {
		// Load tex
		cards = Application3D.getApp().getResources().loadTexture("res/sprites/classic-playing-cards.png", "cards");
		Application3D.getApp().getAudioUtils().loadSound("res/Weapon.wav", "Gunshot");
		
		gJoker = Application3D.getApp().getResources().loadTexture("res/sprites/gJoker.png", "gJoker");
		Joker = Application3D.getApp().getResources().loadTexture("res/sprites/Joker.png", "Joker");
		
		crosshair = Application3D.getApp().getResources().loadTexture("res/sprites/crosshair.png", "crosshair" );
		
	}

	@Override
	public void update() {
		// Game logic
		
		// Update leap
		updateLeap();
		
	}

	@Override
	public void render3D() {
		
	}

	@Override
	public void render2D() {
		// render board
		renderBoard();
				
		//tween += 0.12;
		if ( !draw ) return;
		Application3D.getApp()
					 .getRenderUtils()
					 .drawString(
					    ChatColor.RED + "Testing" + ChatColor.GREEN + " things" + ChatColor.YELLOW + " out!",
					    50,
					    50, 
					    FontSize.LARGE,
					    1.0f
					 );
		
		Application3D.getApp()
					 .getRenderUtils()
					 .drawString(
					    (fired)?"fired!":"not fired!",
					    50,
					    75, 
					    FontSize.LARGE,
					    1.0f
					 );
		
		Application3D.getApp()
		 .getRenderUtils()
		 .drawString(
		    String.format( "%20f", prevDist ),
			50,
		    100, 
		    FontSize.LARGE,
		    1.0f
		 );
	
		// render crosshair
		crosshair_rot += 0.05f;
		Application3D.getApp()
					.getRenderUtils()
					.drawSpriteExt(x, y, crosshair, 85, 85, 1, 1, new GColour(1, 1, 1, 1));
		Application3D.getApp()
					.getRenderUtils()
					.drawSpriteExt(x, y, crosshair, 85, 85, 0.5f, 0.5f, new GColour(1, 1, 1, 1));
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
	            Vector position  = hand.fingers().fingerType( Finger.Type.TYPE_INDEX).get(0).tipPosition();
	            
	            if( hand.grabStrength() < 1) { draw = true; }
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
	            x += (xDET - x)*0.35;
	            y += (yDET - y)*0.35;
	            
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
		Board board = boards.get(currentRound+1);
		
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
		
		for( int cy = 0; cy < board.getBoardSize(); cy ++ ) {
			xx = leftX;
			for( int cx = 0; cx < board.getBoardSize(); cx ++ ) {
				
				// Get card info
				Card card 		= board.getCard( cx, cy );
				int row   		= convertSuitToImageRow( card.suit );
				int col   		= card.value.intValue()-1;
				boolean shot 	= board.getCardShot( cx, cy );
				
				// Draw card
				if ( row >= 0 && row < 4 ) { // NORMAL CARD
					// Check for fire hover
					if ( fireX >= xx && fireX <= xx+scaledCardWidth ) {
						if ( fireY >= yy && fireY <= yy+scaledCardHeight ) {
							if ( !shot ) {
								board.shoot( cx, cy );
								shot = true;
							}
						}
					}
					
					Application3D.getApp().getRenderUtils().drawSpritePartExt(xx, yy, (cardWidth+1)*col, (cardHeight+1)*row, cardWidth, cardHeight, cards, 0, 0, (float)scaledCardWidth/(float)cardWidth, (float)scaledCardHeight/(float)cardHeight, new GColour(1, 1, 1, shot?0.25f:1));
				} else if ( row == 4 ) { // COLOURED JOKER
					Application3D.getApp().getRenderUtils().drawSpriteExt(xx, yy, Joker, 0,0, (float)scaledCardWidth/(float)cardWidth, (float)scaledCardHeight/(float)cardHeight, new GColour(1, 1, 1, shot?0.25f:1));
				} else if ( row == 5 ) { // GRAY JOKE
					Application3D.getApp().getRenderUtils().drawSpriteExt(xx, yy, gJoker, 0,0, (float)scaledCardWidth/(float)cardWidth, (float)scaledCardHeight/(float)cardHeight, new GColour(1, 1, 1, shot?0.25f:1));
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

}
