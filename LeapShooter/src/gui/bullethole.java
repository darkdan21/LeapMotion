package gui;

import static org.lwjgl.opengl.GL11.*;

public class bullethole extends FactoryInstance3D{

	int x;
	int y;
	Texture bullethole;
	boolean delete = false;
	float alpha = 1;
	
	public bullethole( int x, int y ) {
		super();
		this.x = x;
		this.y = y;
		
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		bullethole = Application3D.getApp().getResources().getTexture("bulletholePaper");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if ( delete ) {
			alpha -= 0.05f;
			if ( alpha <= 0 ) {
				this.destruct();
			}
		}
	}

	@Override
	public void render3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render2D() {
		// TODO Auto-generated method stub
		//glBlendFunc(GL_ZERO, GL_DST_ALPHA);

		Application3D.getApp().getRenderUtils().drawSprite(x-21, y-23, bullethole, 1, 1, 1, alpha);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBegin() {
		// TODO Auto-generated method stub
		delete = true;
	}

}
