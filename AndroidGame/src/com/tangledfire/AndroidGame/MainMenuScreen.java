package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * @author Nikhil Khanna
 * The Main Menu
 */
public class MainMenuScreen implements Screen
{
	public TextButton playGame;
	public TextButton options;
	public Sprite background;
	public Sprite title;
	public AndroidGame game;
	public SpriteBatch batch;
	public static Music menuSong;
	public static final float menuVol = .25f;
	public MainMenuScreen(AndroidGame game)
	{
		menuSong = Gdx.audio.newMusic(Gdx.files.internal("data/menu.mp3"));
		menuSong.setVolume(menuVol);
		this.game = game;
		batch = new SpriteBatch();
	}
	@Override
	public void render(float delta) {
		if(playGame.isPressed())
		{
			game.setScreen(game.worldSelectScreen);
			return;
		}
		if(options.isPressed())
		{
			game.setScreen(game.optionsScreen);
			return;
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		title.draw(batch);
		playGame.draw(batch);
		options.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		menuSong.setLooping(true);
		menuSong.play();
		playGame = new TextButton(AndroidGame.lang.getString("play"),AndroidGame.w/2, AndroidGame.h/2);
		playGame.setSize(playGame.getWidth()*2,playGame.getHeight()*2);
		playGame.setPosition(AndroidGame.w/2-playGame.getWidth()/2, playGame.getHeight()*2.5f);
		options = new TextButton(AndroidGame.lang.getString("options"), AndroidGame.w/2, AndroidGame.h/2);
		options.setSize(options.getWidth()*2,options.getHeight()*2);
		options.setPosition(AndroidGame.w/2-options.getWidth()/2, options.getHeight()*1f);
		background = new Sprite(new Texture(Gdx.files.internal("data/House.png")));
		background.setSize(AndroidGame.w, AndroidGame.h);
		background.setPosition(0, 0);
		title = new Sprite(new Texture(Gdx.files.internal("data/Bandit_Bolt_Text.png")));
		title.setSize(title.getWidth()*1.25f, title.getHeight()*1.25f);
		title.setPosition(AndroidGame.w/2-title.getWidth()/2, AndroidGame.h-title.getHeight()*1.5f);
	}

	@Override
	public void hide() {
		playGame = null;
		background = null;
		title = null;
		options = null;
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
