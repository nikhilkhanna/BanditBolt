package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * The trampoline a player can bounce on
 */
public class JumpPad extends GameSprite{
	public final static String filename = "data/jumppad.png";
	public static TextureRegion tex = AndroidGame.gameTextures.findRegion("Tram_STATIC");
	public Animation myAnim;
	boolean animating = false;
	float animationStateTime = 0; 
	public JumpPad()
	{
		super(tex);
		myAnim = new Animation(.125f, AndroidGame.gameTextures.findRegions("Tram"));
		animationStateTime = 0;
		animating = false;
		needsPlatform = true;
	}
	public void update()
	{
		if(animating)
		{
			animationStateTime+=Gdx.graphics.getDeltaTime();
			currentFrame = myAnim.getKeyFrame(animationStateTime, false);
			if(myAnim.isAnimationFinished(animationStateTime))
			{
				animationStateTime = 0;
				animating = false;
				currentFrame = tex;
			}
		}
	}
	public void startAnimation()
	{
		animating = true;
	}
	public static String getFilename()
	{
		return filename;
	}
	public Rectangle getBoundingRectangle()
	{
		return new Rectangle(getX()+getWidth()/4, getY(), getWidth()/2, getHeight()/2);
	}
}
