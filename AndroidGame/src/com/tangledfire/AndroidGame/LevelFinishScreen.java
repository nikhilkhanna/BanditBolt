package com.tangledfire.AndroidGame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
/**
 * @author Nikhil Khanna
 * The "level completed" screen that shows at the end of a level
 */
public class LevelFinishScreen implements Screen
{
	public SpriteBatch spriteBatch;
	private BitmapFont levelCompletedText;
	public AndroidGame game;
	public TextButton next;
	public TextButton replay;
	public TextButton menu;
	GameSprite[][] tiles;
	public TextureAtlas myAtlas;
	public String myName;
	public final int sideLength = 400;
	public Random rand;
	public String levelCompletedString;
	Preferences prefs;
	public LevelFinishScreen(AndroidGame game)
	{
		this.game = game;
		spriteBatch = new SpriteBatch();
		levelCompletedText = new BitmapFont(Gdx.files.internal("data/levelcompletefont.fnt"), Gdx.files.internal("data/levelcompletefont_0.tga"), false);
		levelCompletedText.scale((AndroidGame.DP/AndroidGame.testedDP -1)/3);
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		if(replay.isPressed())
		{
			game.levelToPlay--;
			game.setScreen(game.levelScreen);
			return;
		}
		if(next.isPressed())
		{
			if(game.levelToPlay>game.numLevels)
			{
				game.setScreen(game.worldSelectScreen);
			}
			else if(game.levelToPlay>=16&&!prefs.getBoolean("levelPack"))
			{
				game.setScreen(game.worldSelectScreen);
			}
			else
			{
				game.setScreen(game.levelScreen);
			}
			return;
		}
		if(menu.isPressed())
		{
			if(game.levelToPlay<=15)
				game.setScreen(game.levelSelectScreen[0]);
			else if(16<game.levelToPlay&&game.levelToPlay<=30)
				game.setScreen(game.levelSelectScreen[1]);
			else//if at the margins, 15, 30
				game.setScreen(game.worldSelectScreen);
			return;
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		for(int i = 0; i<tiles.length;i++)
		{
			for(int j =0; j<tiles[0].length;j++)
			{
				tiles[i][j].draw(spriteBatch);
			}
		}
		next.draw(spriteBatch);
		replay.draw(spriteBatch);
		menu.draw(spriteBatch);
		TextBounds bounds = levelCompletedText.getBounds(levelCompletedString);
		levelCompletedText.draw(spriteBatch, levelCompletedString, AndroidGame.w/2-bounds.width/2, AndroidGame.h/2+next.getHeight()*3);
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		if(game.levelToPlay<16)
		{
			myAtlas = AndroidGame.woodBackgrounds;
			myName = "Wood-";
		}
		else
		{
			myAtlas = AndroidGame.metalBackgrounds;
			myName = "metal-";
		}
		rand = new Random();
		prefs = Gdx.app.getPreferences(AndroidGame.prefName);
		prefs.putBoolean("level"+game.levelToPlay, true);
		prefs.flush();
		next = new TextButton(AndroidGame.lang.getString("next"),50,50);
		replay = new TextButton(AndroidGame.lang.getString("replay"),100,50);
		menu = new TextButton(AndroidGame.lang.getString("menu"), 75, 125);
		next.sizeScale(1.5f);
		replay.sizeScale(1.5f);
		menu.sizeScale(1.5f);
		next.setPosition(AndroidGame.w/2-next.getWidth()/2, AndroidGame.h/2);
		menu.setPosition(AndroidGame.w/2-menu.getWidth()*1.5f, AndroidGame.h/2-menu.getHeight()*2f);
		replay.setPosition(AndroidGame.w/2+replay.getWidth()/2, AndroidGame.h/2-replay.getHeight()*2f);
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
		levelCompletedString = "Level "+game.levelToPlay+" Completed!";
		game.levelToPlay++;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		tiles = null;
		rand = null;
		next = null;
		replay = null;
		menu = null;
		LevelScreen.runningSong.stop();
		myAtlas = null;
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
		spriteBatch.dispose();
	}

}
