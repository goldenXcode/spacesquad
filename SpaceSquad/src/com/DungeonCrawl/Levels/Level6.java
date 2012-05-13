package com.DungeonCrawl.Levels;

import java.util.Random;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Walls;
import com.DungeonCrawl.Collisions.DoNothingCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Shooting.FragmentationProjectile;
import com.DungeonCrawl.Shooting.TurretShot;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.PullShipsStep;
import com.DungeonCrawl.Steps.WanderStep;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level6 extends BasicLevel{
	
	
	Random r = new Random();
	Walls myWalls = new Walls();
	private boolean b_widening = true;
	private int MID_GAP = 1;
	private int i_midGapCountdown = MID_GAP;
	
	
	public Level6()
	{
		
		BOSS_STEP = 1800;
		i_stepCounter = 0;
		i_levelNumber = 6;
		
		
	}

	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		//in_manager.spawnRandomPowerup(in_logicEngine, LogicEngine.SCREEN_WIDTH/2);
	
		if(i_stepCounter ==0 )
			in_logicEngine.background.setBackground(2);
		
		int i_middleOfTunnel = myWalls.getMiddleOfTunnel();
		
		
		if(i_stepCounter %500 ==0 && i_stepCounter<1800)
			in_manager.spawnRandomPowerup(in_logicEngine, i_middleOfTunnel);
		else
			if(i_stepCounter %500 == 0)
				in_manager.spawnRandomPowerup(in_logicEngine, (float) (Math.random()* LogicEngine.SCREEN_WIDTH));
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);

		if(i_stepCounter %50 ==0 && i_stepCounter<1800)
		{
			FlyStraightStep fs = new FlyStraightStep(new Vector2d(0,-5f));
			fs.setIsAccelleration(true);
			
			int i_rand = r.nextInt(4);
			
//			if(i_rand == 0)
	//			in_manager.spawnTurretShip(in_logicEngine, i_middleOfTunnel - 25);
			if(i_rand == 1)
			{
				in_manager.spawnBaterang(in_logicEngine, i_middleOfTunnel -50, LogicEngine.SCREEN_HEIGHT+10, fs);
				
				if(!Difficulty.isHard())
					in_manager.spawnBaterang(in_logicEngine, i_middleOfTunnel +50, LogicEngine.SCREEN_HEIGHT+10, fs);
			}
			if(i_rand == 2)
				in_manager.spawnSplitter(in_logicEngine);
			if(i_rand == 3)
			{
				in_manager.spawnSeeker(in_logicEngine, i_middleOfTunnel-50);
				in_manager.spawnSeeker(in_logicEngine, i_middleOfTunnel+50);
			}
		}
		//narrow until 200
		if(i_stepCounter < 200)
			//narrow cave
			if(i_stepCounter%32 == 0)
			{
				myWalls.narrowTunnel();
			}
			
		//increase speed
		if(i_stepCounter == 196)
		{
			myWalls.setBlockSpeed(in_logicEngine,4 ,i_stepCounter);
		}
		
		///////////////////////200 - 400 slow tunnel//////////////////////////
		if(i_stepCounter > 200 && i_stepCounter < 400)
		{
			
			if(i_stepCounter%64 == 0 )
			{
				myWalls.spawnShooterBlock(in_logicEngine);
				
			}
			else
			if(i_stepCounter > 300 && i_stepCounter < 400)
				if(i_stepCounter%32 == 0 )
					myWalls.spawnShooterBlock(in_logicEngine);
		}
		
		
		/////////////////////400 - 800 fast tunnel//////////////////////////
		//increase speed
		if(i_stepCounter == 516)
		{
			if(Difficulty.isHard())
				myWalls.setBlockSpeed(in_logicEngine, 16,i_stepCounter);
			if(Difficulty.isMedium() || Difficulty.isEasy())
				myWalls.setBlockSpeed(in_logicEngine, 8,i_stepCounter);
			
		}
		//		
		if(i_stepCounter > 400 && i_stepCounter < 1200)
		{
			

			//narrow cave
			if(i_stepCounter%8 == 0 )
			{
				if(r.nextInt(2)==0) 
				{
					myWalls.moveTunnelRight();	
				}
				else
				
				{
					myWalls.moveTunnelLeft();
				}
			}
			
		}
		
		/////////////////////800 - 1200 straight tunnel//////////////////////////
		//decrease speed
		if(i_stepCounter == 801)
		{
	//		myWalls.setBlockSpeed(in_logicEngine, 8);

		}
		//		
		if(i_stepCounter > 800 && i_stepCounter < 1200)
		{
			if(i_stepCounter%35 == 0)
			{
				int i_rand = r.nextInt(3);
				GameObject go_mine = null;
				
				if(i_rand==0)
					go_mine = in_manager.spawnMine(in_logicEngine, i_middleOfTunnel, LogicEngine.SCREEN_HEIGHT+10);
				if(i_rand==1)
					go_mine = in_manager.spawnMine(in_logicEngine, i_middleOfTunnel - 64, LogicEngine.SCREEN_HEIGHT+10);
				if(i_rand==2)
					go_mine = in_manager.spawnMine(in_logicEngine, i_middleOfTunnel +64, LogicEngine.SCREEN_HEIGHT+10);
				 
				((FlyStraightStep)go_mine.stepHandlers.get(0)).setXY(0, -myWalls.i_tunnelSpeed);
			}
		}
		
		/////////////////////1200 - 1400 open up tunnel//////////////////////////
		if(i_stepCounter > 1200 && i_stepCounter < 1400)
		{
			if(i_stepCounter%64 == 0 )
			{
				myWalls.openTunnel();
			}
		}
		
		/////////////////////1400 - 1800 spawn something interesting//////////////////////////
		
		/////////////////////1800 - BOSS Midsection Rock//////////////////////////
		//increase speed
		if(i_stepCounter == 1801)
		{
			if(Difficulty.isHard())
			{
				myWalls.setBlockSpeed(in_logicEngine, 16,i_stepCounter);
				MID_GAP = -1;
			}
			else
			if(Difficulty.isMedium())
			{
				myWalls.setBlockSpeed(in_logicEngine, 16,i_stepCounter);
				MID_GAP = 0;
			}
			else
			myWalls.setBlockSpeed(in_logicEngine, 8,i_stepCounter);
		}
		
		if(i_stepCounter > 1800)
		{
			
			if(i_stepCounter %8 == 0)
				if(b_widening)
				{
					if(i_midGapCountdown < 0)
						myWalls.widenMiddle();
					else
						i_midGapCountdown--;
					
					
					if(myWalls.getMiddleWidth() == 1)
					{
						b_widening=false;
						i_midGapCountdown=MID_GAP;
					}
				}
				else
				{			
					if(i_midGapCountdown < 0)
						myWalls.narrowMiddle();
					else
						i_midGapCountdown--;
					
					
					if(myWalls.getMiddleWidth() == 0)
					{
						i_midGapCountdown=MID_GAP;
						b_widening=true;
					}
				}
				
			
			myWalls.spawnBlockMiddleIfNeeded(in_logicEngine, i_stepCounter);
			
		
		}
		else
		myWalls.spawnBlockIfNeeded(in_logicEngine,i_stepCounter);
			
		if(i_stepCounter == BOSS_STEP)
			spawnBoss(in_logicEngine);
		if(boss != null)
			stepBoss(in_manager, in_logicEngine);
		
		/*
		//spawn boulders
		if(i_stepCounter%10==0)
			in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
		*/
		
		
		return super.stepLevel(in_manager, in_logicEngine);
	}
	
	int i_bossAnimationFrame = 0;
	int i_bossStepCounter=0;
	
	private void stepBoss(LevelManager in_manager, LogicEngine in_logicEngine) {
		
		i_bossStepCounter = (i_bossStepCounter+1)%401;
		// TODO Auto-generated method stub
		boss.i_animationFrame = i_bossAnimationFrame;
		
		if(i_bossStepCounter < 100)
			if(boss.v.getPath().isEmpty())
				boss.v.addWaypoint(leftWaypoint);
		
		
		if(i_bossStepCounter == 100)
		{
			boss.i_animationFrameRow = 1;
			boss.v.clearWaypoints();
			boss.v.addWaypoint(leftTopWaypoint);
		}
		
		
		if(i_bossStepCounter == 150)
		{
			boss.i_animationFrameRow = 0;
			boss.v.clearWaypoints();
			boss.v.setPos(rightTopWaypoint.getX(),rightTopWaypoint.getY());
			
			boss.v.addWaypoint(rightWaypoint);
			
			//spawn mines
			for(int i=-2;i<3;i++)
			{
				GameObject go_mine = in_manager.spawnMine(in_logicEngine, leftWaypoint.getX()+ (16*i), LogicEngine.SCREEN_HEIGHT+10);
				((FlyStraightStep)go_mine.stepHandlers.get(0)).setXY(0, -myWalls.i_tunnelSpeed);
			}
			
		}
		
		if(i_bossStepCounter == 300)
		{
			boss.i_animationFrameRow = 1;
			boss.v.clearWaypoints();		
			boss.v.addWaypoint(rightTopWaypoint);
		}
		
		if(i_bossStepCounter == 350)
		{
			boss.i_animationFrameRow = 0;
			boss.v.clearWaypoints();
			boss.v.setPos(leftTopWaypoint.getX(),leftTopWaypoint.getY());
			boss.v.addWaypoint(leftWaypoint);
			
			//spawn mines
			for(int i=-2;i<3;i++)
			{
				GameObject go_mine = in_manager.spawnMine(in_logicEngine, rightWaypoint.getX()+ (16*i), LogicEngine.SCREEN_HEIGHT+10);
				((FlyStraightStep)go_mine.stepHandlers.get(0)).setXY(0, -myWalls.i_tunnelSpeed);
			}
			
		}
	}

	Point2d leftTopWaypoint = new Point2d(((float)LogicEngine.SCREEN_WIDTH/4),LogicEngine.SCREEN_HEIGHT+125);

	Point2d leftWaypoint = new Point2d(((float)LogicEngine.SCREEN_WIDTH/4),LogicEngine.SCREEN_HEIGHT-125);

	Point2d rightTopWaypoint = new Point2d(((float)LogicEngine.SCREEN_WIDTH*.75),LogicEngine.SCREEN_HEIGHT+125);

	Point2d rightWaypoint = new Point2d(((float)LogicEngine.SCREEN_WIDTH*.75),LogicEngine.SCREEN_HEIGHT-125);
	
	private void spawnBoss(LogicEngine in_logicEngine)
	{
		boss = new GameObject("data/"+GameRenderer.dpiFolder+"/thrusterboss.png",((float)LogicEngine.SCREEN_WIDTH/4),LogicEngine.SCREEN_HEIGHT+64,50);
		
		if(Difficulty.isEasy() || Difficulty.isMedium())
			boss.shootEverySteps = 35;
		
		boss.v.addWaypoint(leftWaypoint);
		boss.v.setMaxForce(1.5f);
		boss.v.setMaxVel(1.5f);
		
		//set animation frame
		boss.i_animationFrame=0;
		boss.i_animationFrameRow=0;
		boss.i_animationFrameSizeWidth=64;
		boss.i_animationFrameSizeHeight=112;
		
		//drive onto map
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "25", null);
		
		boss.stepHandlers.add(b);

		boss.allegiance = GameObject.ALLEGIANCES.ENEMIES;

		boss.isBoss=true;
		
		HitpointShipCollision c =  new HitpointShipCollision(boss, 150, 64, true,1);
		
		if(Difficulty.isHard())
			c.f_numberOfHitpoints = 250;
		if(Difficulty.isMedium())
			c.f_numberOfHitpoints = 200;
		
		c.setExplosion(Utils.getBossExplosion(boss));
		c.addHitpointBossBar(in_logicEngine);
		
		//String in_bulletImage,float in_collisionRadius, Vector2d in_direction)
		FragmentationProjectile fp = new FragmentationProjectile("data/"+GameRenderer.dpiFolder+"/redbullets2.png",8,new Vector2d(0,-5f));
		
		fp.setShootAtPlayer(true, 3f);
		boss.shotHandler = fp; 
		boss.collisionHandler = c;
		
		//wander 	
		CustomBehaviourStep cb = new CustomBehaviourStep(new Wander(-2.5,2.5,20,0.1));
		boss.stepHandlers.add( cb);
		
		
		in_logicEngine.toAddObjectsObstacles.add(boss);
		
		
		b_bossSpawned = true;
		
		/*
		bossWestWaypoint = new Point2d(50,LogicEngine.SCREEN_HEIGHT-100);
		bossEastWaypoint = new Point2d(LogicEngine.SCREEN_WIDTH-50,LogicEngine.SCREEN_HEIGHT-100);
		boss.v.addWaypoint(bossEastWaypoint);
		*/
		
			
		/*
		//turret
		for(int i=0;i<2;i++)
		{
			Drawable turret = new Drawable();
			turret.i_animationFrame = 6;
			turret.i_animationFrameSizeWidth=16;
			turret.i_animationFrameSizeHeight=16;
			turret.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
			turret.f_forceRotation = 90;
			
					
			//spawn invisible guy
			GameObject bossTurreti = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",0,LogicEngine.SCREEN_HEIGHT+20,20);
			bossTurreti.i_animationFrameSizeWidth = 16;
			bossTurreti.i_animationFrameSizeHeight = 16;
			bossTurreti.i_animationFrame = 0;
			bossTurreti.i_animationFrameRow = 5;
			bossTurreti.allegiance = GameObject.ALLEGIANCES.ENEMIES;
			
			bossTurreti.v.setMaxForce(0.0f);
			bossTurreti.v.setMaxVel(0.0f);
			
			bossTurreti.visibleBuffs.add(turret);
			
			//ship is invincible as well as being invisible
			HitpointShipCollision hps = new HitpointShipCollision(bossTurreti, 1000, 0);
			
			
			bossTurreti.collisionHandler = hps;
			
			bossTurreti.shotHandler = new TurretShot(turret,"data/"+GameRenderer.dpiFolder+"/redbullets.png",8,5.0f);
			
			in_logicEngine.objectsEnemies.add(bossTurreti);
			
			if(i==0)
				bossTurretLeft = bossTurreti;
			else
			if(i==1)
				bossTurretRight = bossTurreti;
		}*/
	
	}
	
}
