package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * @author Nikhil Khanna
 * The screen at the beginning of the game (shows company name)
 */
public class SplashScreen implements Screen{
	public AndroidGame game;
	public SpriteBatch batch;
	public Texture splash;
	public final float timeToShow = 2;
	public float timeElapsed = 0;
	float splashWidth;
	float splashHeight;
	public SplashScreen(AndroidGame game)
	{
		this.game = game;
		batch = new SpriteBatch();
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(splash, AndroidGame.w/2-splashWidth/2, AndroidGame.h/2-splashHeight/2, splashWidth, splashHeight);
		batch.end();
		timeElapsed+=Gdx.graphics.getDeltaTime();
		if(Gdx.input.isTouched()||timeElapsed>timeToShow)
		{
			game.setScreen(game.mainMenuScreen);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		timeElapsed = 0;
		splash = new Texture(Gdx.files.internal("data/TangledFireLogo.png"));
		if(splash.getWidth()>AndroidGame.w)
		{
			splashWidth = AndroidGame.w;
		}
		else
		{
			splashWidth= splash.getWidth();
		}
		if(splash.getHeight()>AndroidGame.h)
		{
			splashHeight = AndroidGame.h;
		}
		else
		{
			splashHeight = splash.getHeight();
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		timeElapsed = 0;
		splash = null;
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
