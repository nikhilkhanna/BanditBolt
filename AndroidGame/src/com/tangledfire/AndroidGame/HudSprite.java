package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;

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
