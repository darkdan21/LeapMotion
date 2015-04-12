package gui;

public class bullethole extends FactoryInstance3D{

	int x;
	int y;
	Texture bullethole;
	
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
		
	}

	@Override
	public void render3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render2D() {
		// TODO Auto-generated method stub
		Application3D.getApp().getRenderUtils().drawSprite(x-21, y-23, bullethole, 1, 1, 1, 1);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBegin() {
		// TODO Auto-generated method stub
		
	}

}
