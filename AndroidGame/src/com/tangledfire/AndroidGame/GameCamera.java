package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
/**
 * @author Nikhil Khanna
 * The Camera for gameplay (all non-hud sprites are drawn based on its position)
 */
public class GameCamera extends OrthographicCamera
{
	public static final int offScreenMargin = 50;
	public float currentMarginX;
	public float currentMarginY;
	boolean lastFrameTouched = false;
	boolean drifting = false;
	Vector2 lastTouch = Vector2.Zero;
	Vector2 translateAmount = Vector2.Zero;
	Vector3 initialPosition;
	public GameCamera()
	{
		super();
		//testing zoom capabilities
		zoom = AndroidGame.testedDP/AndroidGame.DP;
		if(zoom>1)
		{
			zoom = 1;
		}
		initialPosition = position;
	}
	public void newInitialPosition()
	{
		initialPosition.x = position.x;
		initialPosition.y = position.y;
		initialPosition.z = position.z;
	}
	public void reset(Player player)
	{
		position.x = player.getX();
		position.y = player.getY();
	}
	public void resetToOrigin()
	{
		position.x = AndroidGame.w/2;
		position.y = AndroidGame.h/2;
	}
	public void update(Grid g, Player p) 
	{
		if(!LevelScreen.isRunning)//mobile camera
		{
		if(Gdx.app.getType()==ApplicationType.Desktop)
		{
			if(Gdx.input.isKeyPressed(Input.Keys.UP))
				translate(0, 10);
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
				translate(0, -10);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
				translate(-10,0);
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				translate(10,0);
		}
		if(DraggableSprite.dragged&&Gdx.input.isTouched()&&DraggableSprite.canPan())
		{
			Vector2 currentTouch = new Vector2(Gdx.input.getX(),Helper.simplePointerY());
			final int margin = AndroidGame.h/8;
			final int panSpeed = 10;
			if(currentTouch.x<margin)
			{
				translate(-panSpeed,0);
			}
			if(currentTouch.x>AndroidGame.w-margin)
			{
				translate(panSpeed,0);
			}
			if(currentTouch.y>AndroidGame.h-margin&&!SpriteMenu.isMenuTouched())
			{
				translate(0,panSpeed);
			}
			if(currentTouch.y<margin&&!SpriteMenu.isMenuTouched())
			{
				translate(0,-panSpeed);
			}
		}
		if(Gdx.input.isTouched()&&!DraggableSprite.dragged&&Gdx.input.getX()<AndroidGame.w-SpriteMenu.width)//starting the drag
		{
			if(lastFrameTouched)
			{
				Vector2 newTouch = new Vector2(Gdx.input.getX(),Helper.simplePointerY());
				translateAmount = new Vector2(lastTouch.x-newTouch.x,lastTouch.y-newTouch.y);
				translate(translateAmount);
				lastTouch = newTouch;
			}
			else
			{
				drifting = false;
				lastFrameTouched = true;
				lastTouch = new Vector2(Gdx.input.getX(), Helper.simplePointerY());
			}
		}
		else//logic for tweening (making the camera drift)
		{
			if(lastFrameTouched)//if it was touched last frame
			{
				drifting = true;
				lastFrameTouched = false;
			}
			if(drifting)
			{
				translateAmount.mul(.7f);
				translate(translateAmount);
				if(translateAmount.len()<.001)
				{
					translateAmount = Vector2.Zero;
					drifting = false;
				}
			}
		}
		}
		else//fixed camera on player
		{
			position.x = p.feet.x;
			position.y = p.feet.y;
		}
		if(AndroidGame.w*zoom>g.size.width)
		{
			currentMarginX = AndroidGame.w*zoom-g.size.width + offScreenMargin;
		}
		else
		{
			currentMarginX = offScreenMargin;
		}
		if(AndroidGame.h*zoom>g.size.height)
		{
			currentMarginY = AndroidGame.h*zoom-g.size.height+ offScreenMargin;
		}
		else
		{
			currentMarginY = offScreenMargin;
		}
		if(position.x<g.size.x+(AndroidGame.w/2*zoom)-currentMarginX)//SHIT CAN'T GO OFFSCREEN
			position.x=g.size.x+(AndroidGame.w/2*zoom)-currentMarginX;
		//this is wierd because of hte menu change the right boundary later
		else if(position.x>g.size.x+g.size.width-(AndroidGame.w/2*zoom)+currentMarginX+SpriteMenu.width)
			position.x= g.size.x+g.size.width-(AndroidGame.w/2*zoom)+currentMarginX+SpriteMenu.width;
		if(position.y<g.size.y+(AndroidGame.h/2*zoom)-currentMarginY)
			position.y=g.size.y+(AndroidGame.h/2*zoom)-currentMarginY;
		else if(position.y>g.size.y+g.size.height-(AndroidGame.h/2*zoom)+currentMarginY)
			position.y=g.size.y+g.size.height-(AndroidGame.h/2*zoom)+currentMarginY;
		super.update();
	}
}
