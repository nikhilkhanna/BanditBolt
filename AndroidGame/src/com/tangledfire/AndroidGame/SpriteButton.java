package com.tangledfire.AndroidGame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * The Sprite that is located on the sprite menu.
 * When clicked (if there are enough left) spawns a draggable sprite that the player can place
 */
public class SpriteButton extends GameSprite{
	public static enum spriteType{Platform, Jumppad, TurnLeft, TurnRight, SpeedBoost};
	public static Sound trash = Gdx.audio.newSound(Gdx.files.internal("data/trash.wav"));
	public spriteType myType;
	public float normalX;
	public float normalY;
	public int numLeft;
	public BitmapFont font;
	public static Texture spriteselected = new Texture(Gdx.files.internal("data/spriteselected.png"));
	public static Texture cross = new Texture(Gdx.files.internal("data/cross.png"));
	public boolean selected = false;
	public SpriteButton(spriteType s)
	{
		super(fileName(s));
		numLeft = 100;//defaults to 100 for level creation
		myType = s;
		if(myType==spriteType.TurnRight)
		{
			flip(true, false);//flips if it is supposed to turn right
		}
	}
	public SpriteButton(spriteType s, int amount)
	{
		super(getRegion(s));
		numLeft = amount;
		myType = s;
		if(myType==spriteType.TurnRight)
		{
			flip(true, false);//flips if it is supposed to turn right
		}
		font = new BitmapFont();
		font.scale(.25f);
		font.setColor(Color.YELLOW);
	}
	public static String fileName(spriteType s)
	{
		if(s==spriteType.Platform)
		{
			return Platform.filename;
		}
		else if(s==spriteType.Jumppad)
		{
			return JumpPad.filename;
		}
		else if(s==spriteType.SpeedBoost)
		{
			return SpeedBoost.filename;
		}
		else //if it is Turn Left or turn Right
		{
			return Turn.filename;
		}
	}
	public static TextureRegion getRegion(spriteType s)
	{
		if(s==spriteType.Platform)
		{
			return AndroidGame.gameTextures.findRegion("platform");
		}
		else if(s==spriteType.Jumppad)
		{
			return AndroidGame.gameTextures.findRegion("Tramp_STATIC_LRG");
		}
		else if(s==spriteType.SpeedBoost)
		{
			return AndroidGame.gameTextures.findRegion("Fire_STATIC");
		}
		else //if it is Turn Left or turn Right
		{
			return AndroidGame.gameTextures.findRegion("Left_Arrow_Static");
		}
	}
	public boolean isTouched()
	{
		if(Gdx.input.isTouched())
		{
			Rectangle rect = size();
			return size().contains(Gdx.input.getX(), AndroidGame.h-Gdx.input.getY());
		}
		return false;
	}
	public boolean isCloseTouched()
	{
		if(Gdx.input.isTouched())
		{
			Rectangle rectangle = new Rectangle(size());
			rectangle.x-=25;
			rectangle.width+=50;
			return rectangle.contains(Gdx.input.getX(), AndroidGame.h-Gdx.input.getY());
		}
		return false;
	}
	public boolean isSelected()
	{
		return isCloseTouched()&&numLeft>0;
	}
	public void update(Grid grid)//starts the drag if it is touched
	{
		if(!SpriteMenu.draggablesprite.dragged)//if nothing is being dragged
		{
			if(isSelected())//if i am touched make myself be dragged
			{
				DraggableSprite draggablesprite = new DraggableSprite(myType,getX(),getY(), this);
				SpriteMenu.newDrag(draggablesprite);
			}
		}
	}
	public boolean isOffScreen()
	{
		return false;
	}
	public static spriteType getType(GameSprite sprite)//get type of sprite based on picture of thing
	{
		if(sprite instanceof JumpPad)
		{
			return spriteType.Jumppad;
		}
		else if(sprite instanceof SpeedBoost)
		{
			return spriteType.SpeedBoost;
		}
		else if(sprite instanceof Turn)
		{
			if(((Turn) sprite).left)
			{
				return spriteType.TurnLeft;
			}
			else
			{
				return spriteType.TurnRight;
			}
		}
		else 
		{
			return spriteType.Platform;
		}
	}
	public static GameSprite getSprite(spriteType type)
	{

		if(type==spriteType.TurnLeft)
		{
			return new Turn(true);
		}
		else if(type==spriteType.TurnRight)
		{
			return new Turn(false);
		}
		else if(type == spriteType.SpeedBoost)
		{
			return new SpeedBoost();
		}
		else
		{
			return new JumpPad();
		}

	}
	public void decrementAmount()
	{
		numLeft--;
	}
	public void incrementAmount()
	{
		numLeft++;
		trash.play();
	}
	public void draw(SpriteBatch spriteBatch)
	{
		super.draw(spriteBatch);
		if(!AndroidGame.levelEditingMode)
		{
			font.draw(spriteBatch, ""+numLeft, AndroidGame.w-SpriteMenu.width+5, getY()+font.getBounds(""+numLeft).height+5);
		}
		if(numLeft==0)
		{
			spriteBatch.draw(cross, AndroidGame.w-SpriteMenu.width, getY()+getHeight()/2-cross.getHeight()/2);
		}
	}
	public void drawSelected(SpriteBatch spriteBatch)
	{
		spriteBatch.draw(spriteselected, getX(), getY());
	}
}
