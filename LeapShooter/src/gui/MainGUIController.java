package gui;

import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

import leap.Sample;

public class MainGUIController implements Renderer3D{

	private Texture cards;
	private float x=50.0f, y=50.0f;
	private double tween = 1.0;
	
	public static boolean isFist(Hand hand) {
		FingerList fingers = hand.fingers();
		int count = 0;
		for (int i=0; i<fingers.count(); i++) {
			if (fingers.get(i).isExtended())
				count++;
		}
		
		return count == 0;
	}
	
	@Override
	public void init() {
		// Load tex
		cards = Application3D.getApp().getResources().loadTexture("res/sprites/classic-playing-cards.png", "cards");
	}

	@Override
	public void update() {
		// Get information
		Frame frame = Sample.getLastFrame();
		
		if ( frame != null ) {
			for(Hand hand : frame.hands()) {

	            String handType  = hand.isLeft() ? "Left hand" : "Right hand";
	            Vector direction = hand.direction();
	            Vector position  = hand.palmPosition();

	            if (!isFist(hand)) {
		            // FRUSTUM POSITIONING
		            int distance_to_screen = 1400;
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
		            int DPI = 85;
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
		            
		            
		            // Limit to data from one hand to avoid misk errors
		            break;
	            }
			}
		}
		
		if ( x < 0 ) { x = 0; }
		if ( y < 0 ) { y = 0; }
		if ( x > 1280) { x = 1280; }
		if ( y > 720 ) { y = 720; }
		
		// Linear interpolation
	}

	@Override
	public void render3D() {
		
	}

	@Override
	public void render2D() {
		//tween += 0.12;
		
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
		 .drawSpritePartExt(x, y, 0, 0, 72, 98, cards, 36, 49, (float)Math.cos(tween), 1, new GColour(1, 1, 1, 1));
	}

	@Override
	public void destroy() {
		
	}

}
