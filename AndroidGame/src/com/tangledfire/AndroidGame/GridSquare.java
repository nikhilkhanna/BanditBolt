package com.tangledfire.AndroidGame;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GridSquare{//a single grid square that contains a static object

	public static int width = 50;//standard: 50
	public static int height = 30;//standard: 30
	public boolean startLocation = false;
	public Rectangle rect;
	public GameSprite sprite;
	public static Texture userspriteoutline = new Texture(Gdx.files.internal("data/userspriteoutline.png"));
	public static Texture outline = new Texture(Gdx.files.internal("data/squareoutline.png"));//the outline that all of the platforms have
	public static Texture highlight = new Texture(Gdx.files.internal("data/squarehighlight.png"));
	public Platform platform;
	public boolean userSprite;
	public boolean userPlatform;
	public boolean spriteDragged = false;//if a sprite is being dragged away
	public boolean shouldHighlight = false;
	public GridSquare(int x, int y){
		sprite = null;
		rect = new Rectangle(x, y, width+Helper.epsilon, height+Helper.epsilon);
	}
	public GridSquare(int x, int y, String s)//passing in coordinates and the string representation [PS]
	{
		String contents = s.substring(s.indexOf('[')+1, s.indexOf(']'));
		rect = new Rectangle(x, y, width+Helper.epsilon, height+Helper.epsilon);
		if(contents.contains("p"))
		{
			addPlatform();
		}
		if(contents.contains("u"))
		{
			addUnplaceablePlatform();
		}
		if(contents.contains("j"))
		{
			addSprite(new JumpPad());
		}
		if(contents.contains("l"))
		{
			addSprite(new Turn(true));
		}
		if(contents.contains("r"))
		{
			addSprite(new Turn(false));
		}
		if(contents.contains("b"))
		{
			addSprite(new SpeedBoost());
		}
		if(contents.contains("k"))
		{
			addSprite(new Key());
		}
		if(contents.contains("f"))
		{
			addSprite(new Finish(true));
		}
		if(contents.contains("g"))
		{
			addSprite(new Finish(false));
		}
		if(contents.contains("s"))
		{
			addSprite(new Spike());
		}
		if(contents.contains("S"))
		{
			startLocation = true;
		}
	}
	public GridSquare(GameSprite mySprite, int x, int y){
		rect = new Rectangle(x, y, width+Helper.epsilon, height+Helper.epsilon);
		sprite = mySprite;
		mySprite.setX(rect.x);
		mySprite.setY(rect.y);
	}
	public Vector2 center()
	{
		return new Vector2(rect.x+rect.width/2,rect.y+rect.height/2);
	}
	public GameSprite getSprite()
	{
		return sprite;
	}
	public void draw(SpriteBatch batch)//draws its local sprite batch
	{
		if(isOffScreen())
			return;
		if(hasPlatform())
			platform.draw(batch);
		if(hasSprite())
			sprite.draw(batch);
		if(shouldHighlight)
		{
			batch.draw(highlight,rect.x,rect.y);
		}
		if(DraggableSprite.shouldShowOutline(this)&&!isOffScreen())
		{
			batch.draw(outline, rect.x, rect.y);
		}
		if(userSprite&&!LevelScreen.isRunning)
		{
			batch.draw(userspriteoutline,rect.x,rect.y);
		}
	}
	public void highlight()
	{
		shouldHighlight = true;
	}
	public void unhighlight()
	{
		shouldHighlight = false;
	}
	public void addSprite(GameSprite NewSprite)//adds a sprite
	{
		if(sprite!=null)
		{
			return;//DOES NOT REPLACE SPRITES (AVOID ACCIDNETAL REPLACES)
		}
		sprite = NewSprite;
		sprite.setX(rect.x);
		sprite.setY(rect.y);
	}
	public void addSprite(GameSprite NewSprite, int translateX, int translateY)//translate x and translate y should be less than rect width
	{
		assert translateX<=width&&translateY<=height;
		if(sprite!=null)
		{
			return;
		}
		sprite = NewSprite;
		sprite.setX(rect.x+translateX);
		sprite.setY(rect.y+translateY);
	}
	public void addPlatform()
	{
		platform = new Platform();
		platform.setX(rect.x);
		platform.setY(rect.y);
	}
	public void addUnplaceablePlatform()
	{
		platform = new Platform(false);
		platform.setX(rect.x);
		platform.setY(rect.y);
	}
	public void addUserPlatform()
	{
		addPlatform();
		userPlatform = true;
	}
	public void addUserSprite(GameSprite NewSprite, int translateX, int translateY)//translate x and translate y should be less than rect width
	{
		addSprite(NewSprite,translateX,translateY);
		userSprite = true;
	}
	public void addUserSprite(GameSprite NewSprite)
	{
		addSprite(NewSprite);
		userSprite = true;
	}
	public void removePlatform()
	{
		userPlatform = false;
		platform = null;
	}
	public void replaceSprite(GameSprite NewSprite)//replaces sprites
	{
		sprite = NewSprite;
		sprite.setX(rect.x);
		sprite.setY(rect.y);
	}
	public void removeSprite()//remove sprites
	{
		userSprite = false;
		sprite = null;
	}
	public boolean hasSprite()//if there is already a sprite living the grid square
	{
		if(sprite != null)
		{
			return true;
		}
		return false;
	}
	public boolean hasPlatform()
	{
		if(platform!=null)
		{
			return true;
		}
		return false;
	}
	public boolean hasPlaceablePlatform()
	{
		return hasPlatform()&&platform.isPlaceable;
	}
	public boolean hasFinish()
	{
		return hasSprite() &&sprite instanceof Finish;
	}
	public boolean hasOpenFinish()
	{
		return hasSprite()&&sprite instanceof Finish && ((Finish)sprite).isOpen;
	}
	public boolean hasClosedFinish()
	{
		return hasSprite()&&sprite instanceof Finish && !((Finish)sprite).isOpen;
	}
	public boolean hasKey()
	{
		return hasSprite()&&sprite instanceof Key;
	}
	public boolean containsPoint(int x, int y)
	{
		return rect.contains(x, y);
	}
	public boolean hasJumpPad()
	{
		if(sprite!= null && sprite instanceof JumpPad)
		{
			return true;
		}
		return false;
	}
	public boolean hasSpeedBoost()
	{
		return sprite!=null &&sprite instanceof SpeedBoost;
	}
	public boolean hasTurn()
	{
		return sprite!= null && sprite instanceof Turn;
	}
	public boolean hasLeftTurn()
	{
		if(hasTurn())
		{
			Turn turn = (Turn)sprite;
			return turn.left;
		}
		return false;
	}
	public boolean hasRightTurn()
	{
		if(hasTurn())
		{
			Turn turn = (Turn)sprite;
			return !turn.left;
		}
		return false;
	}
	public boolean hasSpike()
	{
		return hasSprite()&&sprite instanceof Spike;
	}
	public boolean isOffScreen()
	{
		if(rect.x+rect.width<0+AndroidGame.camera.position.x-AndroidGame.w/2||rect.x>AndroidGame.w+AndroidGame.camera.position.x-AndroidGame.w/2||rect.y+rect.height<0+AndroidGame.camera.position.y-AndroidGame.h/2||rect.y>AndroidGame.h+AndroidGame.camera.position.y-AndroidGame.h/2)
		{
			return true;
		}
		return false;
	}
	public boolean isTouched()
	{
		if(Gdx.input.isTouched())
		{
			return rect.contains(Helper.PointerX(), Helper.PointerY());
		}
		return false;
	}
	public boolean isAlmostTouched()
	{
		if(Gdx.input.isTouched()&&Helper.PointerX()<AndroidGame.w-SpriteMenu.width+AndroidGame.camera.position.x-AndroidGame.w/2-width)
		{
			Rectangle rectangle = new Rectangle(rect);
			rectangle.x-=10;
			rectangle.y-=10;
			rectangle.width+=20;
			rectangle.height+=20;
			return rectangle.contains(Helper.PointerX(), Helper.PointerY());
		}
		return false;
	}
	public void update()
	{
		if(AndroidGame.levelEditingMode)
		{
			if(rect.contains(Helper.PointerX(), Helper.PointerY()))//this method handles pressing keys to spawn objects in level editor mode
			{
				if(Gdx.input.isKeyPressed(Input.Keys.C))
				{
					removeSprite();
					removePlatform();
					startLocation = false;
				}
				if(Gdx.input.isKeyPressed(Input.Keys.P)&&!hasPlatform())
				{
					addPlatform();
				}
				if(Gdx.input.isKeyPressed(Input.Keys.U)&&!hasPlatform())
				{
					addUnplaceablePlatform();
				}
				if(Gdx.input.isKeyPressed(Input.Keys.J)&&hasPlatform()&&!hasSprite())
				{
					addSprite(new JumpPad());
				}
				if(Gdx.input.isKeyPressed(Input.Keys.F)&&!hasSprite())
				{
					addSprite(new Finish(true));
				}
				if(Gdx.input.isKeyPressed(Input.Keys.G)&&!hasSprite())
				{
					addSprite(new Finish(false));
				}
				if(Gdx.input.isKeyPressed(Input.Keys.K)&&!hasSprite())
				{
					addSprite(new Key());
				}
				if(Gdx.input.isKeyPressed(Input.Keys.S)&&hasPlatform()&&!hasSprite())
				{
					addSprite(new Spike());
				}
				if(Gdx.input.isKeyPressed(Input.Keys.B)&&hasPlatform()&&!hasSprite())
				{
					addSprite(new SpeedBoost());
				}
				if(Gdx.input.isKeyPressed(Input.Keys.SPACE)&&hasPlatform())
				{
					addSprite(new GameSprite("data/Player.png"));
					startLocation = true;
				}
			}
		}
		else if (!LevelScreen.isRunning)
		{
			if(userSprite&&isAlmostTouched()&&!DraggableSprite.dragged)//if nothing is being dragged and the sprite can be removed
			{
				DraggableSprite d = new DraggableSprite(getType(), rect.x, rect.y, this);
				SpriteMenu.newDrag(d);
				removeSprite();
			}
		}
		if(hasSpeedBoost())
		{
			sprite.update();
		}
		if(hasJumpPad())
		{
			sprite.update();
		}
		if(hasTurn())
		{
			sprite.update();
		}
		if(hasFinish())
		{
			sprite.update();
		}
	}
	public String toString()//constructing the squares string for saving purposes based on the objects within
	{
		String s = "[";
		if(hasPlatform())
		{
			if(hasPlaceablePlatform())
			{
				s+="p";
			}
			else
			{
				s+="u";//u represents unplaceableplatform
			}
		}
		if(hasJumpPad())
		{
			s+="j";
		}
		if(hasSpeedBoost())
		{
			s+="b";
		}
		if(hasLeftTurn())
		{
			s+="l";
		}
		if(hasRightTurn())
		{
			s+="r";
		}
		if(hasKey())
		{
			s+="k";
		}
		if(startLocation)
		{
			s+="S";
		}
		if(hasOpenFinish())
		{
			s+="f";
		}
		if(hasClosedFinish())
		{
			s+="g";
		}
		if(hasSpike())
		{
			s+="s";
		}
		s+="]";
		return s;
	}
	public SpriteButton.spriteType getType()//get's type for moving objects
	{
		if(hasJumpPad())
		{
			return SpriteButton.spriteType.Jumppad;
		}
		else if(hasLeftTurn())
		{
			return SpriteButton.spriteType.TurnLeft;
		}
		else if(hasRightTurn())
		{
			return SpriteButton.spriteType.TurnRight;
		}
		else if(hasSpeedBoost())
		{
			return SpriteButton.spriteType.SpeedBoost;
		}
		return SpriteButton.spriteType.Platform;
	}
}
