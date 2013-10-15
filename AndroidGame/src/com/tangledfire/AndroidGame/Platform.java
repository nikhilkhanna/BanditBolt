package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Platform extends GameSprite
{
	public static final String filename = "data/platform.png";
	public static final String unplaceablefilename = "data/unplaceableplatform.png";
	public static Texture tex = new Texture(Gdx.files.internal(filename));
	public static TextureRegion normalTex = AndroidGame.gameTextures.findRegion("platform");
	public static TextureRegion unplaceableTex = AndroidGame.gameTextures.findRegion("unplaceableplatform");
	public boolean isPlaceable = true;
	public Platform()
	{
		super(normalTex);
	}
	public Platform(boolean canBePlaced)//this is usually called when the platform is unplaceable
	{
		super(getTex(canBePlaced));
		if(canBePlaced)
		{
			isPlaceable = true;
		}
		else
		{
			isPlaceable = false;
		}
	}
	public Platform(int x, int y)
	{
		super(normalTex, x,y);
	}
	public static String getFilename()
	{
		return filename;
	}
	public static TextureRegion getTex(boolean canBePlaced)
	{
		if(canBePlaced)
			return normalTex;
		else
			return unplaceableTex;
	}
	public static String getFilename(boolean canBePlaced)
	{
		if(canBePlaced)
		{
			return filename;
		}
		else
		{
			return unplaceablefilename;
		}
	}
}
