package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * @author Nikhil Khanna
 * Options Screen
 */
public class OptionsScreen implements Screen{
	AndroidGame game;
	public SpriteBatch batch;
	public Button backButton;
	public TextButton musicButton;
	public Sprite background;
	Preferences prefs;
	public OptionsScreen(AndroidGame game)
	{
		this.game = game;
		batch = new SpriteBatch();
	}
	@Override
	public void render(float delta) {
		if(backButton.isPressed())
		{
			game.setScreen(game.mainMenuScreen);
			return;
		}
		if(musicButton.isPressed())
		{
			boolean currentMusic = prefs.getBoolean("music");
			prefs.putBoolean("music", !currentMusic);
			prefs.flush();
			if(prefs.getBoolean("music"))
			{
				LevelScreen.placementSong.setVolume(LevelScreen.placementVol);
				LevelScreen.runningSong.setVolume(LevelScreen.runningVol);
				MainMenuScreen.menuSong.setVolume(MainMenuScreen.menuVol);
				musicButton.myText = AndroidGame.lang.getString("music_on");
			}
			else
			{
				LevelScreen.placementSong.setVolume(0);
				LevelScreen.runningSong.setVolume(0);
				MainMenuScreen.menuSong.setVolume(0);
				musicButton.myText = AndroidGame.lang.getString("music_off");
			}
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		backButton.draw(batch);
		musicButton.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		prefs = Gdx.app.getPreferences(AndroidGame.prefName);
		backButton = new Button("Back_Arrow","Back_Arrow_Touch",30,AndroidGame.h-80);
		backButton.setSize(backButton.getWidth()*1.3f, backButton.getHeight()*1.3f);
		backButton.setPosition(backButton.getWidth()/2,AndroidGame.h-1.5f*backButton.getHeight());
		String textKey;
		if(prefs.getBoolean("music"))
		{
			textKey = "music_on";
		}
		else
		{
			textKey = "music_off";
		}
		musicButton = new TextButton(AndroidGame.lang.getString(textKey), AndroidGame.w/2, AndroidGame.h/2);
		musicButton.setSize(musicButton.getWidth()*2,musicButton.getHeight()*2);
		musicButton.setPosition(AndroidGame.w/2-musicButton.getWidth()/2, AndroidGame.h/2-musicButton.getHeight()/2);
		background = new Sprite(new Texture(Gdx.files.internal("data/Wood_Wall.png")));
		background.setSize(AndroidGame.w, AndroidGame.h);
		background.setPosition(0, 0);
	}

	@Override
	public void hide() {
		background = null;
		backButton = null;
		musicButton = null;
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
		
	}

}
