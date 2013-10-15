package com.tangledfire.AndroidGame;

import java.util.Random;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
/**
 * @author Nikhil Khanna
 * The Level Select screen that displays the 15 levels you can go to behind a specific door
 */
public class LevelSelectScreen implements Screen{
	public Array<LevelButton> buttons;
	public Button backButton;
	public AndroidGame game;
	public SpriteBatch batch;
	private final int numLevels = 15;
	public int startingLevel = 0;
	GameSprite[][] tiles;
	public TextureAtlas myAtlas;
	public String myName;
	public final int sideLength = 400;
	public Random rand;
	public LevelSelectScreen(AndroidGame game, int startingLevel)
	{
		if(startingLevel<16)
		{
			myAtlas = AndroidGame.woodBackgrounds;
			myName = "Wood-";
		}
		else
		{
			myAtlas = AndroidGame.metalBackgrounds;
			myName = "metal-";
		}
		this.startingLevel = startingLevel;
		this.game = game;
		buttons = new Array<LevelButton>();
		batch = new SpriteBatch();
	}
	public void render(float delta) {
		for(int i = 0; i<buttons.size;i++)
		{
			if(buttons.get(i).isPressed()&&buttons.get(i).isUnlocked())
			{
				MainMenuScreen.menuSong.stop();
				game.levelToPlay=buttons.get(i).levelNumber;
				game.setScreen(game.levelScreen);
				return;
			}
		}
		if(backButton.isPressed())
		{
			game.setScreen(game.worldSelectScreen);
			return;
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(int i = 0; i<tiles.length;i++)
		{
			for(int j = 0; j<tiles[0].length;j++)
			{
				tiles[i][j].draw(batch);
			}
		}
		for(int i = 0; i<buttons.size;i++)
		{
			buttons.get(i).draw(batch);
		}
		backButton.draw(batch);
		batch.end();
	}
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		if(!MainMenuScreen.menuSong.isPlaying())
		{
			MainMenuScreen.menuSong.play();
		}
		// TODO Auto-generated method stub
		rand = new Random();
		backButton = new Button("Back_Arrow", "Back_Arrow_Touch", 50, AndroidGame.h-70);
		backButton.setSize(backButton.getWidth()*1.3f, backButton.getHeight()*1.3f);
		backButton.setPosition(backButton.getWidth()/2,AndroidGame.h-1.5f*backButton.getHeight());
		AndroidGame.camera.resetToOrigin();
		final int lines = 3;
		int level = startingLevel;
		for(int i = 0;i<lines;i++)
		{
			for(int j = 0; j<5;j++)
			{
				buttons.add(new LevelButton(level,(AndroidGame.w/6)*j+AndroidGame.w/5,AndroidGame.h - ((int)(AndroidGame.h/3.5f*i+AndroidGame.h/4))));
				level++;
			}
		}
		tiles = new GameSprite[(AndroidGame.w/sideLength)+1][(AndroidGame.h/sideLength)+1];
		int fileNumber = 1;
		for(int i = 0; i<tiles.length;i++)
		{
			for(int j = 0; j<tiles[0].length;j++)
			{
				if(myName.equals("Wood-"))
				{
					fileNumber = rand.nextInt(4)+1;
				}
				else
				{
					fileNumber = rand.nextInt(2)+1;
				}
				tiles[i][j] = new GameSprite(myAtlas.findRegion(myName+fileNumber), i*sideLength,j*sideLength);
			}
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		rand = null;
		backButton = null;
		tiles = null;
		buttons.clear();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
	}

}
