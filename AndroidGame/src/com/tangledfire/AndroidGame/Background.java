package com.tangledfire.AndroidGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * @author Nikhil Khanna
 * The backgrounds used in the game
 */
import java.util.Random;
public class Background{
	TextureAtlas myAtlas;
	String myName;
	GameSprite[][] tiles;
	public int totalWidth;
	public int totalHeight;
	public final int sideLength = 400;
	Random rand;
	public Background(int levelNum, Grid grid)
	{
		rand = new Random();
		if(levelNum<16)
		{
			myAtlas = AndroidGame.woodBackgrounds;
		}
		else
		{
			myAtlas = AndroidGame.metalBackgrounds;
		}
		//name is in format Wood-(put your number here)
		if(myAtlas.equals(AndroidGame.woodBackgrounds))
		{
			myName = "Wood-";
		}
		else
		{
			myName = "metal-";
		}
		totalHeight = grid.height*GridSquare.height;
		totalWidth = grid.width*GridSquare.width;
		tiles = new GameSprite[(totalWidth/sideLength)+1][(totalHeight/sideLength)+1];
		TextureRegion atlasRegion;
		for(int i = 0; i<tiles.length;i++)
		{
			for(int j = 0; j<tiles[0].length;j++)
			{
				int fileNumber = 1;
				if(myName.equals("Wood-"))
				{
				fileNumber = rand.nextInt(2)+1;
				}
				else
				{
					fileNumber = rand.nextInt(2)+1;
				}
				int currentTexWidth = sideLength;
				int currentTexHeight = sideLength;
				if(i==tiles.length-1)
				{
					currentTexWidth = totalWidth%sideLength;
				}
				if(j==tiles[0].length-1)
				{
					currentTexHeight = totalHeight%sideLength;
				}
				atlasRegion = myAtlas.findRegion(myName+fileNumber);
				atlasRegion.setRegion(atlasRegion.getRegionX(), atlasRegion.getRegionY(), currentTexWidth, currentTexHeight);
				tiles[i][j] = new GameSprite(atlasRegion, i*sideLength, j*sideLength);
				atlasRegion.setRegion(atlasRegion.getRegionX(), atlasRegion.getRegionY(), sideLength, sideLength);
			}
		}
	}
	public void draw(SpriteBatch spriteBatch)
	{
		for(int i = 0; i<tiles.length;i++)
		{
			for(int j= 0; j<tiles[0].length;j++)
			{
				tiles[i][j].draw(spriteBatch);
			}
		}
	}
}
