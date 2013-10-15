package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * A spike that kills the player when hit
 */
public class Spike extends GameSprite
{
	public final static String filename = "data/spikes.png";
	public static TextureRegion tex = AndroidGame.gameTextures.findRegion("spikes");
	public Spike()
	{
		super(tex);
	}
	public static String getFilename()
	{
		return filename;
	}
}
