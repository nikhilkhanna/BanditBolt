package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * The fire that speeds up the player when hit
 */
public class SpeedBoost extends GameSprite
{
	public final static String filename = "data/speedboost.png";
	public static TextureRegion tex = AndroidGame.gameTextures.findRegion("Fire",0);
	public Animation myAnim;
	float animationStateTime = 0; 
	public SpeedBoost()
	{
		//super(tex);
		super(tex);
		myAnim = new Animation(.1f, AndroidGame.gameTextures.findRegions("Fire"));
		animationStateTime = 0;
		needsPlatform = true;
	}
	public static String getFilename()
	{
		return filename;
	}
	public void update()
	{
		animationStateTime += Gdx.graphics.getDeltaTime();
		currentFrame = myAnim.getKeyFrame(animationStateTime, true);
	}
	public Rectangle getBoundingRectangle()
	{
		return new Rectangle(getX()+getWidth()/4, getY(), getWidth()/2, getHeight()/2);
	}
}
