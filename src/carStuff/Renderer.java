package carStuff;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class Renderer {
	public static final int TILE_SIZE = 100;

	public static boolean FULLSCREEN = false;

	public static int WINDOW_WIDTH = Display.getDesktopDisplayMode().getWidth();
	public static int WINDOW_HEIGHT = Display.getDesktopDisplayMode().getHeight();
	
	public static final int WINDOW_WIDTH_WINDOWED = 800;
	public static final int WINDOW_HEIGHT_WINDOWED = 600;
	
	private boolean running; 
	
	private Map map;
	private Car car;
	
	public Renderer(Map map, Car car)
	{
		this.map = map;
		this.car = car;
	}
	
	
	public void start()
	{
		init();
		running = true;
		
		while(!Display.isCloseRequested() && running)
		{
			renderMap();
			Display.update();
			Display.sync(120);
		}
		Display.destroy();
	}
	
	
	public void renderCar()
	{
		
		Texture texture = TextureStore.getTexture("res/car.png");
		Vector2d offset = car.pos;
		int carSize = 10;
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0.0f, 0.0f);	//Upper Left
		GL11.glVertex2d(offset.x, offset.y + carSize);	
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2d(offset.x + carSize, offset.y + carSize);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2d(offset.x + carSize, offset.y);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2d(offset.x, offset.y);

		GL11.glEnd();
		
		GL11.glColor4f(1, 1, 1, 1);
		car.renderSightLines();
	}
	
	public void renderMap()
	{
		Texture pathTile = TextureStore.getTexture("res/tiles/path.png");
		Texture backTile = TextureStore.getTexture("res/tiles/background.png");
		
		boolean[][] track = map.getTrack();
		for(int x=0; x<track.length; x++)
		{
			for(int y=0; y<track[0].length; y++)
			{
				int tx = x;// track.length - x - 1;
				int ty = y;// track[0].length - y - 1;
				if(track[x][y])
				{
					renderTile(new Vector2d(tx*TILE_SIZE, ty*TILE_SIZE), pathTile);
				}
				else 
				{
					renderTile(new Vector2d(tx*TILE_SIZE, ty*TILE_SIZE), backTile);
				}
				
			}
		}
		
		renderCar();
		
	}
	
	public void renderTile(Vector2d offset, Texture texture)
	{		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0.0f, 0.0f);	//Upper Left
		GL11.glVertex2d(offset.x, offset.y + TILE_SIZE);	
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2d(offset.x + TILE_SIZE, offset.y + TILE_SIZE);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2d(offset.x + TILE_SIZE, offset.y);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2d(offset.x, offset.y);

		GL11.glEnd();
		
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void init()
	{
		try{
			if(FULLSCREEN == false)
			{
				WINDOW_WIDTH = WINDOW_WIDTH_WINDOWED;
				WINDOW_HEIGHT = WINDOW_HEIGHT_WINDOWED;
				Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
			}
			
			Display.setVSyncEnabled(true);
			Display.setFullscreen(FULLSCREEN);
			Display.create();
		}catch(LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	}
}
