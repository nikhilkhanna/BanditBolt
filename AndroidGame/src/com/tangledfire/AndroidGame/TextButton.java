package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public class TextButton extends Button
{
	private BitmapFont buttonText;
	String myText = "";
	float textX = 0;
	float textY = 0;
	public TextButton()
	{
		super();
		commonInit();
	}
	public TextButton(String text)
	{
		super();
		myText = text;
		commonInit();
	}
	public TextButton(String text, float x, float y)
	{
		super();
		setPosition(x, y);
		myText = text;
		commonInit();
	}
	public TextButton(String filename, String clickedFilename, String text, float x, float y)
	{
		super(filename, clickedFilename, (int)x,(int)y);
		myText = text;
		commonInit();
	}
	public TextButton(String filename, String clickedFilename, String text, float x, float y, float textX, float textY)
	{
		super(filename, clickedFilename, (int)x,(int)y);
		myText = text;
		commonInit();
		this.textX = textX;
		this.textY = textY;
	}
	public void commonInit()
	{
		buttonText = new BitmapFont(Gdx.files.internal("data/buttonfont.fnt"), Gdx.files.internal("data/buttonfont_0.tga"), false);
		buttonText.setColor(Color.WHITE);
	}
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		TextBounds bounds = buttonText.getBounds(myText);
		if(normalTex.equals(AndroidGame.gameTextures.findRegion("dooroutline")))
		{
			buttonText.drawWrapped(spriteBatch, myText, getX()+getWidth()/5, getY()+getHeight()/2+bounds.height*2, getWidth()/1.5f);
		}
		else
			buttonText.draw(spriteBatch, myText, getX()+getWidth()/2-bounds.width/2, getY()+getHeight()/2+bounds.height/2);
	}
	public void dispose()
	{
		buttonText.dispose();
	}
}
