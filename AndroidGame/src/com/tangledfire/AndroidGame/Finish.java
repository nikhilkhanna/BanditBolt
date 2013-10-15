package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * The door the player hits to end the game
 */
public class Finish extends GameSprite
{
	public final static String filename = "data/finish.png";
	public static TextureRegion openTex = AndroidGame.gameTextures.findRegion("Wood_Static");
	public static TextureRegion closedTex = AndroidGame.gameTextures.findRegion("Wood_Locked");
	public Animation openAnim;
	public boolean isOpen;
	public boolean unlocked;
	float animationStateTime = 0; 
	public Finish()
	{
		super(openTex);
		currentFrame = openTex;
		unlocked = true;
		openAnim = new Animation(.1f, AndroidGame.gameTextures.findRegions("Wood"));
		isOpen = true;
	}
	public Finish(boolean open)
	{
		super(openTex);
		isOpen = open;
		unlocked = isOpen;
		if(isOpen)
		{
			currentFrame = openTex;
		}
		else
		{
			currentFrame = closedTex;
		}
		openAnim = new Animation(.1f, AndroidGame.gameTextures.findRegions("Wood"));
	}
	public void update()
	{
		if(isOpen&&!unlocked)
		{
			animationStateTime +=Gdx.graphics.getDeltaTime();
			currentFrame = openAnim.getKeyFrame(animationStateTime, false);
			if(openAnim.isAnimationFinished(animationStateTime))
			{
				unlocked = true;
				currentFrame = openTex;
			}
		}
	}
	public void reset()
	{
		isOpen = false;
		unlocked = false;
		animationStateTime = 0;
		currentFrame = closedTex;
	}
	public void draw(SpriteBatch spriteBatch)
	{
		if(!isOffScreen())
		{
		if(currentFrame == null)
			super.draw(spriteBatch);
		else
			spriteBatch.draw(currentFrame, getX(), getY(), currentFrame.getRegionWidth()*1.25f,currentFrame.getRegionHeight()*1.25f);
		}
	}
	public void open()
	{
		isOpen = true;
	}
	public static String getFilename()
	{
		return filename;
	}
	public Rectangle getBoundingRectangle()
	{
		return new Rectangle(getX(),getY(),30,50);
	}
}
