package com.tangledfire.AndroidGame;

import java.io.*;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Grid {
public GridSquare[][] squares;
public final int width;//width of grid in grid squares
public final int height;//height of grid in grid squares
public static final String levelToEdit = AndroidGame.levelToEdit;
public final Rectangle size;
	public Grid(int MyWidth, int MyHeight){//called when making a completely new level
			width = MyWidth;
			height = MyHeight;
			size = new Rectangle(0, 0, width*GridSquare.width, height*GridSquare.height);
			squares = new GridSquare[width][height];
			for(int i = 0; i<width;i++)
			{
				for(int j =0; j<height;j++)
				{
					squares[i][j] = new GridSquare(i*GridSquare.width, j*GridSquare.height);
				}
			}
	}
	public Grid(String path)//only the level fileName
	{
		FileHandle levelFile;
		if(Gdx.app.getType()==ApplicationType.Desktop)
		{
			levelFile = Gdx.files.external("Levels/"+path);
		}
		else
		{
			levelFile = Gdx.files.internal("Levels/"+path);
		}
		String s = levelFile.readString();
		int lineLength = 0;
		String[] lines = s.split("\n");//splits the lines on line break
		width = lines.length;
		for(int i = 0; i<lines.length;i++)
		{
			String[] boxes = lines[i].split(" ");//splits on spaces
			lineLength = boxes.length;
			if(squares==null)
				squares = new GridSquare[width][lineLength];
			for(int j = 0; j<boxes.length;j++)
			{
				squares[i][j] = new GridSquare(i*GridSquare.width, j*GridSquare.height, boxes[j]);
			} 
		}
		height = lineLength;
		size = new Rectangle(0,0,width*GridSquare.width,height*GridSquare.height);
	}
	public void makeLevel()//makes the grid a level
	{
		FileHandle file = Gdx.files.external("Levels/"+levelToEdit);
		file.writeString("", false);
		for(int i = 0; i <width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				file.writeString(squares[i][j].toString()+" ", true);
			}
			file.writeString("\n", true);
		}
	}
	public void draw(SpriteBatch batch)//draws every single square in the batch
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				squares[i][j].draw(batch);
			}
		}
	}
	public void update()
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				squares[i][j].update();
			}
		}
	}
	public void addSquares()//adds sprites on touch (test purposes for now)
	{
			int touchX = (int)Helper.PointerX();
			int touchY = (int)Helper.PointerY();
			GridSquare touchedSquare = getSquare(touchX,touchY);
			touchedSquare.addPlatform();
			touchedSquare.addSprite(new JumpPad(),25,0);
	}
	public GridSquare getSquare(int xCoordinate, int yCoordinate)//returns the square that contains point if point is out of bounds 
	{
		assert(contains(xCoordinate,yCoordinate));
		for(int i = 0;i<width;i++)
		{
			for(int j= 0; j<height;j++)
			{
				if(squares[i][j].containsPoint(xCoordinate, yCoordinate))
				{
					return squares[i][j];
				}
			}
		}
		return null;//should never happen
	}
	public boolean isTouched()//checks if the grid is touched
	{
		if(Gdx.input.isTouched()&&!SpriteMenu.isMenuTouched())
		{
			return size.contains(Helper.PointerX(), Helper.PointerY());
		}
		return false;
	}
	public boolean contains(int x, int y)
	{
		return size.contains(x, y);
	}
	public Array<GameSprite> getSprites()
	{
		Array<GameSprite> sprites = new Array<GameSprite>();
		for(int i = 0; i<width;i++)
		{
			for(int j= 0; j<height;j++)
			{
				if(squares[i][j].hasSprite())
				{
					sprites.add(squares[i][j].sprite);
				}
			}
		}
		return sprites;
	}
	public Array<Platform> getPlatforms()//gets all platforms within the grid
	{
		Array<Platform> platforms = new Array<Platform>();
		for(int i = 0;i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasPlatform())
				{
					platforms.add(squares[i][j].platform);
				}
			}
		}
		return platforms;
	}
	public Array<JumpPad> getJumpPads()
	{
		Array<JumpPad> pads = new Array<JumpPad>();
		for(int i = 0;i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasJumpPad())
				{
					pads.add((JumpPad)squares[i][j].sprite);
				}
			}
		}
		return pads;
	}
	public Array<SpeedBoost> getSpeedBoosts()
	{
		Array<SpeedBoost> boosts = new Array<SpeedBoost>();
		for(int i = 0; i<width; i++)
		{
			for(int j = 0; j<height; j++)
			{
				if(squares[i][j].hasSpeedBoost())
				{
					boosts.add((SpeedBoost)squares[i][j].sprite);
				}
			}
		}
		return boosts;
	}
	public Array<Turn> getTurns()
	{
		Array<Turn> turns = new Array<Turn>();
		for(int i = 0;i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasTurn())
				{
					turns.add((Turn)squares[i][j].sprite);
				}
			}
		}
		return turns;
	}
	public Array<Spike> getSpikes()
	{
		Array<Spike> spikes = new Array<Spike>();
		for(int i = 0;i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasSpike())
				{
					spikes.add((Spike)squares[i][j].sprite);
				}
			}
		}
		return spikes;
	}
	public boolean hasKey()
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasKey())
					return true;
			}
		}
		return false;
	}
	public Key getKey()
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasKey())
				{
					return (Key)squares[i][j].sprite;
				}
			}
		}
		return null;
	}
	public Finish getFinish()
	{
		for(int i = 0;i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].hasFinish())
				{
					return (Finish)squares[i][j].sprite;
				}
			}
		}
		return null;//this should never happens EVERY LEVEL HAS A FINISH
	}
	public boolean contains(float x, float y) 
	{
		return size.contains(x, y);
	}
	public GridSquare startLocation()
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if (squares[i][j].startLocation)
				{
					return squares[i][j];
				}
			}
		}
		return squares[0][0];//if no posiiton is set default to 0,0
	}
	//highlights the horizontal and verticle lines taht intersect at the square
	public void highlightIntersection(GridSquare square)
	{
		Tuple coordinates = squarePosition(square);
		for(int j = 0; j<height;j++)
		{
			squares[coordinates.i][j].highlight();
		}
		for(int i = 0; i<width;i++)
		{
			squares[i][coordinates.j].highlight();
		}
	}
	public void unhighlightAll()
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				squares[i][j].unhighlight();
			}
		}
	}
	public Tuple squarePosition(GridSquare square)
	{
		for(int i = 0;i<width;i++)
		{
			for(int j= 0; j<height;j++)
			{
				if(squares[i][j].containsPoint((int)(square.rect.x+square.rect.width/2),(int)(square.rect.y+square.rect.height/2)))
				{
					return new Tuple(i, j);
				}
			}
		}
		return null;
	}
	public void clearUserObjects()
	{
		for(int i = 0; i<width;i++)
		{
			for(int j = 0; j<height;j++)
			{
				if(squares[i][j].userPlatform)
				{
					squares[i][j].removePlatform();
				}
				if(squares[i][j].userSprite)
				{
					squares[i][j].removeSprite();
				}
			}
		}
	}
}
