package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ResetButton extends TextButton
{
	public static Sound trash = Gdx.audio.newSound(Gdx.files.internal("data/trash.wav"));
	public static final String filename = "data/reset.png";
	public ResetButton()
	{
		super("Button","Button_Touch", AndroidGame.lang.getString("reset"), 0, 0);
		setPosition(AndroidGame.w-getWidth(), AndroidGame.h-getHeight()*2.3f);
	}
	public boolean isPressed(Level l)
	{
		if(!LevelScreen.isRunning&&super.isPressed()&&!DraggableSprite.dragged)
		{
			l.Reset();
			trash.play();
			return true;
		}
		if(LevelScreen.isRunning&&super.isPressed())
		{
			l.player.reset(l.grid);
			l.Reset();
			trash.play();
			return true;
		}
		return false;
	}
	public void update(Level l)//this only clear user objects
	{
		if(!LevelScreen.isRunning&&isPressed()&&!DraggableSprite.dragged)
		{
			l.Reset();
		}
		if(LevelScreen.isRunning&&isPressed())
		{
			l.player.reset(l.grid);
			l.Reset();
		}
	}
	public void draw(SpriteBatch batch)//only draw if the game is not currently running
	{
		super.draw(batch);
	}
}
