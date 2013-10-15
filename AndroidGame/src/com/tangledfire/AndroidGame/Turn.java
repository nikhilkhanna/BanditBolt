package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * An arrow that turns the player when hit
 */
public class Turn extends GameSprite
{
	public static final String filename = "data/turn.png";
	public TextureRegion tex;
	public TextureRegion litTex;
	public static final float timeToDim = .3f;
	public float timer = 0;
	public boolean isLit = false;
	public boolean left;
	public Turn()
	{
		super(AndroidGame.gameTextures.findRegion("Left_Arrow"));
		tex = AndroidGame.gameTextures.findRegion("Left_Arrow");
		litTex = AndroidGame.gameTextures.findRegion("Left_Arrow_Select");
		left = true;
		isLit = false;
	}
	public Turn(boolean isLeft)
	{
		super(AndroidGame.gameTextures.findRegion("Left_Arrow"));
		tex = AndroidGame.gameTextures.findRegion("Left_Arrow");
		litTex = AndroidGame.gameTextures.findRegion("Left_Arrow_Select");
		left = isLeft;
		if(!left)
		{
			flip(true, false);
		}
		isLit = false;
	}
	public void playerHit()
	{
		timer = 0;
		isLit = true;
		setRegion(litTex);
		if(!left)
		{
			flip(true, false);
		}
	}
	public void update()
	{
		if(isLit)
		{
			timer+=Gdx.graphics.getDeltaTime();
			if(timer>timeToDim)
			{
				isLit = false;
				setRegion(tex);
				if(!left)
				{
					flip(true, false);
				}
				timer = 0;
			}
		}
	}
	public static String getFilename()
	{
		return filename;
	}
	public Rectangle getBoundingBox()
	{
		return new Rectangle(getX(), getY(), 30,30);
	}
}