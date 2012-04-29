package com.DungeonCrawl.Levels;

import java.util.ArrayList;
import java.util.Random;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.ExplodeIfInRange;
import com.DungeonCrawl.Shooting.TurretShot;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level5 extends BasicLevel{

	Random r = new Random();
	public Level5()
	{
		this.i_stepCounter=0;
		i_levelNumber = 5;
		BOSS_STEP=3000;
		
	}
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {
		
		if(i_stepCounter ==0 )
			in_logicEngine.background.setBackground(0);
		
		//Phase 1 (100-1250)///////////////// Asteroids, satelites and various trash mobs////////////////
		if(i_stepCounter >100 && i_stepCounter < 1250)
		{
			//spawn satellites
			if( i_stepCounter %100==0)
				in_manager.spawnSateliteShip(in_logicEngine, 100);
		}
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
		
		if(i_stepCounter == 500 || i_stepCounter == 600)
		{
				GameObject go = in_manager.spawnBomber(in_logicEngine);
				
				//reduce asteroid damage and laser damage
				go.isBoss=true;
		}
		
		//batterangs
		if(i_stepCounter >250 && i_stepCounter <750)
		{
			if(i_stepCounter%30 ==0)
			{
				FlyStraightStep fs = new FlyStraightStep(new Vector2d(0,-5));
				fs.setIsAccelleration(true);
				
				//random chance of spawning 2
				if(((int)(Math.random()*100.0f)%2) ==0)
					in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH/2)*Math.random()) +LogicEngine.SCREEN_WIDTH/4 , LogicEngine.SCREEN_HEIGHT +25,fs);
				
				//spawn in pairs
				in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH/2)*Math.random()) +LogicEngine.SCREEN_WIDTH/4 , LogicEngine.SCREEN_HEIGHT +25,fs);
				
			}
		}
		
		//spawn pathfinders
		if(i_stepCounter >750 && i_stepCounter <1250)
			if (i_stepCounter %50 == 0)
				in_manager.pathFinderWave(in_logicEngine,0);
			else
			if (i_stepCounter %50 == 25)
				in_manager.pathFinderWave(in_logicEngine,1);
	
		//spawn asteroids
		if(i_stepCounter%20 ==0 && i_stepCounter < 1250)
			in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);

		//Phase 2 (1250-2500)///////////////// black holes and seekers////////////////
		if( i_stepCounter > 1250 && i_stepCounter < 2000 )
		{
			if(i_stepCounter%100==0)
				in_manager.spawnBlackHole(in_logicEngine, (float) (LogicEngine.SCREEN_WIDTH/4 + (Math.random()* LogicEngine.SCREEN_WIDTH/2)));
			
				
			if( i_stepCounter %2==0)
				in_manager.spawnWanderingSeeker(in_logicEngine);
				
				
/*			if(i_stepCounter%20==0)
				in_manager.spawnSeeker(in_logicEngine, (float) (LogicEngine.SCREEN_WIDTH/4 + (Math.random()* LogicEngine.SCREEN_WIDTH/2)));*/
		}
		
		if(i_stepCounter == 2100 || i_stepCounter == 2200 || i_stepCounter == 2400)
		{
				GameObject go = in_manager.spawnBomber(in_logicEngine);
				
				//reduce asteroid damage and laser damage
				go.isBoss=true;
		}
		
		//Phase 3 (2500- BOSS_STEP) ////////////// dense mine field and satelites
		if( i_stepCounter > 2250 && i_stepCounter < BOSS_STEP -150 )
		{
			if ( i_stepCounter %55 == 0)
				in_manager.mineWave(in_logicEngine,4 ,i_stepCounter %2==0,false);
			
			//spawn satelites
			if( i_stepCounter %150==0)
				in_manager.spawnSateliteShip(in_logicEngine, 100);
		}
		
		//Phase 4, boss
		if(i_stepCounter == BOSS_STEP)
			spawnBoss(in_logicEngine);
		
		if(i_stepCounter >= BOSS_STEP )
			stepBoss(in_logicEngine);
		
		//powerups
		if(i_stepCounter >100 &&i_stepCounter%500==0)
			in_manager.spawnRandomPowerup(in_logicEngine, (float) (LogicEngine.SCREEN_WIDTH*Math.random()));

		//clouds
		if(i_stepCounter%40 ==0)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));

	

		return	super.stepLevel(in_manager, in_logicEngine);
	}

	
	private void stepBoss(LogicEngine in_logicEngine) {
		
		//prevent flashing
		for(int i=0 ; i< boss_parts.size() ;i++)
		{
			//unflash
			boss_parts.get(i).i_animationFrame=6;
			
			//see if dead
			if(in_logicEngine.objectsEnemies.indexOf(boss_parts.get(i))==-1)
			{
				boss_parts.remove(i);
				stepBoss(in_logicEngine); //recursion to carry out this step again now that the array is smaller
				return;
			}
			
		}
		
		//if its dead then return
		if(boss_parts.size()==0)
		{
			i_levelEndCountdown--;
			
			return;
		}
		//give the leading head a chase order
		boss_parts.get(0).stepHandlers.clear();
		boss_parts.get(0).stepHandlers.add(boss_seekPlayerStep);
		
		//give head instruction
		for(int i=boss_parts.size()-1;i>0 ;i--)
		{
			GameObject thisPart = boss_parts.get(i);
			GameObject nextPart = boss_parts.get(i-1);
			
			//follow next piece along
			thisPart.stepHandlers.clear();
			thisPart.stepHandlers.add(boss_followPathStep);
			thisPart.v.clearWaypoints();
			thisPart.v.addWaypoint(nextPart.v.getPos());
			thisPart.i_animationFrame = 6;
			
		}
		
		return;
	}

	ArrayList<GameObject> boss_parts;
	SeekNearestPlayerStep boss_seekPlayerStep;
	CustomBehaviourStep boss_followPathStep;
	
	private void spawnBoss(LogicEngine in_logicEngine)
	{
		
		boss_parts = new ArrayList<GameObject>();
		
		int i_numberOfSections = 35;
		
		if(Difficulty.isMedium())
			i_numberOfSections = 40;
		
		if(Difficulty.isHard())
			i_numberOfSections = 50;
		
		boss_seekPlayerStep = new SeekNearestPlayerStep(1000);
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "10", null);
		boss_followPathStep = new CustomBehaviourStep(followPathBehaviour);
		 
		for(int i=0 ; i<i_numberOfSections;i++)
		{
			GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT,1);
			ship.i_animationFrameSizeWidth = 32;
			ship.i_animationFrameSizeHeight = 32;
			ship.i_animationFrame = 6;
			
			ship.v.setMaxForce(2.0f);
			ship.v.setMaxVel(5.0f);
			
			ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1f)));
			
			//destroy ships that get too close
			HitpointShipCollision hps=null; 
			if(Difficulty.isEasy())
				hps = new HitpointShipCollision(ship, 9, 18,true,7);
			else
			if(Difficulty.isMedium())
				hps = new HitpointShipCollision(ship, 8, 18,true,7);
			else
			if(Difficulty.isHard())
				hps = new HitpointShipCollision(ship, 7, 18,true,7);
			
			hps.setSimpleExplosion();
			
			ship.collisionHandler = hps; 
			ship.allegiance= GameObject.ALLEGIANCES.ENEMIES;
			ship.isBoss = true;
			
			boss_parts.add(ship);
			
			in_logicEngine.objectsEnemies.add(ship);	
		}
	}
	
	
	
	

	
}
