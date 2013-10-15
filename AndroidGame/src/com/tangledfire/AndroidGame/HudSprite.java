package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
/**
 * @author Nikhil Khanna
 * A sprite that is located on the hud(does not transalte with the camera)
 */
public class HudSprite extends GameSprite
{
	public HudSprite(String path)
	{
		super(path);
	}
	public HudSprite(String path, float x, float y)
	{
		super(path);
		setPosition(x,y);
	}
	@Override
	public boolean isOffScreen()
	{
		return false;
	}
	public boolean isTouched()
	{
		if(Gdx.input.isTouched())
		{
			return size().contains(Gdx.input.getX(), AndroidGame.h-Gdx.input.getY());
		}
		return false;
	}
}
