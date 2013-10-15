package com.tangledfire.AndroidGame;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sun.corba.se.spi.ior.MakeImmutable;
/**
 * main game class (sets up most of the global variables and screens)
 * @author Nikhil Khanna
 */
public class AndroidGame extends Game {
	//developer option: whether or not the level editor should open
	private static final boolean islevelEditingMode = false;
	//developer option: whether or not the game is in test mode (all levels unlocked)
	public static final boolean testMode = false;
	public static final String levelToEdit = "level28.txt";
	public static final int numLevels = 30;
	public static final int worldLevels = 15;
	public static final String prefName = "my-preferences";
	public static boolean levelEditingMode;
	public static GameCamera camera;
	//width and height of game
	public static int w;
	public static int h;
	//initial dimensions of a level being editing
	public static int editHeight = 32;
	public static int editWidth = 40;
	//texture atlases with all of the game textures
	public static TextureAtlas gameTextures;
	public static TextureAtlas woodBackgrounds;
	public static TextureAtlas metalBackgrounds;
	public LevelScreen levelScreen;
	public LevelSelectScreen[] levelSelectScreen;
	public MainMenuScreen mainMenuScreen;
	public WorldSelectScreen worldSelectScreen;
	public LevelFinishScreen levelFinishScreen;
	public SplashScreen splashScreen;
	public OptionsScreen optionsScreen;
	public static LanguagesManager lang;
	public int levelToPlay = 1;
	//Allows access to Android Specific Code
	public static ActionResolver actionResolver;
	public static float pixelDensity;
	public static boolean hasLevelPack;
	public static boolean didConnect;
	//the tested screen dimensions (used for scaling)
	public static final float testedWidth = 800;
	public static final float testedHeight = 480;
	public static float actualWidthRatio;
	public static float actualHeightRatio;
	public static final float maxRatio = 1.5f;
	//the tested pixel densities (used for scaling)
	public static final float testedDP = 1.294f;
	public static float DP;
	public static float widthRatio;
	public static float heightRatio;
	public AndroidGame(ActionResolver resolver)
	{
		super();
		actionResolver = resolver;
	}
	@Override
	public void create() {
		Preferences prefs = Gdx.app.getPreferences(prefName);	
		//handles first time launching
		if(!prefs.getBoolean("hasBeenLaunched"))
		{
			for(int i = 1; i<=numLevels;i++)
			{
				prefs.putBoolean("level"+i, false);
			}
			prefs.putBoolean("hasBeenLaunched", true);
			prefs.putBoolean("music", true);
			prefs.putBoolean("levelPack", false);
			prefs.flush();
		}
		if(testMode)
		{
			for(int i = 1; i<=numLevels;i++)
			{
				prefs.putBoolean("level"+i, true);
			}
			prefs.putBoolean("hasBeenLaunched", false);
			prefs.putBoolean("levelPack", true);
			prefs.flush();
		}
		if(!prefs.getBoolean("levelPack"))
			actionResolver.queryInventory();
		lang = LanguagesManager.getInstance();
		gameTextures = new TextureAtlas(Gdx.files.internal("data/gameAssets.txt"));
		woodBackgrounds = new TextureAtlas(Gdx.files.internal("data/WoodBG.txt"));
		metalBackgrounds = new TextureAtlas(Gdx.files.internal("data/MetalBG.txt"));
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		levelEditingMode = islevelEditingMode&&!(Gdx.app.getType() == ApplicationType.Android);
		w = Gdx.graphics.getWidth();
	    h = Gdx.graphics.getHeight();
	    actualWidthRatio = w/testedWidth;
	    actualHeightRatio = h/testedHeight;
	    widthRatio = actualWidthRatio;
	    heightRatio = actualHeightRatio;
	    DP = Gdx.graphics.getDensity();
	    if(actualWidthRatio>maxRatio)
	    {
	    	widthRatio = maxRatio;
	    }
	    if(actualHeightRatio>maxRatio)
	    {
	    	heightRatio = maxRatio;
	    }
	    //we only use the smaller of the 2 ratios to scale buttons (fuck uneven scaling)
	    if(widthRatio>heightRatio)
	    {
	    	widthRatio = heightRatio;
	    }
	    else
	    {
	    	heightRatio = widthRatio;
	    }
	    pixelDensity = Gdx.graphics.getDensity();
	    camera = new GameCamera();
		camera.setToOrtho(false, w, h);
		//making all the screens
		levelSelectScreen = new LevelSelectScreen[2];
		levelScreen = new LevelScreen(this);
		//based on the number of levels, init the level select screens (15 per screen)
		for(int i = 0; i<levelSelectScreen.length;i++)
		{
			levelSelectScreen[i] = new LevelSelectScreen(this, worldLevels*i+1);
		}
		mainMenuScreen = new MainMenuScreen(this);
		worldSelectScreen = new WorldSelectScreen(this);
		levelFinishScreen = new LevelFinishScreen(this);
		optionsScreen = new OptionsScreen(this);
		splashScreen = new SplashScreen(this);
		if(prefs.getBoolean("music"))
		{
			LevelScreen.placementSong.setVolume(LevelScreen.placementVol);
			LevelScreen.runningSong.setVolume(LevelScreen.runningVol);
			MainMenuScreen.menuSong.setVolume(MainMenuScreen.menuVol);
		}
		else
		{
			MainMenuScreen.menuSong.setVolume(0);
			LevelScreen.placementSong.setVolume(0);
			LevelScreen.runningSong.setVolume(0);
		}
		if(islevelEditingMode)
		{
			setScreen(levelScreen);
		}
		else
		{
			setScreen(splashScreen);
		}
	}
	public static void unlockLevels()
	{
		Preferences prefs = Gdx.app.getPreferences(prefName);
		prefs.putBoolean("levelPack", true);
		prefs.flush();
	}
}

