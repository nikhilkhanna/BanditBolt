package com.tangledfire.AndroidGame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Level
{
	public static final boolean levelEditingMode = AndroidGame.levelEditingMode;
	public static boolean isRunning;
	public String levelFileName;
	public String listFileName;
	public int levelNum;//if i ever simply want to pass in an int
	public Grid grid;
	public Player player;
	private SpriteMenu menu;
	private PlayButton playButton;
	private GameSprite makeButton; 
	private BitmapFont tutorialRender;
	private String tutorialText;
	private Background background;
	private OffScreenBubble doorBubble;
	private OffScreenBubble keyBubble;
	boolean hasKey = false;
	//called during regular gameplay
	public Level(int levelNumber)
	{	
		levelNum = levelNumber;
		levelFileName = "level"+levelNum+".txt";
		listFileName = "level"+levelNum+"items.txt";
		makeCommonObjects();
		grid = new Grid(levelFileName);
		GridSquare startSquare = grid.startLocation();
		player = new Player((int)startSquare.rect.x,(int)startSquare.platform.getBoundingRectangle().y+3);
		AndroidGame.camera.position.x = player.getX();
		AndroidGame.camera.position.y = player.getY();
		background = new Background(levelNumber, grid);
		doorBubble = new OffScreenBubble(grid.getFinish());
		if(grid.getKey()!=null)
		{
			hasKey = true;
			keyBubble = new OffScreenBubble(grid.getKey());
		}
		player.reset(grid);
	}
	//called during level editing mode when the level already exists
	public Level(String levelToEdit)
	{
		makeCommonObjects();
		grid = new Grid(levelToEdit);//CHANGE GRID COORDINATES WHILE MAKING A NEW LEVEL
		makeButton = new GameSprite(Player.filename);
		makeButton.setPosition(0,0);
		player = new Player(100, 420);
		background = new Background(1, grid);
	}
	//called in level editing mode to create a new level
	public Level(int width, int height)
	{
		makeCommonObjects();
		grid = new Grid(width,height);//CHANGE GRID COORDINATES WHILE MAKING A NEW LEVEL
		makeButton = new GameSprite(Player.filename);
		makeButton.setPosition(0,0);
		player = new Player(100, 420);
		background = new Background(1, grid);
	}
	/**
	 * 
	 * Clears all user objects off the grid and fully resets the level
	 */
	public void Reset()
	{
		if(!levelEditingMode)//CAN ONLY RESET IN LEVEL EDITING MODE
		{
			grid.clearUserObjects();
			menu = new SpriteMenu(listFileName, playButton);
		}
	}
	/**
	 * 
	 * common init
	 */
	public void makeCommonObjects()
	{
		hasKey = false;
		playButton = new PlayButton();
		ArrayList<SpriteButton.spriteType> sprites = new ArrayList<SpriteButton.spriteType>();
		if(levelEditingMode)
		{
			sprites.add(SpriteButton.spriteType.Platform);
			sprites.add(SpriteButton.spriteType.Jumppad);
			sprites.add(SpriteButton.spriteType.TurnLeft);
			sprites.add(SpriteButton.spriteType.TurnRight);
			menu = new SpriteMenu(sprites, playButton);
		}
		else
		{
			menu = new SpriteMenu(listFileName, playButton);
		}
		tutorialRender = new BitmapFont(Gdx.files.internal("data/tutorialfont.fnt"), Gdx.files.internal("data/tutorialfont_0.tga"), false);
		tutorialRender.setColor(Color.BLACK);
		tutorialRender.scale((AndroidGame.DP/AndroidGame.testedDP -1)/2);
	}
	/**
	 * 
	 * Called once a frame, updates each of hte levels objects
	 */
	public void update()
	{
		AndroidGame.camera.update(grid, player);
		player.update(grid);
		playButton.update(player, grid);
		menu.update(grid);
		grid.update();
		if(grid.hasKey())
			grid.getKey().update(player, grid.getFinish());
		if(levelEditingMode)//checking if the make button is clicked
		{
			if(makeButton.getBoundingRectangle().contains(Helper.PointerX(), Helper.PointerY()))
			{
				grid.makeLevel();
			}
		}
		else
		{
			doorBubble.update();
			if(hasKey)
				keyBubble.update();
		}
	}
	/**
	 * 
	 * Draws each of the levels objects
	 */
	public void draw(SpriteBatch batch, SpriteBatch hudbatch, ShapeRenderer render, SpriteBatch dragBatch)
	{
		batch.begin();
		background.draw(batch);
		grid.draw(batch);
		player.draw(batch);
		if(levelEditingMode)//drawing the make button
		{
			makeButton.draw(batch);
		}
		else
		{
			doorBubble.draw(batch);
			if(hasKey)
				keyBubble.draw(batch);
		}
		batch.end();
		menu.draw(hudbatch, render, dragBatch);
		hudbatch.begin();//the hud batch that has no projection matrix
		playButton.draw(hudbatch);
		//if the string key exists (returns the key when string is null)
		if(!AndroidGame.lang.getString("tut"+levelNum).equals("tut"+levelNum)&&!LevelScreen.isRunning)
		{
			tutorialRender.drawWrapped(hudbatch, AndroidGame.lang.getString("tut"+levelNum), 75, AndroidGame.h-AndroidGame.h/4, AndroidGame.w/1.5f);
		}
		hudbatch.end();
	}
	public boolean isBeaten()
	{
		return player.levelOver;
	}
}
