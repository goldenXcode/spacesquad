package com.DungeonCrawl.Levels;

import com.DungeonCrawl.AreaEffect;
import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LoopingAnimationStep;
import com.DungeonCrawl.Steps.PullShipsStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.WanderStep;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import com.DungeonCrawl.Shooting.TurretShot;

public class Level4 extends BasicLevel{
	LevelManager levelManager;
	
		
	public Level4()
	{
		
		i_levelNumber = 4;
		BOSS_STEP = 3000;
		i_stepCounter=0;
	}
	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		levelManager = in_manager;

		if(i_stepCounter ==0 )
			in_logicEngine.background.setBackground(0);
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
		
		//turret ships
		if(i_stepCounter >=100 && i_stepCounter < 1000 && i_stepCounter%200==0)
		{
			in_manager.spawnTurretShip(in_logicEngine,  LogicEngine.SCREEN_WIDTH/4);
			in_manager.spawnTurretShip(in_logicEngine,  (float) (LogicEngine.SCREEN_WIDTH/1.5));
		}

		//lots of turret ships
		if(i_stepCounter >=1000 && i_stepCounter < 1500 && i_stepCounter%150==0)
		{
			in_manager.spawnTurretShip(in_logicEngine,  LogicEngine.SCREEN_WIDTH/4);
			in_manager.spawnTurretShip(in_logicEngine,  (float) (LogicEngine.SCREEN_WIDTH/1.5));
		}
		//turrets and pathfinders
		if(i_stepCounter >500 && i_stepCounter < 1500)
		{
			if (i_stepCounter %50 == 0)
				in_manager.pathFinderWave(in_logicEngine,0);
			if (i_stepCounter %50 == 25)
				in_manager.pathFinderWave(in_logicEngine,1);
		}
		
		//turrets seekers and pathfinders
		if(i_stepCounter >800 && i_stepCounter < 1500 &&i_stepCounter%80==0)
		{
			in_manager.spawnSeeker(in_logicEngine, (float) (Math.random()*LogicEngine.SCREEN_WIDTH/2));
			in_manager.spawnSeeker(in_logicEngine, (float) ((Math.random()*LogicEngine.SCREEN_WIDTH/2) + LogicEngine.SCREEN_WIDTH/2  ));
		}
		
		//////////////////////Black holes phase////////////////////////////
		
		//spawn black holes
		if(i_stepCounter > 1200 && i_stepCounter<2300 && i_stepCounter%150==0)
			in_manager.spawnBlackHole(in_logicEngine,LogicEngine.SCREEN_WIDTH/2);
		
		
		//spawn boulders (put them in enemy que so they collide with black holes)
		if(i_stepCounter>1300 && i_stepCounter<2300 &&i_stepCounter%10==0)
			in_manager.spawnEnemyAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
			
		if(i_stepCounter>1900 && i_stepCounter<2300)
		{
			if (i_stepCounter %50 == 0)
				in_manager.pathFinderWave(in_logicEngine,0);
			if (i_stepCounter %50 == 25)
				in_manager.pathFinderWave(in_logicEngine,1);
		}
		/////////////////////End black holes phase/////////////////////
		

		//turret ships 2 (2 waves)
		if(i_stepCounter >=2400 && i_stepCounter <= 2700 && i_stepCounter%300==0)
		{
			in_manager.spawnTurretShip(in_logicEngine,  LogicEngine.SCREEN_WIDTH/4);
			in_manager.spawnTurretShip(in_logicEngine,  (float) (LogicEngine.SCREEN_WIDTH/1.5));
		}
		if(i_stepCounter >=2400 && i_stepCounter <= 2750 && i_stepCounter%300==40)
		{
			in_manager.spawnTurretShip(in_logicEngine, 32 + LogicEngine.SCREEN_WIDTH/4);
			in_manager.spawnTurretShip(in_logicEngine,  -32 + (float) (LogicEngine.SCREEN_WIDTH/1.5));
		}

		//////////////////////////Boss//////////////////
		if(i_stepCounter == BOSS_STEP)
			spawnBoss(in_logicEngine);
		if(boss != null)
			stepBoss(in_manager, in_logicEngine);
		
		//powerups
		if(i_stepCounter >100 &&i_stepCounter%500==0)
			in_manager.spawnRandomPowerup(in_logicEngine, (float) (LogicEngine.SCREEN_WIDTH*Math.random()));

		//clouds
		if(i_stepCounter%40 ==0)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));

		//call basic level
		return super.stepLevel(in_manager, in_logicEngine);
	}
	
	
	

	

	
private void spawnBoss(LogicEngine in_logicEngine) {
		
		//spawn pyramid in center of screen
		boss = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",((float)LogicEngine.SCREEN_WIDTH/2),LogicEngine.SCREEN_HEIGHT+64,25);
		
		
		boss.v.addWaypoint(new Point2d(((float)LogicEngine.SCREEN_WIDTH/2),LogicEngine.SCREEN_HEIGHT-75));
		boss.v.setMaxForce(1.0f);
		boss.v.setMaxVel(1.0f);
		
		//set animation frame
		boss.i_animationFrame=0;
		boss.i_animationFrameRow=1;
		boss.i_animationFrameSizeWidth=128;
		boss.i_animationFrameSizeHeight=64;
		
		//drive onto map
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "25", null);
		
		boss.stepHandlers.add(b);

		boss.allegiance = GameObject.ALLEGIANCES.ENEMIES;

		
		HitpointShipCollision c =  new HitpointShipCollision(boss, 150, 64, true,1);
		c.setExplosion(Utils.getBossExplosion(boss));
		c.addHitpointBossBar(in_logicEngine);
		
		boss.collisionHandler = c;
		
		
		in_logicEngine.objectsEnemies.add(boss);
		b_bossSpawned = true;
		
		bossWestWaypoint = new Point2d(50,LogicEngine.SCREEN_HEIGHT-100);
		bossEastWaypoint = new Point2d(LogicEngine.SCREEN_WIDTH-50,LogicEngine.SCREEN_HEIGHT-100);
		boss.v.addWaypoint(bossEastWaypoint);
		
		
			
		
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
			
			bossTurreti.shotHandler = new TurretShot(turret,"data/"+GameRenderer.dpiFolder+"/redbullets.png",6,5.0f);
			
			in_logicEngine.objectsEnemies.add(bossTurreti);
			
			if(i==0)
				bossTurretLeft = bossTurreti;
			else
			if(i==1)
				bossTurretRight = bossTurreti;
		}
		
			
		
	}
	
	Point2d bossWestWaypoint;
	Point2d bossEastWaypoint;
	GameObject bossTurretLeft=null;
	GameObject bossTurretRight=null;
	int i_bossStepCounter=0;
	
	private void stepBoss(LevelManager in_manager,LogicEngine in_logicEngine)
	{
		//move turrets
		if(boss != null)
		{
			bossTurretLeft.v.setX(boss.v.getX()-24);
			bossTurretLeft.v.setY(boss.v.getY()-8);
			
			bossTurretRight.v.setX(boss.v.getX()+24);
			bossTurretRight.v.setY(boss.v.getY()-8);
		}
		
		//destroy turrets
		if(boss != null && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			in_logicEngine.objectsEnemies.remove(bossTurretLeft);
			in_logicEngine.objectsEnemies.remove(bossTurretRight);
		}
		
		//fix flashes sticking
		if(boss !=null && boss.i_animationFrame==1)
		{
			boss.i_animationFrame=0;
		}
		
		//add new waypoints
		if(boss != null && boss.v.getPath().size() ==1)
		{
			if( boss.v.getPath().get(0) == bossWestWaypoint)
				boss.v.addWaypoint(bossEastWaypoint);
			else
			if( boss.v.getPath().get(0) == bossEastWaypoint)
				boss.v.addWaypoint(bossWestWaypoint);
		}
		
		//if boss died stop doing his behaviour
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			in_logicEngine.currentAreaEffects.clear();
			return;
		}
			
		//if boss is in place
		if(boss.v.getY()<= LogicEngine.SCREEN_HEIGHT-50)
		{
			//behaviour loops 250
			if(i_bossStepCounter == 299)
				i_bossStepCounter=0;
			
			//drop mines
			if(i_bossStepCounter% 50 == 0)
			{
				GameObject mine = levelManager.spawnMine(in_logicEngine, boss.v.getX(), boss.v.getY());
				
				 
				mine.i_animationFrameRow = 1;
				mine.stepHandlers.add(new LoopingAnimationStep(5, 2));
				
				if(Difficulty.isMedium() || Difficulty.isHard() )
					mine.stepHandlers.add(new SeekNearestPlayerStep(1000));
			}
			
			//spawn turret ship
			if(i_bossStepCounter == 289)
				if(Math.random() > 0.5)
					in_manager.spawnTurretShip(in_logicEngine,LogicEngine.SCREEN_WIDTH-32);
				else
					in_manager.spawnTurretShip(in_logicEngine,32);
			
			//on hard spawn twice as often
			if(Difficulty.isHard() && i_bossStepCounter == 150)
				if(Math.random() > 0.5)
					in_manager.spawnTurretShip(in_logicEngine,LogicEngine.SCREEN_WIDTH-32);
				else
					in_manager.spawnTurretShip(in_logicEngine,32);
			
			i_bossStepCounter++;
		}
		
	
	}
		
}
