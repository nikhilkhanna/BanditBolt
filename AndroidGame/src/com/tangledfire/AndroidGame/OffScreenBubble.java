package com.tangledfire.AndroidGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class OffScreenBubble extends GameSprite
{	
	boolean isFinish = true;
	public GameSprite contents;
	public GameSprite attatchedItem;
	Vector2 attatchedVector;
	Vector2 cameraVector;
	Vector2 myVector;
	Vector2 verticleScreenVector = new Vector2(0, AndroidGame.h);
	Vector2 horizontalScreenVector = new Vector2(AndroidGame.w-SpriteMenu.width,0);
	boolean intersectsRightSide;
	boolean intersectsLeftSide;
	boolean intersectsTopSide;
	boolean intersectsBottomSide;
	public OffScreenBubble(Key key)
	{
		super(AndroidGame.gameTextures.findRegion("bubble"));
		isFinish = false;
		attatchedItem = key;
		contents = new GameSprite(AndroidGame.gameTextures.findRegion("Key", 1));
		setPosition(-100, -100);
		contents.setPosition(-100, -100);
	}
	public OffScreenBubble(Finish finish)
	{
		super(AndroidGame.gameTextures.findRegion("bubble"));
		isFinish = true;
		attatchedItem = finish;
		if(finish.isOpen)
			contents = new GameSprite(AndroidGame.gameTextures.findRegion("Wood_Static"));
		else
			contents = new GameSprite(AndroidGame.gameTextures.findRegion("Wood_Locked"));
		setPosition(-100, -100);
		contents.setPosition(-100, -100);
	}
	public void update()
	{
		if(attatchedItem.isSlightlyOffScreen()&&!LevelScreen.isRunning)
		{
			 attatchedVector = new Vector2(attatchedItem.getX(), attatchedItem.getY());
			 cameraVector = new Vector2(AndroidGame.camera.position.x, AndroidGame.camera.position.y);
			 attatchedVector = attatchedVector.sub(cameraVector);
			 final int intervals = 500;
			 setPosition(cameraVector.x-getWidth()/2, cameraVector.y-getHeight()/2);
			 for(int i = 0; i<intervals;i++)
			 {
				 if(isSlightlyOffScreen())
				 {
					 break;
				 }
				 translateX(attatchedVector.x/intervals);
				 translateY(attatchedVector.y/intervals);
			 }
			 contents.setPosition(getX()+getWidth()/2-contents.getWidth()/2, getY()+getHeight()/2-contents.getHeight()/2);
		}
		else
		{
			setPosition(-100, -100);
			contents.setPosition(-100, -100);
		}
		//setPosition(AndroidGame.camera.position.x, AndroidGame.camera.position.y);
		//contents.setPosition(getX(), getY());
	}
	public void draw(SpriteBatch spriteBatch)
	{
		if(attatchedItem.isSlightlyOffScreen()&&!LevelScreen.isRunning)
		{
			super.draw(spriteBatch);
			contents.draw(spriteBatch);
		}
	}
}
