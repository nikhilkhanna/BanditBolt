package com.tangledfire.AndroidGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 * @author Nikhil Khanna
 *  superclass of all sprites in the game that supports animations and not drawing offscreen
 */
public class GameSprite extends Sprite{
public Vector2 velocity = Vector2.Zero; 
public boolean needsPlatform = false;//if it needs to sit on a platform
public TextureRegion currentFrame;
	public GameSprite(Texture tex,int width, int height, int x, int y)
	{
		super(tex,width,height);
		setX(x);
		setY(y);
	}
	public GameSprite(TextureRegion texReg, int width, int height, int x, int y)
	{
		super(texReg,0,0,width,height);
		setX(x);
		setY(y);
	}
	public GameSprite(TextureRegion texReg)
	{
		super(texReg);
	}
	public GameSprite(TextureRegion texReg, int x, int y)
	{
		super(texReg);
		setX(x);
		setY(y);
	}
	public GameSprite(Texture tex, int x, int y)
	{
		super (tex, tex.getWidth(),tex.getHeight());
		setX(x);
		setY(y);
	}
	public GameSprite(String path,int xPosition,int yPosition)
	{
		super(new Texture(Gdx.files.internal(path)));
		setX(xPosition);
		setY(yPosition);
	}
	public GameSprite(Texture tex)
	{
		super (tex);
	}
	public GameSprite(String path)
	{
		super (new Texture(Gdx.files.internal(path)));
	}
	public void updatePosition()//updates position based on velocity every frame
	{
		translate(velocity.x*Gdx.graphics.getDeltaTime(), velocity.y*Gdx.graphics.getDeltaTime());
	}
	public Rectangle size()
	{
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	public void update()
	{
		updatePosition();
	}
	public boolean isOffScreen()
	{
		if(getX()+getWidth()<0+AndroidGame.camera.position.x-AndroidGame.w/2||getX()>AndroidGame.w+AndroidGame.camera.position.x-AndroidGame.w/2||getY()+getHeight()<0+AndroidGame.camera.position.y-AndroidGame.h/2||getY()>AndroidGame.h+AndroidGame.camera.position.y-AndroidGame.h/2)
		{
			return true;
		}
		return false;
	}
	public boolean isSlightlyOffScreen()
	{
		return getX()<0+AndroidGame.camera.position.x-AndroidGame.w/2*AndroidGame.camera.zoom||getX()+getWidth()>AndroidGame.w*AndroidGame.camera.zoom+AndroidGame.camera.position.x-AndroidGame.w/2*AndroidGame.camera.zoom-SpriteMenu.width*AndroidGame.camera.zoom||getY()<0+AndroidGame.camera.position.y-AndroidGame.h/2*AndroidGame.camera.zoom||getY()+getHeight()>AndroidGame.h*AndroidGame.camera.zoom+AndroidGame.camera.position.y-AndroidGame.h/2*AndroidGame.camera.zoom;
	}
	@Override
	public void draw(SpriteBatch spriteBatch)
	{
		if(!isOffScreen())
		{
		if(currentFrame == null)
			super.draw(spriteBatch);
		else
			spriteBatch.draw(currentFrame, getX(), getY());
		}
	}
	public void offScreenDraw(SpriteBatch spriteBatch)
	{
		if(!isOffScreen())
		{
		if(currentFrame == null)
			super.draw(spriteBatch);
		else
			spriteBatch.draw(currentFrame, getX(), getY());
		}
	}
	public float getScaleWidth()
	{
		return getWidth()*getScaleX();
	}
	public float getScaleHeight()
	{
		return getHeight()*getScaleY();
	}
	public void sizeScale(float scaleFactor)
	{
		setSize(getWidth()*scaleFactor, getHeight()*scaleFactor);
	}
}
