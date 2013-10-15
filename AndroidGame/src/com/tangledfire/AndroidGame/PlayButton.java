package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * @author Nikhil Khanna
 * The button that plays the game when it is hit
 */
public class PlayButton extends Button//TODO make it change to a stop sign if game is running
{
	public static final String filename = "Play";
	public static final float totalDelay = .5f;//used to make 
	public static TextureRegion play = AndroidGame.gameTextures.findRegion("Play");
	public static TextureRegion stop = AndroidGame.gameTextures.findRegion("Stop");
	public static TextureRegion playTouch = AndroidGame.gameTextures.findRegion("Play_Touch");
	public static TextureRegion stopTouch = AndroidGame.gameTextures.findRegion("Stop_Touch");
	public PlayButton()
	{
		super("Play", "Play_Touch");
		setSize(getWidth()*2, getHeight()*2);
		setPosition(AndroidGame.w-(getWidth()), AndroidGame.h-(getHeight()));
	}
	public void update(Player p, Grid grid)
	{
		if(LevelScreen.isRunning)
		{
			normalTex = stop;
			clickedTex = stopTouch;
		}
		else
		{
			normalTex = play;
			clickedTex = playTouch;
		}
		if(!LevelScreen.isRunning&&isPressed()&&!DraggableSprite.dragged)
		{
			LevelScreen.placementSong.pause();
			LevelScreen.runningSong.play();
			LevelScreen.isRunning = true;
		}
		else if(LevelScreen.isRunning&&isPressed())
		{
			LevelScreen.runningSong.pause();
			LevelScreen.placementSong.play();
			p.reset(grid);
		}
	}
}
