package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Key extends GameSprite
{
	public static Sound key = Gdx.audio.newSound(Gdx.files.internal("data/keypickup.wav"));
	public static final float keyVol = 1f;
	public final static String filename = "data/key.png";
	public static TextureRegion tex = AndroidGame.gameTextures.findRegion("Key", 0);
	public Animation spinAnim;
	public Animation collectAnim;
	public boolean collected;
	public boolean wasCollected;
	public boolean collectDone = false;
	float animationStateTime = 0; 
	public Key()
	{
		super(tex);
		currentFrame = tex;
		spinAnim = new Animation(.075f, AndroidGame.gameTextures.findRegions("Key"));
		collectAnim = new Animation(.075f, AndroidGame.gameTextures.findRegions("Key_Collect"));
		collected = false;
		wasCollected = false;
	}
	public void update(Player p, Finish f)
	{
		if(!collected)
		{
			animationStateTime+=Gdx.graphics.getDeltaTime();
			currentFrame = spinAnim.getKeyFrame(animationStateTime, true);
		}
		if(collected&&!collectDone)
		{
			animationStateTime+=Gdx.graphics.getDeltaTime();
			currentFrame = collectAnim.getKeyFrame(animationStateTime, false);
			if(collectAnim.isAnimationFinished(animationStateTime))
			{
				collectDone = true;
			}
		}
		if(getBoundingRectangle().overlaps(p.getBoundingRectangle())&&!collected)
		{
			collected = true;
			f.open();
		}
		if(collected&&!wasCollected)
		{
			animationStateTime = 0;
			key.play(keyVol);
			wasCollected = true;
		}
	}
	public void draw(SpriteBatch spriteBatch)
	{
		if(!collectDone)
			super.draw(spriteBatch);
	}
	public void reset(Finish f)
	{
		wasCollected = false;
		collected = false;
		collectDone = false;
		f.reset();
	}
}
