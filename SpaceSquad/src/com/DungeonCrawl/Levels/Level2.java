package com.DungeonCrawl.Levels;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameObject.ALLEGIANCES;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.DoNothingCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Collisions.PowerupCollision;
import com.DungeonCrawl.Powerups.Powerup;
import com.DungeonCrawl.Powerups.ShieldPowerup;
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.ExplodeIfInRange;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LoopingAnimationStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level2 implements Level{

	LevelManager levelManager;
	TextDisplaying level2Text;
	int i_stepCounter=0;
	boolean b_bossSpawned;
	final int BOSS_STEP =3000;
	
	int i_levelEndCountdown=100;
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {
		
		levelManager = in_manager;
		
		if(i_stepCounter==0)
		{
			in_logicEngine.background.setBackground(1);
			SoundEffects.getInstance().levels[2].play(SoundEffects.SPEECH_VOLUME);
			in_manager.spawnDifficultyIcon(in_logicEngine);
			
			level2Text = new TextDisplaying("Level 2",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(level2Text);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(level2Text);
		
		if(i_stepCounter == 150)
			createPowerupTrap(in_logicEngine);
		
		//clouds
		if(i_stepCounter%40 ==0)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));
		
		
		//mines
		if (i_stepCounter > 200 && i_stepCounter < 700 && i_stepCounter %55 == 0)
			if(Difficulty.isEasy())
				in_manager.mineWave(in_logicEngine, 3, i_stepCounter %2==0,false);
			else
				if(Difficulty.isMedium())
					in_manager.mineWave(in_logicEngine, 6, i_stepCounter %2==0,false);
				else
					if(Difficulty.isHard())
						in_manager.mineWave(in_logicEngine, 8, i_stepCounter %2==0,false);
					
		
		//pathfinders on thier own
		if(i_stepCounter > 900 && i_stepCounter < 1400)
		{
			
			if (i_stepCounter %30 == 0)
				in_manager.pathFinderWave(in_logicEngine,0);
			
			if (i_stepCounter %30 == 15)
				in_manager.pathFinderWave(in_logicEngine,1);
		}
	
		//---------------------------------
		//pathfinders and batterangs
		//---------------------------------
		if(i_stepCounter > 1400 && i_stepCounter < 2200)
		{
			if (i_stepCounter %50 == 0)
				in_manager.pathFinderWave(in_logicEngine,0);
			if (i_stepCounter %50 == 25)
				in_manager.pathFinderWave(in_logicEngine,1);
		}
		
		if(i_stepCounter > 1400 && i_stepCounter < 2200)
		{
			FlyStraightStep fs = new FlyStraightStep(new Vector2d(0,-5));
			fs.setIsAccelleration(true);
			
			//random chance of spawning a baterang
			if(i_stepCounter%30 ==0)
				if(((int)(Math.random()*100.0f)%2) ==0)
					in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH/2)*Math.random()) +LogicEngine.SCREEN_WIDTH/4 , LogicEngine.SCREEN_HEIGHT +25,fs);
			
			
		}
		
		//mines and asteroids 
		if(i_stepCounter > 2300 && i_stepCounter < 3000 && i_stepCounter %25 ==0)
			in_manager.mineWave(in_logicEngine,1, false,true);
		
		//asteroids
		if(i_stepCounter > 800 && i_stepCounter < BOSS_STEP -40)
			if(i_stepCounter%35 ==0)
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
		//seekers
		if(i_stepCounter > 2300 && i_stepCounter%50 ==0  && i_stepCounter < BOSS_STEP -40)
			in_manager.spawnSeeker(in_logicEngine, (float)(Math.random()*320.0+1.0f));
		
		//powerups
		if(i_stepCounter%500 ==0 && i_stepCounter > 200 && boss!=null)
			in_manager.spawnRandomPowerup(in_logicEngine, (float)(Math.random()*320.0+1.0f));
		
		mineLayerBoss(in_logicEngine);
		
		i_stepCounter++;
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	
	int bossDirection=1;
	GameObject boss = null;
	Point2d bossWestWaypoint;
	Point2d bossEastWaypoint;
	private void mineLayerBoss( LogicEngine in_logicEngine)
	{
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
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
		
		//drop mines
		if(boss != null && i_stepCounter%40 == 0)
		{
			GameObject mine = levelManager.spawnMine(in_logicEngine, boss.v.getX(),boss.v.getY()-50);
			
			mine.i_animationFrameRow = 1;
			mine.stepHandlers.add(new LoopingAnimationStep(5, 2));
			
			if(Difficulty.isMedium())
				mine.stepHandlers.add(new SeekNearestPlayerStep(1000));
			
			if(Difficulty.isHard())
				mine.stepHandlers.add(new SeekNearestPlayerStep(1000));
		}
		
		//drop twice as many mines 1/2 of the time
		if(boss != null && i_stepCounter%40 == 20 && Difficulty.isHard() && Math.random()%4<=1)
		{
			GameObject mine = levelManager.spawnMine(in_logicEngine, boss.v.getX(),boss.v.getY()-50);
			 
			mine.i_animationFrameRow = 1;
			mine.stepHandlers.add(new LoopingAnimationStep(5, 2));
			
			mine.stepHandlers.add(new SeekNearestPlayerStep(1000));
		}
		
		//spawn greenies
		if(boss != null && i_stepCounter%35 == 0)
		{
			levelManager.spawnSlime(in_logicEngine, Math.random() * LogicEngine.SCREEN_WIDTH);
		}
		
		if(i_stepCounter == BOSS_STEP)
		{
			
			
			
			bossWestWaypoint = new Point2d(50,LogicEngine.SCREEN_HEIGHT-100);
			bossEastWaypoint = new Point2d(LogicEngine.SCREEN_WIDTH-50,LogicEngine.SCREEN_HEIGHT-100);
			
			boss = new GameObject("data/"+GameRenderer.dpiFolder+"/boss2.png",LogicEngine.rect_Screen.getWidth()/2,LogicEngine.rect_Screen.getHeight()+100,0);
			boss.isBoss = true;
			
			boss.v.setMaxForce(.25);
			boss.v.setMaxVel(3);
			boss.allegiance = GameObject.ALLEGIANCES.ENEMIES;
			
			//set animation /graphics sizes
			boss.i_animationFrameSizeWidth=50;
			boss.i_animationFrameSizeHeight=100;
			boss.i_animationFrameRow = 0;
					
			//set up steps
			SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
			followPathBehaviour.setInfluence(1);
			followPathBehaviour.setAttribute("arrivedistance", "50", null);
			boss.stepHandlers.add(new CustomBehaviourStep(followPathBehaviour));
			
			boss.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT-100));
			boss.v.addWaypoint(bossEastWaypoint);
			boss.v.addWaypoint(bossWestWaypoint);
			HitpointShipCollision c =  new HitpointShipCollision(boss, 100, 40, true,1);
			c.addHitpointBossBar(in_logicEngine);
		
			//set destroyed object
			/*
			GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/minelayer.png",0,0,0);
			explosion.i_animationFrameRow = 0;
			explosion.i_animationFrame =2;
			explosion.i_animationFrameSizeWidth =50;
			explosion.i_animationFrameSizeHeight =100;
			explosion.stepHandlers.add( new StaticAnimationStep(3,20,2));*/
			c.setExplosion(Utils.getBossExplosion(boss));			
			boss.collisionHandler = c;
							
			
			
		
			
			b_bossSpawned = true;
			
			in_logicEngine.objectsEnemies.add(boss);
			
			//web 
			GameObject go_web1 = new GameObject("data/"+GameRenderer.dpiFolder+"/minelayer.png",0,LogicEngine.rect_Screen.getHeight()+100,0);
			go_web1.i_animationFrameSizeWidth=100;
			go_web1.i_animationFrameSizeHeight=100;
			go_web1.i_animationFrameRow = 0;
			go_web1.v.addWaypoint(new Point2d(50,LogicEngine.SCREEN_HEIGHT-50));
			
			SimplePathfollowing followPathBehaviour2 = new SimplePathfollowing();
			followPathBehaviour2.setInfluence(1);
			followPathBehaviour2.setAttribute("arrivedistance", "5", null);
			go_web1.stepHandlers.add(new CustomBehaviourStep(followPathBehaviour2));
			
			go_web1.v.setMaxForce(5.0f);
			go_web1.v.setMaxVel(5.0f);
			go_web1.allegiance = ALLEGIANCES.ENEMIES;
			
			
			
			GameObject go_web2 = new GameObject("data/"+GameRenderer.dpiFolder+"/minelayer.png",LogicEngine.SCREEN_WIDTH ,LogicEngine.rect_Screen.getHeight()+100,0);
			go_web2.i_animationFrameSizeWidth=100;
			go_web2.i_animationFrameSizeHeight=100;
			go_web2.i_animationFrame = 1;
			go_web2.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH - 50,LogicEngine.SCREEN_HEIGHT-50));
			go_web2.stepHandlers.add(new CustomBehaviourStep(followPathBehaviour2));
			
			go_web2.v.setMaxForce(5.0f);
			go_web2.v.setMaxVel(5.0f);
			go_web2.allegiance = ALLEGIANCES.ENEMIES;
			
			in_logicEngine.objectsEnemies.add(go_web1);
			in_logicEngine.objectsEnemies.add(go_web2);
		}
	}
	
	private void createPowerupTrap(LogicEngine in_logicEngine)
	{
	
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/powerups.png",LogicEngine.SCREEN_WIDTH/2,LogicEngine.rect_Screen.getHeight()+50,0);
		
		//not lethal to anyone
		go.allegiance = GameObject.ALLEGIANCES.NONE;
				
		go.i_animationFrameSizeWidth=16;
		go.i_animationFrameSizeHeight=16;
		go.i_animationFrameRow = 0;
		go.i_animationFrame = 1;
		
		//apply powerup to all ships
		go.collisionHandler = new PowerupCollision(new ShieldPowerup(),true);
		
		go.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-2.5)));
		in_logicEngine.objectsPowerups.add(go);
		
	}
	

	@Override
	public void gameOver(LogicEngine in_logicEngine) {

		
	}

}
