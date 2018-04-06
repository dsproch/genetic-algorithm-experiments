package carStuff;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureStore
{
	private static Map<String, Texture> textureMap = new HashMap<String, Texture>();
	
	public static Texture getTexture(String path)
	{
		if(textureMap.containsKey(path))
		{
			return textureMap.get(path);
		}
		else if(new File(path).exists())
		{
			try{
				Texture tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path), GL11.GL_NEAREST);
				textureMap.put(path, tex);
				return tex;
			}catch(IOException e){
				System.out.println("TextureStore::getTexture():: ERROR: Cannot find texture: " + path);
				e.printStackTrace();
			}
		}
		else if(new File("res/images/error.png").exists())
		{
			return TextureStore.getTexture("res/images/error.png");
		}
		return null;
	}
}
