package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.tangledfire.AndroidGame.SpriteButton.spriteType;

public class DraggableSprite extends GameSprite{
public static Sound snap = Gdx.audio.newSound(Gdx.files.internal("data/snap.wav"));
public static Sound thud = Gdx.audio.newSound(Gdx.files.internal("data/thud.wav"));
public static Sound pickup = Gdx.audio.newSound(Gdx.files.internal("data/pickup.wav"));
public SpriteButton.spriteType myType;
public static SpriteButton.spriteType typeDragged;
public static boolean dragged = false;
public boolean scaled = true;
public static final float half = .5f;
public SpriteButton button = null;//used to decrement the counter
public GridSquare callersquare = null;//used to reset the sprite if drag is stopped
public static final float panTime = .5f;
public static float timeElapsed = 0;
	public DraggableSprite(SpriteButton.spriteType type, float x, float y)
	{
		super(SpriteButton.getRegion(type),(int)x,(int)y);
		myType = type;
		flipArrows();
		startDrag();
	}
	public DraggableSprite(SpriteButton.spriteType type, float x, float y, SpriteButton button)
	{
		super(SpriteButton.getRegion(type),(int)x,(int)y);
		myType = type;
		this.button = button;
		flipArrows();
		startDrag();
	}
	public DraggableSprite(SpriteButton.spriteType type, float x, float y, GridSquare square)
	{
		super(SpriteButton.getRegion(type),(int)x,(int)y);
		myType = type;
		callersquare = square;
		flipArrows();
		startDrag();
	}
	public void flipArrows()
	{
		if(myType == SpriteButton.spriteType.TurnRight)
		{
			flip(true, false);
		}
	}
	public void startDrag()
	{
		timeElapsed = 0;
		typeDragged = myType;
		dragged = true;
		if(!LevelScreen.isRunning)
			pickup.play();
	}
	public void update(Grid grid, SpriteMenu menu)
	{
		if(dragged)
		{
			timeElapsed +=Gdx.graphics.getDeltaTime();
		if(Gdx.input.isTouched())//set the position if it is still being touched
		{
			GridSquare square;
			if(grid.contains(Helper.PointerX(), Helper.PointerY())&&Gdx.input.getX()<AndroidGame.w-SpriteMenu.width)
			{
			square = closestAvailableSquare(grid, Helper.PointerX(), Helper.PointerY());
			if(shouldShowOutline(square)&&closeEnough(square, Helper.PointerX(), Helper.PointerY()))
			{
				grid.unhighlightAll();
				grid.highlightIntersection(square);
				if(scaled)
				{
					setSize(getWidth()*half*1/AndroidGame.camera.zoom, getHeight()*half*1/AndroidGame.camera.zoom);
					scaled = false;
				}
				float x = getX();
				float y = getY();
				//setPosition to the square its supposed to snap to
				setPosition((square.rect.x+AndroidGame.w/2*AndroidGame.camera.zoom-AndroidGame.camera.position.x)/AndroidGame.camera.zoom, (square.rect.y+AndroidGame.h/2*AndroidGame.camera.zoom-AndroidGame.camera.position.y)/AndroidGame.camera.zoom);
				//if the position has changed (just snapped to the grid) play the thud sound
				if(!Helper.isEpsilonEquals(x, getX())||!Helper.isEpsilonEquals(y, getY()))
				{
					thud.play();
				}
				return;
			}
			}
				//set position to cursor
				setPosition(Gdx.input.getX()-getWidth()/2,AndroidGame.h-Gdx.input.getY()-getHeight()/2);
				if(!scaled)
				{
					setSize(getWidth()*(1/half)*AndroidGame.camera.zoom, getHeight()*(1/half)*AndroidGame.camera.zoom);
					scaled = true;
					grid.unhighlightAll();
				}
		}
		else//if the touch was just released
		{
			grid.unhighlightAll();
			if(menu.inTrash(this)&&callersquare!=null)//if something is in the trash and is being dragged from a square
			{
				menu.buttonFromType(myType).incrementAmount();//increase the item count of hte button type
			}
			else if(grid.contains(Helper.PointerX(),Helper.PointerY())&&Gdx.input.getX()<AndroidGame.w-SpriteMenu.width)
			{
				//GridSquare square = grid.getSquare((int)Helper.PointerX(), (int)Helper.PointerY());
				GridSquare square = closestAvailableSquare(grid, Helper.PointerX(), Helper.PointerY());
				if(closeEnough(square, Helper.PointerX(), Helper.PointerY()))
				{
				if(myType==spriteType.Platform&&!square.hasPlatform())
				{
					decrementAmount();
					square.addUserPlatform();
				}
				else if(shouldShowOutline(square))
				{
					decrementAmount();
					snap.play();
					square.addUserSprite(SpriteButton.getSprite(myType));
				}
				}
				else
				{
					resetSquare();
				}
			}
			else
			{
				resetSquare();
			}
			dragged = false;//stop dragging
		}
		}
	}
	public boolean shouldDelete()
	{
		return !dragged;
	}
	public void decrementAmount()
	{
		if(button!=null)
		{
			button.decrementAmount();
		}
	}
	public void resetSquare()//if a square spawned this, reset it 
	{
		if(callersquare!=null)
		{
			callersquare.addUserSprite(SpriteButton.getSprite(myType));
		}
	}
	//get the closest Square you can place this in (might be null)
	public GridSquare closestAvailableSquare(Grid grid)
	{
		Vector2 myCenter = new Vector2(getX()+getWidth()/2, getY()+getHeight()/2);
		float closestDistance = 9999999;
		GridSquare closestSquare = null;
		for(int i = 0; i<grid.width;i++)
		{
			for(int j = 0; j<grid.height;j++)
			{
				if(shouldShowOutline(grid.squares[i][j]))
				{
					float distance = myCenter.dst(grid.squares[i][j].center());
					if(closestDistance>=distance)
					{
						closestSquare = grid.squares[i][j];
						closestDistance = distance;
					}
				}
			}
		}
		return closestSquare;
	}
	public GridSquare closestAvailableSquare(Grid grid, float x, float y)
	{
		Vector2 myCenter = new Vector2(x,y);
		float closestDistance = 9999999;
		GridSquare closestSquare = null;
		for(int i = 0; i<grid.width;i++)
		{
			for(int j = 0; j<grid.height;j++)
			{
				if(shouldShowOutline(grid.squares[i][j]))
				{
					float distance = myCenter.dst(grid.squares[i][j].center());
					if(closestDistance>=distance)
					{
						closestSquare = grid.squares[i][j];
						closestDistance = distance;
					}
				}
			}
		}
		return closestSquare;
	}
	public boolean closeEnough(GridSquare square, float x, float y)
	{
		final float maxDistance = 50;
		Vector2 myCenter = new Vector2(x,y);
		return myCenter.dst(square.center())<maxDistance;
	}
	public static boolean canPan()
	{
		return timeElapsed>panTime;
	}
	public static boolean shouldShowOutline(GridSquare s)//whether or not an outline should be shown by the grid
	{
		if(dragged)
		{
		if(s.startLocation)
		{
			return false;
		}
		if(typeDragged==null)
		{
			return false;
		}
		if(typeDragged==spriteType.Platform)
		{
			if(s.hasPlatform())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		if(typeDragged == spriteType.Jumppad||typeDragged == spriteType.TurnLeft||typeDragged==spriteType.TurnRight||typeDragged==spriteType.SpeedBoost)
		{
			if(s.hasPlaceablePlatform())
			{
				if(!s.hasSprite())
				{
					return true;
				}
			}
		}
		}
		return false;
	}
	public boolean isOffScreen()
	{
		return false;
	}
}
