package com.tangledfire.AndroidGame;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader;

public class Player extends GameSprite{
public static final String filename = "data/Player.png";
//Player Sounds
public static Sound landing = Gdx.audio.newSound(Gdx.files.internal("data/landing.mp3"));
public static Sound squish = Gdx.audio.newSound(Gdx.files.internal("data/squish.mp3"));
public static Sound run = Gdx.audio.newSound(Gdx.files.internal("data/run.wav"));
public static Sound jump = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
public static Sound fireLit = Gdx.audio.newSound(Gdx.files.internal("data/fire.wav"));
public static Sound burn = Gdx.audio.newSound(Gdx.files.internal("data/burn2.wav"));
public static Sound turn = Gdx.audio.newSound(Gdx.files.internal("data/turn.wav"));
public static Sound win = Gdx.audio.newSound(Gdx.files.internal("data/win.wav"));
//Sound Volumes
public static float landingVol = .5f;
public static float squishVol = 1;
public static float runVol = .5f;
public static float jumpVol = .5f;
public static float fireVol = 1;
public static float burnVol = 1f;
public static float turnVol = 1.9f;
public static float winVol = 15f;
boolean isDead = false;
boolean finished = false;
boolean levelOver = false;
boolean didHitFinish = false;
public static long burnID;
public boolean playingRun;
//constants for determinting
public final float gravity = -1000;
public final float terminalVelocity = -750;
public final float feetHeight = 7;
public final float runningSpeed = 200;//normal running speed
public final float sprintingSpeed = 400;//normal sprinting speed (after hitting boost)
public final float sprintingDecay = 70;//how much sprinting slows down each frame
public boolean falling = false;//if gravity should affect the sprite
public final float jumpHeight = 570;
public boolean running = false;
public float xTranslate;
public float yTranslate;
public Rectangle feet;
public final float feetWidth = 20;
public static final TextureRegion initialTex = AndroidGame.gameTextures.findRegion("Thief_Run", 1);
public Animation runAnim;
public Animation jumpAnim;
public Animation burnAnim;
public Animation deathAnim;
public Animation winAnim;
boolean winSoundPlaying = false;
boolean playBurnAnimation = false;
float animationStateTime = 0;
float burnAnimStateTime = 0;
boolean wasFlipped = false;
	public Player(int x, int y) {
		super(AndroidGame.gameTextures.findRegion("Thief_Run", 1));
		setX(x);
		setY(y);
		isDead = false;
		finished = false;
		levelOver = false;
		falling = false;
		feet = new Rectangle(x,y,feetWidth,feetHeight);
		setPosition(getX(), getY()+1);
		velocity.x = 0;
		makeAnimations();
		if(isFlipX())
		{
			flip(true,false);
		}
	}
	public Player()
	{
		super(AndroidGame.gameTextures.findRegion("Thief_Run", 1));
		isDead = false;
		finished = false;
		levelOver = false;
		feet = new Rectangle(getX(),getY(),feetWidth,feetHeight);
		setPosition(getX(), getY()+1);
		velocity.x = 0;
		makeAnimations();
	}
	/**
	 * 
	 * Makes the animations which the player can perform (called in constructor)
	 */
	public void makeAnimations()
	{
		Array<AtlasRegion> runRegions = AndroidGame.gameTextures.findRegions("Thief_Run");
		runAnim = new Animation(.05f, runRegions);
		Array<AtlasRegion> jumpRegions = AndroidGame.gameTextures.findRegions("Thief_Jump");
		jumpAnim = new Animation(.05f, jumpRegions);
		Array<AtlasRegion> deathRegions = AndroidGame.gameTextures.findRegions("Thief_Death");
		deathAnim = new Animation(.025f, deathRegions);
		Array<AtlasRegion> burnRegions = AndroidGame.gameTextures.findRegions("Fire_Run_Thief");
		burnAnim = new Animation(.1f, burnRegions);
		Array<AtlasRegion> victoryRegions = AndroidGame.gameTextures.findRegions("Thief_Victory");
		winAnim = new Animation(.075f, victoryRegions);
	}
	/**
	 * 
	 * @return      the collision box of the player
	 */
	public Rectangle getBoundingBox()
	{
		return new Rectangle(getX(), getY(), 20, 40);
	}
	/**
	 * 
	 * translates the player's position
	 * @param  x  amount to translate the player in x direction
	 * @param y amount to translate the player in the y direction
	 */
	public void translate(float x, float y)
	{
		super.translate(x, y);
		feet.x+=x;
		feet.y+=y;
	}
	/**
	 * 
	 * Stops the burning animation if the player is on fire
	 */
	public void stopBurn()
	{
		burnAnimStateTime = 0;
		animationStateTime = 0;
		playBurnAnimation = false;
		burn.stop();
	}
	/**
	 * 
	 * Starts the burning animation if the player is on fire
	 */
	public void startBurn()
	{
		burnAnimStateTime = 0;
		animationStateTime = 0;
		playBurnAnimation = true;
		burn.stop();
		burnID = burn.loop(burnVol);
	}
	/**
	 * 
	 * Stops the running animation
	 */
	public void StopRun()
	{
		run.stop();
		playingRun = false;
	}
	/**
	 * 
	 * Starts the running animation
	 */
	public void StartRun()
	{
		run.loop(runVol);
		playingRun = true;
	}
	/**
	 * 
	 * updates the position of the player based on the velocity
	 */
	public void updatePosition()
	{
		xTranslate = velocity.x*Gdx.graphics.getDeltaTime();
		yTranslate = velocity.y*Gdx.graphics.getDeltaTime();
//		translate(xTranslate,yTranslate);
	}
	/**
	 * 
	 * Applies gravity to the player if he is in the air (and stops his fall if he 
	 * hits a platform)
	 * @param  grid  the grid containing all of the objects
	 */
	public void updateGravity(Grid grid)//subtracts gravity from velocity if not hitting platform
	{
		if(falling)
		{
			if(playingRun)
			{
				StopRun();
			}
			velocity.y+=(gravity*Gdx.graphics.getDeltaTime());//multiplying all things by delta time
			if(velocity.y<terminalVelocity)
				velocity.y=terminalVelocity;
			if(velocity.y<0&&willBeOnPlatform(grid))
			{
				falling = false;
				velocity.y=0;
				landing.play(landingVol);
			}
		}
		else
		{
			if(!playingRun)
			{
				StartRun();
			}
			if(!onPlatform(grid))
			{
				animationStateTime = 0;
				falling = true;
			}
		}
	}
	/**
	 * 
	 * Starts a jump
	 */
	public void jump()
	{
		animationStateTime = 0;
		jump.play(jumpVol);
		velocity.y = jumpHeight;
		falling = true;
	}
	/**
	 * 
	 * If the player collides with a turn, this is used to determine 
	 * if he should change direction or remain going forward
	 * @param  t  the turn object that the player just hit
	 * @return      whether or not the player has turned
	 */
	public boolean turn(Turn t)
	{
		if(t.left&&velocity.x>0)
		{
			velocity.x = -velocity.x;
			turn.play(turnVol);
			return true;
		}
		else if(!t.left&&velocity.x<0)
		{
			velocity.x = -velocity.x;
			turn.play(turnVol);
			return true;
		}
		return false;
	}
	public void boost()
	{
		if(Math.abs(velocity.x)<350)
		{
			fireLit.play(fireVol);	
			startBurn();
		}
		if(velocity.x<0)//make sure we continue to go left
		{
			velocity.x = -sprintingSpeed;
		}
		else
		{
			velocity.x = sprintingSpeed;
		}
	}
	public void boostDecay()
	{
		if(velocity.x>runningSpeed)
		{
			velocity.x -= sprintingDecay * Gdx.graphics.getDeltaTime();
			burn.setVolume(burnID, (velocity.x-runningSpeed)*burnVol/sprintingSpeed);
			if(velocity.x<=runningSpeed)
			{
				velocity.x = runningSpeed;
				stopBurn();
			}
		}
		else if(velocity.x < -runningSpeed)
		{
			velocity.x += sprintingDecay * Gdx.graphics.getDeltaTime(); 
			burn.setVolume(burnID, (velocity.x+runningSpeed)*burnVol/-sprintingSpeed);
			if(velocity.x >= -runningSpeed)
			{
				velocity.x = -runningSpeed;
				stopBurn();
			}
		}
	}
	public Rectangle AABB()//gets the rectangle encompassing the area of the movement this frame
	{
		Rectangle destinationRect = new Rectangle(feet.x+xTranslate, feet.y+yTranslate, feet.width, feet.height);
		//returns the largest possible rectangle (lowest-left corner biggest width and height possbile
		return new Rectangle(Math.min(feet.x, destinationRect.x), Math.min(feet.y, destinationRect.y), Math.max(destinationRect.x+destinationRect.width-feet.x, feet.x+feet.width-destinationRect.x),Math.max(destinationRect.y+destinationRect.height-feet.y, feet.y+feet.height-destinationRect.y));
	}
	public boolean burning()
	{
		return Math.abs((double)velocity.x) > runningSpeed;
	}
	/**
	 * 
	 * Called once per frame and calls methods handling the player's collision, translation
	 * and ending the level
	 * @param  grid  the grid containing all of the objects
	 */
	public void update(Grid grid)
	{
		if(!running)
		{
			if(currentFrame.isFlipX())
			{
				currentFrame.flip(true, false);
			}
		}
		if(!running && LevelScreen.isRunning)
		{
			running = true;
			velocity.x = runningSpeed;
			StartRun();
		}
		if(running&&!isDead&&!finished)
		{
			updateGravity(grid);
			updatePosition();
			collision(grid);
			boostDecay();
			animationStateTime+=Gdx.graphics.getDeltaTime();
			if(playBurnAnimation)
			{
				burnAnimStateTime +=Gdx.graphics.getDeltaTime();
				currentFrame = burnAnim.getKeyFrame(burnAnimStateTime, false);
			}
			else if(!falling)
				currentFrame = runAnim.getKeyFrame(animationStateTime, true);
			else
				currentFrame = jumpAnim.getKeyFrame(animationStateTime, false);
			//ensuring that player is facing to the left when running left and vice versa for right
			if(velocity.x<0)
			{
				if(!currentFrame.isFlipX())
					currentFrame.flip(true,false);
			}
			else
			{
				if(currentFrame.isFlipX())
				{
					currentFrame.flip(true, false);
				}
			}
		}
		if(isDead)
		{
			currentFrame = deathAnim.getKeyFrame(animationStateTime);
			animationStateTime += Gdx.graphics.getDeltaTime();
			if(deathAnim.isAnimationFinished(animationStateTime))
			{
				reset(grid);
			}
		}
		else if(isDeadGrid(grid)||hitSpike(grid))
		{
			StopRun();
			stopBurn();
			squish.play(squishVol);
			animationStateTime = 0;
			isDead = true;
		}
		if(finished)
		{
			currentFrame = winAnim.getKeyFrame(animationStateTime);
			if(wasFlipped&&!currentFrame.isFlipX())
			{
				currentFrame.flip(true, false);
			}
			animationStateTime+=Gdx.graphics.getDeltaTime();
			if(animationStateTime>.35f&&!winSoundPlaying)
			{
			 	win.play(winVol);
			 	winSoundPlaying = true;
			}
			if(winAnim.isAnimationFinished(animationStateTime))
			{
				levelOver = true;
			}
		}
		else if(hitFinish(grid))
		{
			wasFlipped = velocity.x<0;
			StopRun();
			stopBurn();
			animationStateTime = 0;
			finished = true;
		}
	}
	public void reset(Grid grid)
	{
		winSoundPlaying = false;
		boolean wasFlipped = false;
		if(velocity.x<0)
		{
			wasFlipped = true;
		}
		isDead = false;
		finished = false;
		didHitFinish = false;
		StopRun();
		stopBurn();
		currentFrame = initialTex;
		running = false;
		LevelScreen.isRunning = false;
		velocity.x=0;
		velocity.y = 0;
		falling = false;
		GridSquare startSquare = grid.startLocation();
		setPosition(startSquare.rect.x, startSquare.rect.y);
		setPosition(getX(), getY()+1);
		if(wasFlipped&&currentFrame.isFlipX())
		{
			currentFrame.flip(true, false);
		}
		if(grid.hasKey())
		{
			grid.getKey().reset(grid.getFinish());
		}
		LevelScreen.runningSong.stop();
		if(!LevelScreen.placementSong.isPlaying())
			LevelScreen.placementSong.play();
		AndroidGame.camera.reset(this);
	}
	public void setPosition(float x, float y)
	{
		super.setPosition(x, y);
		feet.setX(x);
		feet.setY(y);
	}
	public Array<GameSprite> spritesToCheck(Grid grid)
	{
		Array<GameSprite> sprites = grid.getSprites();
		Array<GameSprite> newSprites = new Array<GameSprite>();
		Rectangle AABB = AABB();
		for(int i = 0; i<sprites.size;i++)
		{
			if(AABB.overlaps(sprites.get(i).getBoundingRectangle()))
			{
				newSprites.add(sprites.get(i));
			}
		}
		return newSprites;
	}
	/**
	 * 
	 * This method checks if the player collided with an object, and if so, responds accordingly
	 * depending on the object
	 * @param  grid  the grid containing all of the objects
	 * @return      whether or not a collision occurred
	 */
	public boolean collision(Grid grid)
	{
		Array<GameSprite> sprites = spritesToCheck(grid);
		final int collisionIntervals = 100;
		float stepX = xTranslate/collisionIntervals;
		float stepY = yTranslate/collisionIntervals;
		for(int i = 0; i<collisionIntervals;i++)
		{
			for(int j = 0; j<sprites.size;j++)
			{
				if(feet.overlaps(sprites.get(j).getBoundingRectangle()))
				{
					if(sprites.get(j) instanceof JumpPad&&velocity.y<=0)
					{
						jump();
						JumpPad myJumpPad = (JumpPad)sprites.get(j);
						myJumpPad.startAnimation();
						return true;
					}
					else if(sprites.get(j) instanceof Turn)
					{
						if(turn((Turn)sprites.get(j)))
						{
							Turn t = (Turn)sprites.get(j);
							t.playerHit();
							return true;
						}
					}
					else if(sprites.get(j) instanceof SpeedBoost)
					{
						boost();
					}
				}
			}
			translate(stepX, stepY);
		}
		return false;
	}
	public boolean onPlatform(Grid grid)//sets the players position to a platform if he is on it
	{
		Array<Platform> platforms = grid.getPlatforms();//probably very inefficient fix later
		for(int i = 0; i<platforms.size;i++)
		{
			if(feet.overlaps(platforms.get(i).getBoundingRectangle()))//if any of the platforms are intersecting w/ feet
			{
				setY(platforms.get(i).getY());
				feet.y = platforms.get(i).getY();
				return true;
			}
		}
		return false;
	}
	public boolean willBeOnPlatform(Grid grid)
	{
		Array<Platform> platforms = grid.getPlatforms();
		for(int i = 0; i<platforms.size;i++)
		{
			if(intersectsX(platforms.get(i)))
			{
				if(getY()>platforms.get(i).getY())
				{
					if(getY()+velocity.y*Gdx.graphics.getDeltaTime()<=platforms.get(i).getY())
					{
						setY(platforms.get(i).getY()+1);
						feet.y = platforms.get(i).getY()+1;
						velocity.y = 0;
						return true;
					}
				}
			}
		}
		return false;
	}
	public void goToCollisionPoint(Rectangle otherRect)
	{
		final float intervals = 100;
		float xInterval = -xTranslate/intervals;
		float yInterval = -yTranslate/intervals;
		for(int i = 0; i<intervals;i++)
		{
			translate(xInterval,yInterval);
			if(!otherRect.overlaps(feet))
				return;
		}
	}
	public boolean hitPad(Grid grid)
	{
		if(velocity.y<=0)
		{
		Array<JumpPad> pads = grid.getJumpPads();
			for(int i = 0; i<pads.size;i++)
			{
				if(feet.overlaps(pads.get(i).getBoundingRectangle()))
				{
					goToCollisionPoint(pads.get(i).getBoundingRectangle());
					jump();
					return true;
				}
			}
		}
		return false;
	}
	public boolean hitTurn(Grid grid)
	{
		//if(!falling)
		{
		Array<Turn> turns = grid.getTurns();
		for(int i = 0; i<turns.size;i++)
		{
			if(feet.overlaps(turns.get(i).getBoundingRectangle()))
			{
				if(turns.get(i).left&&velocity.x>0)
				{
					goToCollisionPoint(turns.get(i).getBoundingRectangle());
					if(velocity.x>0)
					{
						velocity.x = -velocity.x;
					}
				}
				else if(!turns.get(i).left&&velocity.x<0)
				{
					goToCollisionPoint(turns.get(i).getBoundingRectangle());
					if(velocity.x<0)
					{
						velocity.x = -velocity.x;
					}
				}
				return true;
			}
		}
		}
		return false;
	}
	public boolean hitBoost(Grid grid)
	{
		Array<SpeedBoost> boosts = grid.getSpeedBoosts();
		for(int i = 0; i<boosts.size;i++)
		{
			if(feet.overlaps(boosts.get(i).getBoundingRectangle()))
			{
				if(Math.abs(velocity.x)==runningSpeed)
				{
					goToCollisionPoint(boosts.get(i).getBoundingRectangle());
				}
				if(velocity.x<0)//make sure we continue to go left
				{
					velocity.x = -sprintingSpeed;
				}
				else
				{
					velocity.x = sprintingSpeed;
				}
				return true;
			}
		}
		return false;
	}
	public boolean hitSpike(Grid grid)
	{
		Array<Spike> spikes = grid.getSpikes();
		for(int i = 0; i<spikes.size;i++)
		{
			if(feet.overlaps(spikes.get(i).getBoundingRectangle()))
			{
				return true;
			}
		}
		return false;
	}
	public boolean hitFinish(Grid grid)
	{
		 Finish f = grid.getFinish();
		 if(f.isOpen && getBoundingRectangle().overlaps(f.getBoundingRectangle()))
		 {
			 if(falling)
			 {
				 didHitFinish = true;
			 }
			 else
			 {
				StopRun();
			 	stopBurn();
//			 	win.play(winVol);
			 	return true;
			 }
		 }
		 else
		 {
			 if(didHitFinish)
			 {
				didHitFinish = false;
				StopRun();
				stopBurn();
				return true;
			 }
		 }
		 return false;
	}
	public static String getFilename()
	{
		return filename;
	}
	public boolean isOffGrid(Grid g)
	{
		if(g.size.overlaps(getBoundingRectangle()))
		{
			return false;
		}
		return true;
	}
	public boolean isDeadGrid(Grid g)
	{
		if(g.size.overlaps(getBoundingRectangle()))
			return false;
		if(getX()+feet.width>g.size.x&&getX()<g.size.x+g.size.width)//if within x axis of grid
		{
			if(getY()+getHeight()<g.size.y)
			{
				return true;
			}
			return false;
		}
			return true;
	}
	public boolean intersectsX(Platform p)
	{
		return p.getX()<=getX()+30&&p.getX()+p.getWidth()>getX();//returns true if player is within left and right boundaries of platform
	}
	public Rectangle getBoundingRectangle()
	{
		return new Rectangle(getX(),getY(),20,40);
	}
	public void draw(SpriteBatch spriteBatch)
	{
		if(!isOffScreen())
		{
		if(currentFrame == null)
			super.draw(spriteBatch);
		else
			spriteBatch.draw(currentFrame, getX()-getWidth()/2, getY());
		}
	}
}
