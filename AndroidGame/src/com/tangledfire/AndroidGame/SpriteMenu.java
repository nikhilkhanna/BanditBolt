package com.tangledfire.AndroidGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
/**
 * @author Nikhil Khanna
 * The menu that contains the sprite a player can drag to place
 */
public class SpriteMenu extends HudSprite
{
	public ArrayList<SpriteButton> buttons;
	public static final String filename = "data/menu.png";
	public static float width;
	public static DraggableSprite draggablesprite;
	public GameSprite trash;
	public SpriteButton selectedButton;
	public Rectangle myRect;
	public static final Color rectColor = new Color(.38f, .38f, .38f, .75f);
	public SpriteMenu(ArrayList<SpriteButton.spriteType> sprites, PlayButton playButton)
	{
		super(filename);
		buttons = new ArrayList<SpriteButton>();
		for(int i = 0; i<sprites.size();i++)//adding sprite buttons using constructor
		{
			buttons.add(new SpriteButton(sprites.get(i)));
		}
		commonInit(playButton);
	}
	public SpriteMenu(String itemFile, PlayButton playButton)
	{
		super(filename);
		buttons = new ArrayList<SpriteButton>();
		FileHandle levelFile;
		if(Gdx.app.getType()==ApplicationType.Desktop)//determines wthe file path
		{
			levelFile = Gdx.files.external("Levels/"+itemFile);
		}
		else
		{
			levelFile = Gdx.files.internal("Levels/"+itemFile);
		}
		String[] lines = levelFile.readString().split("\n");//splits the lines on line break
		for(int i = 0;i<lines.length;i++)
		{
			String[] pair = lines[i].split(" ");
			buttons.add(new SpriteButton(stringToType(pair[0].trim()),Integer.parseInt(pair[1].trim())));//makes a button based on each line of the file
		}
		commonInit(playButton);
	}
	public void commonInit(PlayButton playButton)
	{
		setPosition(AndroidGame.w-getWidth(), 0);
		trash = new GameSprite(AndroidGame.gameTextures.findRegion("Trash_Can"));
		trash.setPosition(AndroidGame.w-trash.getWidth(), AndroidGame.h/2);
		setScale(3f,2*Gdx.graphics.getHeight()/getHeight());
		ArrayList<GameSprite> menuSprites = new ArrayList<GameSprite>(buttons);
		//menuSprites.add(playButton);
		setPosition(getX(), AndroidGame.h/2);
		width = 100;
		float buttonHeight = menuSprites.get(0).getHeight();
		float yPosition = 0;
		final float playButtonBuffer = playButton.getHeight();
		float yMargin = (AndroidGame.h-playButtonBuffer)/((menuSprites.size())+1)+(buttonHeight/4);
		for(int i = 0; i<menuSprites.size();i++)
		{
			if(i==0)
			{
				yPosition = (AndroidGame.h-playButtonBuffer)-(menuSprites.size())*(yMargin)+buttonHeight/4;
				if(yPosition<0)
					yPosition = 0;
			}
			else
			{
				yPosition += yMargin;
				if(yPosition+buttonHeight>AndroidGame.h-playButtonBuffer)
					yPosition = AndroidGame.h - playButtonBuffer - buttonHeight;
			}
			menuSprites.get(i).setPosition(AndroidGame.w-width/2-menuSprites.get(i).getWidth()/2,yPosition);
		}
		myRect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	public void draw(SpriteBatch spriteBatch, ShapeRenderer render, SpriteBatch dragBatch)
	{
		//super.draw(spriteBatch);
		Gdx.gl.glEnable(GL10.GL_BLEND);
    	Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		render.begin(ShapeType.Filled);
		render.setColor(.1f, .1f, .1f, .9f);
		render.rect(AndroidGame.w-width, 0, width, AndroidGame.h);
		render.end();
		spriteBatch.begin();
		for(int i = 0; i<buttons.size();i++)
		{
			buttons.get(i).draw(spriteBatch);
		}
		if(draggablesprite!=null)
		{
			draggablesprite.draw(spriteBatch);
		}
		spriteBatch.end();
		//trash.draw(spriteBatch);
	}
	public static boolean isMenuTouched()
	{
		if(Gdx.input.isTouched())
		{
			float x = Gdx.input.getX();
			return x>AndroidGame.w-width;
		}
		return false;
	}
	public void update(Grid g)
	{
		for(int i = 0; i<buttons.size();i++)
		{
			buttons.get(i).update(g);
		}
		if(LevelScreen.isRunning&&draggablesprite!=null)//if running while dragging
		{
			stopDrag();
		}
		if(LevelScreen.isRunning)//don't start a drag if running
			return;
		if(draggablesprite!=null)// if drag is currently happening
		{
			draggablesprite.update(g, this);//update this shit
			if(draggablesprite.shouldDelete())
			{
				stopDrag();//stop drag if it hsa been placed
			}
		}
	}
	public static void newDrag(DraggableSprite draggedsprite)
	{
		draggablesprite = draggedsprite;
	}
	public static void stopDrag()
	{
		draggablesprite = null;
		DraggableSprite.dragged= false;
	}
	public static boolean isDragging()
	{
		return draggablesprite !=null;
	}
	public SpriteButton buttonFromType(SpriteButton.spriteType type)
	{
		for(int i = 0; i<buttons.size();i++)
		{
			if(buttons.get(i).myType == type)
			{
				return buttons.get(i);
			}
		}
		return null;
	}
	public boolean inTrash(GameSprite sprite)
	{
		return sprite.getX()+sprite.getWidth()/2>AndroidGame.w-width;
	}
	public SpriteButton.spriteType stringToType(String s)
	{
		if(s.contains("j"))
		{
			return SpriteButton.spriteType.Jumppad;
		}
		else if(s.contains("l"))
		{
			return SpriteButton.spriteType.TurnLeft;
		}
		else if(s.contains("r"))
		{
			return SpriteButton.spriteType.TurnRight;
		}
		else if(s.contains("b"))
		{
			return SpriteButton.spriteType.SpeedBoost;
		}
		return null;
	}
	public boolean isOffScreen()
	{
		return false;
	}
}
