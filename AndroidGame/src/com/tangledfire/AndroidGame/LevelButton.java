package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * @author Nikhil Khanna
 * A button for the level select screen
 */
public class LevelButton extends Button
{
	public static final String filename = "Level";
	public int levelNumber;
	public boolean hasChain = false;
	public GameSprite chain;
	public LevelButton(int levelNumber, int x, int y)
	{

		super(AndroidGame.gameTextures.findRegion("Level",levelNumber),AndroidGame.gameTextures.findRegion("Level_touch",levelNumber),x,y);
		this.levelNumber = levelNumber;
		setSize(getWidth()*2, getHeight()*2);
		if(!isUnlocked())
		{
			hasChain = true;
			chain = new GameSprite(AndroidGame.gameTextures.findRegion("Level_chain"));
			//chain.setPosition(getX()-.1f*getWidth(), getY()-.1f*getHeight());
			//chain.setScale(1.9f);
			chain.setSize(getWidth()*AndroidGame.widthRatio, getHeight()*AndroidGame.heightRatio);
			chain.setSize(getWidth()*1.1f, getWidth()*1.1f);
			chain.setPosition(getX()-(.05f*getWidth()), getY()-(.05f*getHeight()));
		}
	}
	public void update(AndroidGame game)
	{
		if(isPressed())
		{
			game.levelToPlay = levelNumber;
			game.setScreen(game.levelScreen);
		}
	}
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		if(hasChain)
			chain.draw(spriteBatch);
	}
	public boolean isUnlocked()
	{
		if(levelNumber==1||levelNumber==16)
		{
			return true;
		}
		Preferences prefs = Gdx.app.getPreferences(AndroidGame.prefName);
		int previousLevel = levelNumber-1;
		return prefs.getBoolean("level"+previousLevel);
	}
}
