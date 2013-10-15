package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
/**
 * @author Nikhil Khanna
 * Defines the buttons (clickable triggers) in the game
 */
public class Button extends GameSprite 
{
	public static Sound click = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
	public static float clickVol = .5f;
	public static final String filename = "data/button.png";
	public boolean wasTouched = false;
	public boolean lastFrameTouched = false;
	public boolean touchOnMe = false;
	public TextureRegion normalTex;
	public TextureRegion clickedTex;
	public String mynewfilename;
	public Button(String myfilename, String clickedFilename, int x, int y)
	{
		super(AndroidGame.gameTextures.findRegion(myfilename));
		normalTex = AndroidGame.gameTextures.findRegion(myfilename);
		clickedTex = AndroidGame.gameTextures.findRegion(clickedFilename);
		mynewfilename = myfilename;
		setPosition(x, y);
		scaleInit();
	}
	public Button()
	{
		super(AndroidGame.gameTextures.findRegion("Button"));
		normalTex = AndroidGame.gameTextures.findRegion("Button");
		clickedTex = AndroidGame.gameTextures.findRegion("Button_Touch");
		scaleInit();
	}
	public Button(TextureRegion normal, TextureRegion touched, int x, int y)
	{
		super(normal);
		normalTex=normal;
		clickedTex = touched;
		setPosition(x, y);
		scaleInit();
	}
	public Button(String myfilename, String clickedFilename)
	{
		super(AndroidGame.gameTextures.findRegion(myfilename));
		normalTex = AndroidGame.gameTextures.findRegion(myfilename);
		clickedTex = AndroidGame.gameTextures.findRegion(clickedFilename);
		mynewfilename = myfilename;
		scaleInit();
	}
	public Button(int x, int y)
	{
		super(AndroidGame.gameTextures.findRegion("Button"));
		normalTex = AndroidGame.gameTextures.findRegion("Button");
		clickedTex = AndroidGame.gameTextures.findRegion("Button_Touch");
		setPosition(x, y);
		scaleInit();
	}
	public void scaleInit()
	{
		setSize(getWidth()*AndroidGame.widthRatio, getHeight()*AndroidGame.heightRatio);
	}
	public boolean isTouched()
	{
		if(Gdx.input.isTouched())
		{
			lastFrameTouched = true;
			return getBoundingRectangle().contains(Gdx.input.getX(), AndroidGame.h-Gdx.input.getY());
		}
		lastFrameTouched = false;
		touchOnMe = false;
		return false;
	}
	public boolean isPressed()
	{
		if(wasTouched&&!Gdx.input.isTouched()&&touchOnMe)
		{
			click.play(clickVol);
			wasTouched = false;
			return true;
		}
		if(!lastFrameTouched && isTouched())
		{
			touchOnMe = true;
		}
		if(!isTouched())
		{
			setRegion(normalTex);
			touchOnMe = false;
		}
		if(isTouched())
		{
			setRegion(clickedTex);
		}
		wasTouched = isTouched();
		return false;
	}
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
	}
	public boolean isOffScreen()
	{
		return false;
	}
	public void doorUpdate()
	{
		normalTex = AndroidGame.gameTextures.findRegion("Iron_Lrg_Static");
		clickedTex = AndroidGame.gameTextures.findRegion("Iron_Lrg_Static");
		setRegion(normalTex);
		mynewfilename = "Iron_Lrg_Static";
	}
}
