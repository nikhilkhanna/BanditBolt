package com.tangledfire.AndroidGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Logger;

public class WorldSelectScreen implements Screen
{
	public Button[] doorButtons;
	public Button backButton;
	public GameSprite[] backgrounds;
	public Sprite finalBackground;
	public TextButton unlock;
	public SpriteBatch spriteBatch;
	public AndroidGame game;
	public Preferences prefs; 
	boolean lastFrameTouched;
	boolean drifting;
	float lastTouchX;
	public float totalOffset = 0;
	public float center;
	Button closestDoor;
	public static final Rectangle unlockRect = new Rectangle(AndroidGame.w/4, AndroidGame.h/4, AndroidGame.w/2, AndroidGame.h/2);
	public static final Color unlockColor = new Color(0, .75f, 1,1);
	public Button unlockBackButton;
	public TextButton buyUnlock;
	private BitmapFont unlockText;
	public boolean isUnlockingScreen = false;
	private ShapeRenderer render;
	TextBounds unlockTextBounds;
	public WorldSelectScreen(AndroidGame game)
	{
		prefs = Gdx.app.getPreferences(AndroidGame.prefName);
		this.game = game;
		render = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		unlockText = new BitmapFont(Gdx.files.internal("data/buttonfont.fnt"), Gdx.files.internal("data/buttonfont_0.tga"), false);
	}
	@Override
	public void render(float delta) {
		if(prefs.getBoolean("levelPack")&&doorButtons[1].mynewfilename.equals("Iron_Lrg_Locked"))
		{
			doorButtons[1].doorUpdate();
		}
		if(backButton.isPressed())
		{
			game.setScreen(game.mainMenuScreen);
			return;
		}
		if(!isUnlockingScreen)
		{
		for(int i = 0; i<doorButtons.length;i++)
		{
			if(i==2)
				break;
			if(i==1&&!prefs.getBoolean("levelPack"))
			{
				break;
			}
			if(doorButtons[i].isPressed())
			{
				game.setScreen(game.levelSelectScreen[i]);
				return;
			}
		}
		if(!prefs.getBoolean("levelPack")&&unlock.isPressed()||(doorButtons[1].isPressed()&&!prefs.getBoolean("levelPack"))||(doorButtons[2].isPressed()&&!prefs.getBoolean("levelPack")))//TODO if you visit this screen level15 is complete and you ahven't got new levels prompt
		{
			showUnlockScreen();
		}
		if(Gdx.input.isTouched())
		{
			drifting = false;
			if(lastFrameTouched)
			{
				translate(Gdx.input.getX()-lastTouchX);
				lastTouchX = Gdx.input.getX();
			}
			else
			{
				lastFrameTouched = true;
				lastTouchX = Gdx.input.getX();
			}
		}
		else
		{
			lastFrameTouched = false;
			if(!drifting)
			{
				float closestDistance = 999;
				for(int i = 0; i<doorButtons.length;i++)
				{
					if(Math.abs(doorButtons[i].getX()-center)<closestDistance)
					{
						closestDistance = Math.abs(doorButtons[i].getX()-center);
						closestDoor = doorButtons[i];
					}
				}
				drifting = true;
			}
				if(drifting)
				{
				translate(.1f*(center-closestDoor.getX()));
				if(Math.abs(center-closestDoor.getX())<Helper.epsilon)
				{
					drifting = false;
				}
			}
		}
		}
		else
		{
			if(buyUnlock.isPressed())
			{
				AndroidGame.actionResolver.purchaseLevelPack();
			}
			if(unlockBackButton.isPressed())
			{
				isUnlockingScreen = false;
				if(!prefs.getBoolean("levelPack"))
					AndroidGame.actionResolver.queryInventory();
			}
		}
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		finalBackground.draw(spriteBatch);
		for(int i = 0; i<backgrounds.length;i++)
		{
			backgrounds[i].draw(spriteBatch);
		}
		for(int i = 0; i<doorButtons.length;i++)
		{
			doorButtons[i].draw(spriteBatch);
		}
		if(!prefs.getBoolean("levelPack"))
			unlock.draw(spriteBatch);
		backButton.draw(spriteBatch);
		spriteBatch.end();
		if(isUnlockingScreen)
		{
			render.begin(ShapeType.Filled);
			render.setColor(unlockColor);
			render.rect(unlockRect.x, unlockRect.y, unlockRect.width, unlockRect.height);
			render.end();
			spriteBatch.begin();
			buyUnlock.draw(spriteBatch);
			unlockTextBounds = unlockText.getBounds(AndroidGame.lang.getString("unlocktext"));
			unlockText.draw(spriteBatch, AndroidGame.lang.getString("unlocktext"), buyUnlock.getX()+buyUnlock.getWidth()/2-unlockTextBounds.width/2, buyUnlock.getY()-buyUnlock.getHeight()/3);
			unlockBackButton.draw(spriteBatch);
			spriteBatch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}
	public void showUnlockScreen()
	{
		if(!isUnlockingScreen)
		{
			isUnlockingScreen = true;
		}
	}
	public void translate(float xTranslate)
	{
		for(int i = 0; i<doorButtons.length;i++)
		{
			doorButtons[i].translateX(xTranslate);
		}
		for(int i = 0;i <backgrounds.length;i++)
		{
			backgrounds[i].translateX(xTranslate);
		}
		totalOffset+=xTranslate;
	}
	@Override
	public void show() {
		if(!MainMenuScreen.menuSong.isPlaying())
		{
			MainMenuScreen.menuSong.play();
		}
		if(!prefs.getBoolean("levelPack"))
			AndroidGame.actionResolver.queryInventory();
		// TODO Auto-generated method stub
		if(!MainMenuScreen.menuSong.isPlaying())
		{
			MainMenuScreen.menuSong.play();
		}
		doorButtons = new Button[3];
		unlock = new TextButton(AndroidGame.lang.getString("unlock"));
		unlock.setSize(unlock.getWidth()*2f, unlock.getHeight()*2f);
		for(int i = 0; i<doorButtons.length;i++)
		{
			if(i==0)
				doorButtons[i] = new Button("Wood_Lrg_Static","Wood_Lrg_Static", AndroidGame.w/2+240*i, 200);
			else if(i==1)
			{
				String ironDoorAssetName;
				if(prefs.getBoolean("levelPack"))
				{
					ironDoorAssetName = "Iron_Lrg_Static";
				}
				else
				{
					ironDoorAssetName = "Iron_Lrg_Locked";
				}
				doorButtons[i] = new Button(ironDoorAssetName,ironDoorAssetName, AndroidGame.w/2+240*i, 200);
			}
			else if (i==2)
			{
				doorButtons[i] = new TextButton("dooroutline", "dooroutline", AndroidGame.lang.getString("comingsoon"), 0, 0, 10, 150);
			}
			doorButtons[i].setSize(doorButtons[i].getWidth()*1.5f, doorButtons[i].getHeight()*1.5f);
			doorButtons[i].setPosition(AndroidGame.w/2-doorButtons[i].getWidth()/2 +doorButtons[i].getWidth()*i*2, AndroidGame.h/2-doorButtons[i].getHeight()/2);
		}
		unlock.setPosition(AndroidGame.w-unlock.getWidth()*1.1f, unlock.getHeight()*.1f);
		center = AndroidGame.w/2-doorButtons[0].getWidth()/2;
		totalOffset = 0;
		backButton = new Button("Back_Arrow","Back_Arrow_Touch",30,AndroidGame.h-80);
		backButton.setSize(backButton.getWidth()*1.3f, backButton.getHeight()*1.3f);
		backButton.setPosition(backButton.getWidth()/2,AndroidGame.h-1.5f*backButton.getHeight());
		buyUnlock = new TextButton(AndroidGame.lang.getString("buyunlock"));
		buyUnlock.setSize(buyUnlock.getWidth()*1.75f, buyUnlock.getHeight()*2f);
		buyUnlock.setPosition(unlockRect.x+unlockRect.width/2-buyUnlock.getWidth()/2, unlockRect.y+unlockRect.height/2-buyUnlock.getHeight()/2);
		unlockBackButton = new Button("Back_Arrow","Back_Arrow_Touch",30,AndroidGame.h-80);
		unlockBackButton.setSize(unlockBackButton.getWidth()*1.15f, unlockBackButton.getHeight()*1.15f);
		unlockBackButton.setPosition(unlockRect.x, unlockRect.y+unlockRect.height-unlockBackButton.getHeight());
		finalBackground = new Sprite(new Texture(Gdx.files.internal("data/Wood_Wall.png")));
		finalBackground.setSize(AndroidGame.w, AndroidGame.h);
		finalBackground.setPosition(0, 0);
		backgrounds = new GameSprite[doorButtons.length+1]; 
		for(int i = 0; i<backgrounds.length;i++)
		{
			backgrounds[i] = new GameSprite(new Texture(Gdx.files.internal("data/Wood_Wall.png")));
			backgrounds[i].setSize(AndroidGame.w, AndroidGame.h);
			backgrounds[i].setPosition(-AndroidGame.w+i*backgrounds[i].getWidth(), 0);
		}
		if(prefs.getBoolean("level15")&&!prefs.getBoolean("levelPack"))
		{
			showUnlockScreen();
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		doorButtons=null;
		backButton = null;
		unlock = null;
		buyUnlock = null;
		backgrounds = null;
		finalBackground = null;
		isUnlockingScreen = false;
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
		spriteBatch.dispose();
		render.dispose();
	}
	
}