package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
/**
 * @author Nikhil Khanna
 * The screen that shows during gameplay(contains a level)
 */
public class LevelScreen implements Screen{//this class is used for individiual levels (this is only used for a single level @ a time)
	public static final String levelToEdit = AndroidGame.levelToEdit;
	public static final boolean levelEditingMode = AndroidGame.levelEditingMode;
	public static final Rectangle pauseRect = new Rectangle(0, 0, AndroidGame.w, AndroidGame.h);
	public static final Color pauseColor = new Color(.38f, .38f, .38f, .5f);
	boolean paused = false;
	public int levelNum;
	private SpriteBatch batch;
	public FPSLogger logger = new FPSLogger();
	private SpriteBatch hudbatch;
	private SpriteBatch draggablespritebatch;
	public static boolean isRunning = false;
	public Level level;
	public Button pause;
	public TextButton resume;
	public TextButton menu;
	public ResetButton reset;
	private ShapeRenderer render;
	AndroidGame game;//keeps a reference to teh actual game so we can switch screens
	public static Music placementSong;
	public static Music runningSong;
	public static final float placementVol = .25f;
	public static final float runningVol = .35f;
	public OrthographicCamera draggablespritebatchcam;
	public LevelScreen(AndroidGame game)//instanciates objects common between levels 
	{
		placementSong = Gdx.audio.newMusic(Gdx.files.internal("data/placement.mp3"));
		placementSong.setLooping(true);
		placementSong.setVolume(placementVol);
		runningSong = Gdx.audio.newMusic(Gdx.files.internal("data/running.mp3"));
		runningSong.setLooping(true);
		runningSong.setVolume(runningVol);
		batch = new SpriteBatch();
		hudbatch = new SpriteBatch();
		draggablespritebatchcam = new OrthographicCamera(AndroidGame.w, AndroidGame.h);
		draggablespritebatchcam.zoom = AndroidGame.camera.zoom;
		draggablespritebatch = new SpriteBatch();
		render = new ShapeRenderer();
		this.game = game;
	}
	@Override
	public void render(float delta) {
		if(!paused)
		{
			level.update();
			if(isRunning&&!runningSong.isPlaying())
			{
				runningSong.play();
			}
			if(pause.isPressed())
			{
				pauseGame();
			}
		}
		if(levelNum<16)
			Gdx.gl.glClearColor(.361f, .18f, 0, 1);
		else
			Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(AndroidGame.camera.combined);
		draggablespritebatch.setProjectionMatrix(draggablespritebatchcam.combined);
		level.draw(batch, hudbatch, render, draggablespritebatch);
		hudbatch.begin();
		pause.draw(hudbatch);
		hudbatch.end();
		if(!levelEditingMode)
		if(level.isBeaten())
		{
			game.setScreen(game.levelFinishScreen);
			return;
		}
		if(paused)
		{
			Gdx.gl.glEnable(GL10.GL_BLEND);
	    	Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			render.begin(ShapeType.Filled);
			render.setColor(pauseColor);
			render.rect(pauseRect.x, pauseRect.y, pauseRect.width, pauseRect.height);
			render.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
			hudbatch.begin();
			resume.draw(hudbatch);
			menu.draw(hudbatch);
			reset.draw(hudbatch);
			hudbatch.end();
			//reset.update(level);
			if(resume.isPressed())
			{
				unpauseGame();
				return;
			}
			else if(reset.isPressed(level))
			{
				unpauseGame();
			}
			else if(menu.isPressed())
			{
				if(levelNum<=15)
				game.setScreen(game.levelSelectScreen[0]);
				else if (levelNum<=30)
				game.setScreen(game.levelSelectScreen[1]);
				return;
			}
		}
		logger.log();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		placementSong.play();
		if(levelEditingMode)
		{
			if(Gdx.files.external("Levels/"+levelToEdit).exists())
			{
				existingLevelEdit();//edit existing level
			}
			else
			{
				newLevelEdit(AndroidGame.editWidth,AndroidGame.editHeight);//make new level
			}
		}
		else
		{
			levelNum = game.levelToPlay;
			newLevel(levelNum);//play normal level
		}		
		pause = new Button("Pause","Pause_Touch", 10, AndroidGame.h-60);
		pause.scale(.5f);
		pause.setPosition(0+pause.getWidth()/2, AndroidGame.h-(pause.getHeight()*3/2));
	}
	public void pauseGame()
	{
		menu = new TextButton(AndroidGame.lang.getString("menu"), AndroidGame.w/2+50,AndroidGame.h/2);
		resume = new TextButton(AndroidGame.lang.getString("resume"), AndroidGame.w/2-50,AndroidGame.h/2);
		resume.setSize(resume.getWidth()*1.5f, resume.getHeight()*1.5f);
		menu.setSize(menu.getWidth()*1.5f, menu.getHeight()*1.5f);
		reset = new ResetButton();
		reset.sizeScale(1.5f);
		resume.setPosition(AndroidGame.w/2-resume.getWidth()/2, AndroidGame.h/2+resume.getHeight()*2f);
		menu.setPosition(AndroidGame.w/2-menu.getWidth()/2, AndroidGame.h/2);
		reset.setPosition(AndroidGame.w/2-reset.getWidth()/2, AndroidGame.h/2-reset.getHeight()*2f);
		level.player.StopRun();
		level.player.stopBurn();
		if(isRunning)
			runningSong.pause();
		else
			placementSong.pause();
		paused = true;
	}
	public void unpauseGame()
	{
		if(Math.abs((double)level.player.velocity.x)>level.player.runningSpeed)
		{
			level.player.startBurn();
		}
		if(isRunning)
			runningSong.play();
		else
			placementSong.play();
		paused = false;
		menu = null;
		resume = null;
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		isRunning = false;
		AndroidGame.camera.resetToOrigin();
		pause = null;
		menu = null;
		resume = null;
		level = null;
		paused = false;
		placementSong.stop();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		pauseGame();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		hudbatch.dispose();
		draggablespritebatch.dispose();
		render.dispose();
		placementSong.dispose();
		runningSong.dispose();
	}
	public void newLevel(int newLevel)
	{
		level = new Level(newLevel);
	}
	public void newLevelEdit(int width, int height)
	{
		level = new Level(width, height);
	}
	public void existingLevelEdit()
	{
		level = new Level(levelToEdit);
	}
}
