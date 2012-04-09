package com.DungeonCrawl;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;

import com.DungeonCrawl.AreaEffects.SimpleAreaEffect.Effect;
import com.DungeonCrawl.Collisions.DoNothingCollision;
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.FlyStraightStep;


import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Walls {
	
	public float i_tunnelSpeed=2;
	Random r = new Random();
	
	public float f_maximumBlockWidth;
	
	//cyclical variable that goes from 0 to 8, use Math.abs(i_currentMiddleWidthCalculator-4) to get width
	int i_currentMiddleWidth=0;
	
	int i_currentTunnelWidthLeft;
	int i_currentTunnelWidthRight;
	int i_previousTunnelWidthLeft = 0;
	int i_previousTunnelWidthRight = 0;
	private int i_previousMidTunnelWidth;
	
	private int i_blockSize=32;
	int i_nextBlockToUse=0;
	int i_maxBlocks = 300;
	ArrayList<GameObject> wallSections = new ArrayList<GameObject>();
	
	//opptomisation approach
	public Walls()
	{
		f_maximumBlockWidth = LogicEngine.SCREEN_WIDTH /2;
		for(int i=0;i<i_maxBlocks;i++)
		{
			GameObject block = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png", Integer.MIN_VALUE,LogicEngine.SCREEN_HEIGHT+(i_blockSize/2),0);
			block.v.setMaxForce(1000f);
			block.v.setMaxVel(1000f);
			wallSections.add(block);
		}
	}
	
	private void spawnBlockReusable(LogicEngine in_logicEngine, int in_row,int in_column,float in_x, float in_tunnelSpeed)
	{
		GameObject block =  wallSections.get(i_nextBlockToUse);
		
		in_logicEngine.objectsObstacles.remove(block);
			
		//set x
		block.v.setX(in_x);
		
		//set y
		block.v.setY(LogicEngine.SCREEN_HEIGHT+(i_blockSize/2));
		
		block.i_animationFrameSizeWidth = i_blockSize;
		block.i_animationFrameSizeHeight = i_blockSize;
		
		block.f_forceScaleX = 1 + (GameRenderer.f_pixelAdjustX-GameRenderer.dpiTextureCoordinatesAdjust);
		block.f_forceScaleY = 1 + (GameRenderer.f_pixelAdjustY-GameRenderer.dpiTextureCoordinatesAdjust);
		
		
		block.i_animationFrame = in_row;
		block.i_animationFrameRow = in_column;
	
		block.allegiance = GameObject.ALLEGIANCES.LETHAL;
		
		
		
		block.str_name = "wall";
		
		block.collisionHandler = new DoNothingCollision(block,i_blockSize/2);
		block.stepHandlers.clear();
		block.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1*i_tunnelSpeed)));
		
		
		

		in_logicEngine.objectsObstaclesLock.writeLock().lock();
		
		in_logicEngine.objectsObstacles.add(block);
		
		in_logicEngine.objectsObstaclesLock.writeLock().unlock();
		
		i_nextBlockToUse = (i_nextBlockToUse+1)%i_maxBlocks;
		
		
	}
	
	
	
	public int getMiddleOfTunnel()
	{
		int i_return =  (i_blockSize*i_currentTunnelWidthLeft) ;
		i_return += (LogicEngine.SCREEN_WIDTH - ((i_blockSize*i_currentTunnelWidthLeft) + (i_blockSize*i_currentTunnelWidthRight)))/2;

		return i_return;
	}
	
	private void speedUpBlocks(LogicEngine in_logicEngine, float in_newSpeed)
	{
		
		//speed up obstacles
		for(int i=0;i<in_logicEngine.objectsObstacles.size();i++)
			if(in_logicEngine.objectsObstacles.get(i).str_name.equalsIgnoreCase("wall"))
			{
				((FlyStraightStep)in_logicEngine.objectsObstacles.get(i).stepHandlers.get(0)).setXY(0, -in_newSpeed);
			}
	}
	
	private void spawnBlock(LogicEngine in_logicEngine, int in_row,int in_column,float in_x, float in_tunnelSpeed)
	{
		spawnBlockReusable(in_logicEngine, in_row, in_column, in_x, in_tunnelSpeed);
		return;
		/*
		
		
		GameObject block = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png", in_x,LogicEngine.SCREEN_HEIGHT+(i_blockSize/2),0);
				
		block.i_animationFrameSizeWidth = i_blockSize;
		block.i_animationFrameSizeHeight = i_blockSize;
		
		
		block.i_animationFrame = in_row;
		block.i_animationFrameRow = in_column;
	
		block.allegiance = GameObject.ALLEGIANCES.LETHAL;
		
		block.v.setMaxForce(2.0f);
		block.v.setMaxVel(2.0f);
		
		block.str_name = "wall";
		
		//scroll down
		block.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1f * in_tunnelSpeed)));

		//if corner block
		if(in_row >2 )
			block.collisionHandler = new DoNothingCollision(i_blockSize/3);
		else
			//destroy ships that get too close
			block.collisionHandler = new DoNothingCollision(i_blockSize/2);
		
		in_logicEngine.objectsObstacles.add(block);

*/
	
	}
	
	private void spawnMiddle(LogicEngine in_logicEngine, int in_WallWidth, float in_tunnelSpeed)
	{
		float f_middle = LogicEngine.SCREEN_WIDTH/2;
		
		//dont summon any if theres no wall to draw
		if(in_WallWidth == 0 && i_previousMidTunnelWidth ==0) 
			return;
		
		for(int i=0  ; i< in_WallWidth-1 ;i++)
		{
			//spawn left wall
			int i_randomBlockX = 1;// + r.nextInt(1);
			int i_randomBlockY = 5 + r.nextInt(3);
			
			
			float f_currentBlock = f_middle - i;
			
			//draw right block
			spawnBlock(in_logicEngine, i_randomBlockX, i_randomBlockY,f_middle + (i_blockSize/2)+ (i_blockSize*i), in_tunnelSpeed);
			spawnBlock(in_logicEngine, i_randomBlockX, i_randomBlockY,f_middle - (i_blockSize/2) -(i_blockSize*i), in_tunnelSpeed);
		}
		
		//spawn edge block
		if(in_WallWidth > i_previousMidTunnelWidth)
		{
			spawnBlock(in_logicEngine, 2, 4,f_middle - (i_blockSize/2) -(i_blockSize*(in_WallWidth-1)), in_tunnelSpeed);
			spawnBlock(in_logicEngine, 1, 4,f_middle + (i_blockSize/2) +(i_blockSize*(in_WallWidth-1)), in_tunnelSpeed);
		//	spawnBlock(in_logicEngine, i_randomBlockX, i_randomBlockY,f_middle - ((i_blockSize/2)*i), in_tunnelSpeed);
			
			i_previousMidTunnelWidth = in_WallWidth;
		}
		else if(in_WallWidth == i_previousMidTunnelWidth)
		{
			//spawn left blocks
			spawnBlock(in_logicEngine, 0, 4+r.nextInt(4),f_middle +(i_blockSize/2) + (i_blockSize*(in_WallWidth-1)), in_tunnelSpeed);
			//spawn right blocks
			spawnBlock(in_logicEngine, 3, 4+r.nextInt(4),f_middle - (i_blockSize/2)-(i_blockSize*(in_WallWidth-1)), in_tunnelSpeed);
		}
		else if(in_WallWidth < i_previousMidTunnelWidth)
		{
			//right
			spawnBlock(in_logicEngine, 4, 4,f_middle + (i_blockSize/2) +(i_blockSize*(in_WallWidth)), in_tunnelSpeed);
			//left
			spawnBlock(in_logicEngine, 5, 4,f_middle - (i_blockSize/2) -(i_blockSize*(in_WallWidth)), in_tunnelSpeed);
			
			if(in_WallWidth !=0)
			{
				//spawn right fill in block
				spawnBlock(in_logicEngine, 1, 5+r.nextInt(3),f_middle +(i_blockSize/2) + (i_blockSize*(in_WallWidth-1)), in_tunnelSpeed);
				//spawn left fill in block
				spawnBlock(in_logicEngine, 1, 5+r.nextInt(3),f_middle - (i_blockSize/2)-(i_blockSize*(in_WallWidth-1)), in_tunnelSpeed);
			}
			
			i_previousMidTunnelWidth = in_WallWidth;
		}
		
	}
	
	private void spawnTunnel(LogicEngine in_logicEngine, int in_leftWallWidth, int in_rightWallWidth, float i_tunnelSpeed2)
	{
		//wall blocks start at (i_blockSize/2) x 144 y
	
		
		//spawn blocks
		for(int i=0 ; i< in_leftWallWidth-1 ;i++)
		{
			//spawn left wall
			int i_randomBlockX = 1;// + r.nextInt(1);
			int i_randomBlockY = 5 + r.nextInt(3);
			
			spawnBlock(in_logicEngine, i_randomBlockX, i_randomBlockY,i_blockSize*i, i_tunnelSpeed2);
			
		}
		//spawn blocks
		for(int i=0 ; i< in_rightWallWidth-1 ;i++)
		{
			//spawn left wall
			int i_randomBlockX = 1;// + r.nextInt(1);
			int i_randomBlockY = 5 + r.nextInt(3);
			spawnBlock(in_logicEngine, i_randomBlockX, i_randomBlockY,LogicEngine.SCREEN_WIDTH - i_blockSize*i, i_tunnelSpeed2);
		}
		
		
		//spawn edge block
		if(in_leftWallWidth > i_previousTunnelWidthLeft)
		{
			spawnBlock(in_logicEngine, 1, 4,(i_blockSize*i_previousTunnelWidthLeft), i_tunnelSpeed2);
			
			i_previousTunnelWidthLeft = in_leftWallWidth;
		}
		else if(in_leftWallWidth == i_previousTunnelWidthLeft)
		{
			if(b_skipSpawningWallLeft)
				b_skipSpawningWallLeft=false;
			else
				spawnBlock(in_logicEngine, 0, 4+r.nextInt(4),(i_blockSize*(i_previousTunnelWidthLeft-1)), i_tunnelSpeed2);
			
			
		}
		else if(in_leftWallWidth < i_previousTunnelWidthLeft)
		{
			spawnBlock(in_logicEngine, 4, 4,(i_blockSize*(in_leftWallWidth)), i_tunnelSpeed2);
			spawnBlock(in_logicEngine, 1, 5,(i_blockSize*(in_leftWallWidth-1)), i_tunnelSpeed2);
			
			
			i_previousTunnelWidthLeft = in_leftWallWidth;
		}
		
		if(in_rightWallWidth > i_previousTunnelWidthRight)
		{
			spawnBlock(in_logicEngine, 2, 4, LogicEngine.SCREEN_WIDTH - (i_blockSize*i_previousTunnelWidthRight), i_tunnelSpeed2);
			i_previousTunnelWidthRight = in_rightWallWidth;
		}
		else if(in_rightWallWidth == i_previousTunnelWidthRight)
		{
			if(b_skipSpawningWallRight)
				b_skipSpawningWallRight = false;
			
			spawnBlock(in_logicEngine, 3, 4+r.nextInt(4),LogicEngine.SCREEN_WIDTH - (i_blockSize*(in_rightWallWidth-1)), i_tunnelSpeed2);
		}
		else if(in_rightWallWidth < i_previousTunnelWidthRight)
		{
			spawnBlock(in_logicEngine, 1,5,LogicEngine.SCREEN_WIDTH - (i_blockSize*(in_rightWallWidth-1)), i_tunnelSpeed2);
			spawnBlock(in_logicEngine, 5, 4, LogicEngine.SCREEN_WIDTH - (i_blockSize*(in_rightWallWidth)), i_tunnelSpeed2);
			i_previousTunnelWidthRight = in_rightWallWidth;
		}
		
	
	}

	public void narrowTunnel() {
		
		//ensure minimum tunnel width
		if((i_blockSize*i_currentTunnelWidthLeft) + (i_blockSize*i_currentTunnelWidthRight) < f_maximumBlockWidth)
		{
			i_currentTunnelWidthLeft++;
			i_currentTunnelWidthRight++;
		}
		
		
	}

	public void setBlockSpeed(LogicEngine in_logicEngine, int i,int in_stepCounter) {
		
		
		i_tunnelSpeed=i;
		speedUpBlocks(in_logicEngine,i_tunnelSpeed);
		
		spawnTunnel(in_logicEngine,i_currentTunnelWidthLeft,i_currentTunnelWidthRight,in_stepCounter);
		
	}

	public void moveTunnelLeft() {
		
		//ensure we dont tunnel off the screen
		if(i_currentTunnelWidthLeft >=2)
		{
			i_currentTunnelWidthLeft--;
			i_currentTunnelWidthRight++;
		}
		
	}

	public void moveTunnelRight() {
		if(i_currentTunnelWidthRight >=2)
		{
			i_currentTunnelWidthLeft++;
			i_currentTunnelWidthRight--;
		}
		
	}

	public void openTunnel() {

		i_currentTunnelWidthLeft = Math.max(0,i_currentTunnelWidthLeft-1);
		i_currentTunnelWidthRight = Math.max(0,i_currentTunnelWidthRight-1);
	}

	public void spawnBlockIfNeeded(LogicEngine in_logicEngine, int i_stepCounter) {

		
		
		if(i_currentTunnelWidthLeft <=0 && i_currentTunnelWidthRight <=0 )
			return;
		
		//every (i_blockSize/2) spawn new walls
		if(i_stepCounter%(i_blockSize/i_tunnelSpeed) == 0)
			spawnTunnel(in_logicEngine, i_currentTunnelWidthLeft,i_currentTunnelWidthRight,i_tunnelSpeed);
		
		
	}

	public void spawnBlockMiddleIfNeeded(LogicEngine in_logicEngine,
			int i_stepCounter) {
		
		if(i_stepCounter%(i_blockSize/i_tunnelSpeed) == 0)
			spawnMiddle(in_logicEngine,i_currentMiddleWidth, i_tunnelSpeed);
		
	}

	public int getMiddleWidth()
	{
		return i_currentMiddleWidth;
	}
	
	public void widenMiddle() {
		i_currentMiddleWidth++;
		
	}
	
	public void narrowMiddle() {
		i_currentMiddleWidth = Math.max(0,i_currentMiddleWidth-1);
		
	}

	
	boolean b_skipSpawningWallLeft = false;
	boolean b_skipSpawningWallRight = false;
	
	public GameObject spawnShooterBlock(LogicEngine in_logicEngine) 
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",(i_currentTunnelWidthLeft-1)*i_blockSize,LogicEngine.SCREEN_HEIGHT,1);
		ship.i_animationFrameSizeWidth = 48;
		ship.i_animationFrameSizeHeight = 32;
		ship.i_animationFrame = 3;
		ship.i_animationFrameRow = 5;
		ship.allegiance = GameObject.ALLEGIANCES.LETHAL;

		
		ship.f_forceScaleX = 1 + (GameRenderer.f_pixelAdjustX-GameRenderer.dpiTextureCoordinatesAdjust);
		ship.f_forceScaleY = 1 + (GameRenderer.f_pixelAdjustY-GameRenderer.dpiTextureCoordinatesAdjust);
		
		
		//set y
		ship.v.setY(LogicEngine.SCREEN_HEIGHT+(i_blockSize/2));
		ship.v.setX((i_blockSize*(i_previousTunnelWidthLeft-1)));
		
		ship.str_name = "wall";
		
		//scroll down
		ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1*i_tunnelSpeed)));
		
		ship.v.setMaxForce(1000);
		ship.v.setMaxVel(1000);
		ship.shootEverySteps = 1;
		
		BeamShot b = new BeamShot(25);
		
		b.b_direction=Utils.Direction.EAST;
		b.b_flare = false;
		b.ae_beam.effect = Effect.KILL_NON_OBSTACLES;
		
		ship.shotHandler = b; 
		
		ship.v.setMaxVel(1000);
		
		in_logicEngine.objectsObstacles.add(ship);
		b_skipSpawningWallLeft = true;
		
		return ship;
				
		
	}

	public GameObject spawnWeakShooterBlock(LogicEngine in_logicEngine) 
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",LogicEngine.SCREEN_WIDTH-(i_currentTunnelWidthRight)*i_blockSize,LogicEngine.SCREEN_HEIGHT,1);
		ship.i_animationFrameSizeWidth = 48;
		ship.i_animationFrameSizeHeight = 32;
		ship.i_animationFrame = 3;
		ship.i_animationFrameRow = 6;
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		ship.v.setMaxForce(2.0f);
		ship.v.setMaxVel(2.0f);
		
		ship.f_forceScaleX = 1 + (GameRenderer.f_pixelAdjustX-GameRenderer.dpiTextureCoordinatesAdjust);
		ship.f_forceScaleY = 1 + (GameRenderer.f_pixelAdjustY-GameRenderer.dpiTextureCoordinatesAdjust);
		
		
		//set y
		ship.v.setY(LogicEngine.SCREEN_HEIGHT+(i_blockSize/2));
		ship.v.setX(LogicEngine.SCREEN_WIDTH-(i_blockSize*(i_previousTunnelWidthRight-1)));
		
		ship.str_name = "wall";
		
		//scroll down
		ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1*i_tunnelSpeed)));
		
		ship.shootEverySteps = 30;
		
		if(Difficulty.isMedium())
			ship.shootEverySteps = 20;
		
		if(Difficulty.isHard())
			ship.shootEverySteps = 15;
		
		ship.shotHandler = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/redbullets.png", 6, new Vector2d(-4,-1*i_tunnelSpeed));
		ship.v.setMaxVel(1000);
		
		
		in_logicEngine.objectsObstacles.add(ship);
		b_skipSpawningWallRight = true;
		
		return ship;
				
		
	}
	public void setColour(Color in_color) {
		
		for(int i=0;i<i_maxBlocks;i++)
		{
			wallSections.get(i).c_Color = in_color;
		}
	}

	public void makeHarmless() {
		for(int i=0 ; i< wallSections.size();i++)
		{
			wallSections.get(i).collisionHandler = null;
			wallSections.get(i).allegiance = GameObject.ALLEGIANCES.NONE;	
		}
		
	}	
	
}
