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
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LoopingAnimationStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level3 implements Level{

	LevelManager levelManager;
	TextDisplaying levelText;
	int i_stepCounter=0;
	boolean b_bossSpawned;
	final int BOSS_STEP =3200;
	int i_levelEndCountdown=100;
	Random r = new Random();

	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		
		if(i_stepCounter ==0 )
			in_logicEngine.background.setBackground(1);
		
		//spawn splitters
		if(i_stepCounter%200==0&& i_stepCounter <= 1000)
			in_manager.spawnSplitter(in_logicEngine);
	
		//spawn first double powerup
		if(i_stepCounter == 500)
		{
			in_manager.spawnRandomPowerup(in_logicEngine, (float) LogicEngine.SCREEN_WIDTH/4);
			in_manager.spawnRandomPowerup(in_logicEngine, (float) LogicEngine.SCREEN_WIDTH*0.75f);
		}
		
		//on third splitter spawn batterang pairs too
		if(i_stepCounter >=500 && i_stepCounter%100==0 && i_stepCounter <= 1200)
		{
			FlyStraightStep fs = new FlyStraightStep(new Vector2d(0,-5));
			fs.setIsAccelleration(true);
			
			//random chance of spawning 2
			in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH/4)) , LogicEngine.SCREEN_HEIGHT +25,fs);
			
			//spawn in pairs
			in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH*3/4))  , LogicEngine.SCREEN_HEIGHT +25,fs);
			
		}
		
		//spawn second double powerup	
		if(i_stepCounter == 1000)
		{
			in_manager.spawnRandomPowerup(in_logicEngine, (float) LogicEngine.SCREEN_WIDTH/4);
			in_manager.spawnRandomPowerup(in_logicEngine, (float) LogicEngine.SCREEN_WIDTH*0.75f);
		}
		
		//spawn a few seekers
		if(i_stepCounter > 1000 && i_stepCounter%10==0 && i_stepCounter < 1500 )
			in_manager.spawnWanderingSeeker(in_logicEngine);
		
		if(i_stepCounter > 1000 && i_stepCounter%40==0 && i_stepCounter < 1500 )	
		{
			if(i_stepCounter%20 ==0)
				in_manager.spawnSlime(in_logicEngine,(float)(Math.random()*320.0+1.0f));
			if(i_stepCounter%35 ==0)
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
			if(i_stepCounter%500 ==0)
				in_manager.spawnRandomPowerup(in_logicEngine, (float)(Math.random()*320.0+1.0f));
		}
		
		//spawn a whole lot of seekers
		if(i_stepCounter > 1500 && i_stepCounter%5==0 && i_stepCounter < 2000 )
		{
			in_manager.spawnWanderingSeeker(in_logicEngine);
			
			if(i_stepCounter%500 ==0)
				in_manager.spawnRandomPowerup(in_logicEngine, (float)(Math.random()*320.0+1.0f));
		
		}
		
		if(i_stepCounter > 2000 && i_stepCounter%5==0 && i_stepCounter < 2500 )
		{
			//spawn wandering seekers
			in_manager.spawnWanderingSeeker(in_logicEngine);
			
			//spawn splitters
			if(i_stepCounter%200==0)
				in_manager.spawnSplitter(in_logicEngine);
			
			//spawn seekers
			if(i_stepCounter%100==0)
			{
				in_manager.spawnSeeker(in_logicEngine, LogicEngine.SCREEN_WIDTH/4);
				in_manager.spawnSeeker(in_logicEngine, LogicEngine.SCREEN_WIDTH*0.75f);
			}
		}
		
		//pathfinders and seeker pairs and splitters
		if(i_stepCounter >= 2600 && i_stepCounter < BOSS_STEP)
		{
			if (i_stepCounter %50 == 0)
				in_manager.pathFinderWave(in_logicEngine,0);
			if (i_stepCounter %50 == 25)
				in_manager.pathFinderWave(in_logicEngine,1);
			
			//spawn splitters
			if(i_stepCounter%200==0)
				in_manager.spawnSplitter(in_logicEngine);
			
			//spawn seekers
			if(i_stepCounter%100==0)
			{
				in_manager.spawnSeeker(in_logicEngine, LogicEngine.SCREEN_WIDTH/4);
				in_manager.spawnSeeker(in_logicEngine, LogicEngine.SCREEN_WIDTH*0.75f);
			}
		}
		
		if(i_stepCounter == BOSS_STEP)
			spawnBoss(in_logicEngine);
		
		//level text
		if(i_stepCounter==0)
		{
			SoundEffects.getInstance().levels[3].play(SoundEffects.SPEECH_VOLUME);
			levelText = new TextDisplaying("Level 3",(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
		
		//clouds
		if(i_stepCounter%40 ==0)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));

		
		if(boss != null)
			bossBehaviourStep(in_logicEngine,in_manager);
		
		i_stepCounter++;

		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	
	GameObject boss ;
	GameObject bossWeaponFlareLeft=null;
	GameObject bossWeaponFlareRight=null;
	int i_bossShootDirection=1;
	int i_bossShootCounter=0;
	int i_bossStepCounter=0;
	private void bossBehaviourStep(LogicEngine in_logicEngine,LevelManager in_manager)
	{
		boss.i_animationFrame=0; 
		
		int i_warningFlareDuration = 25;
		
		if(Difficulty.isMedium())
			i_warningFlareDuration = 15;
		if(Difficulty.isHard())
			i_warningFlareDuration = 14;

		//if boss died stop doing his behaviour
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			in_logicEngine.currentAreaEffects.clear();
			return;
		}
			
		//if boss is in place
		if(boss.v.getY()<= LogicEngine.SCREEN_HEIGHT-50)
		{
			//behaviour loops 500
			if(i_bossStepCounter == 251)
				i_bossStepCounter=0;
			
			if(i_stepCounter==BOSS_STEP-100)
				SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
			
			//int i_shootDirection = (int) (Math.random()*3);
			if(i_bossShootDirection ==0)
			{
				//spawn left flare
				if(i_bossShootCounter == 25-i_warningFlareDuration)
				{
					in_logicEngine.objectsOverlay.add(bossWeaponFlareLeft);
				}
				
				//kill player ships
				if(i_bossShootCounter == 25)
				{
					in_logicEngine.currentAreaEffects.add(ae_areaFullLeft);
				}
				//remove area of death
				if(i_bossShootCounter == 45)
				{
					in_logicEngine.objectsOverlay.remove(bossWeaponFlareLeft);
					in_logicEngine.currentAreaEffects.remove(ae_areaFullLeft);
					i_bossShootDirection = r.nextInt(3);
					
				}
			}
			
			if(i_bossShootDirection==1)
			{
				//spawn right flare
				if(i_bossShootCounter == 25-i_warningFlareDuration)
				{
					in_logicEngine.objectsOverlay.add(bossWeaponFlareRight);
				}
				
				//kill player ships
				if(i_bossShootCounter == 25)
				{
					in_logicEngine.currentAreaEffects.add(ae_areaFullRight);
				}
				//remove area of death
				if(i_bossShootCounter == 45)
				{
					in_logicEngine.objectsOverlay.remove(bossWeaponFlareRight);
					in_logicEngine.currentAreaEffects.remove(ae_areaFullRight);
					i_bossShootDirection = r.nextInt(3);
					
				}
			}
			
			if(i_bossShootDirection==2)
			{
				//flare in both areas
				if(i_bossShootCounter == 25-i_warningFlareDuration)
				{
					in_logicEngine.objectsOverlay.add(bossWeaponFlareRight);
					in_logicEngine.objectsOverlay.add(bossWeaponFlareLeft);
				}
				
				//kill player ships
				if(i_bossShootCounter == 25)
				{
					in_logicEngine.currentAreaEffects.add(ae_areaPartialRight);
					in_logicEngine.currentAreaEffects.add(ae_areaPartialLeft);
				}
				//remove area of death
				if(i_bossShootCounter == 45)
				{
					in_logicEngine.objectsOverlay.remove(bossWeaponFlareRight);
					in_logicEngine.objectsOverlay.remove(bossWeaponFlareLeft);
					in_logicEngine.currentAreaEffects.remove(ae_areaPartialRight);
					in_logicEngine.currentAreaEffects.remove(ae_areaPartialLeft);
					i_bossShootDirection = r.nextInt(3);
					
				}
			}
			
			//maybe add for hard diff
			/*if(i_bossStepCounter > 0 && i_bossStepCounter < 150 && i_bossStepCounter%25==0)
			{
				in_manager.spawnSeeker(in_logicEngine, LogicEngine.SCREEN_WIDTH/4);
				in_manager.spawnSeeker(in_logicEngine, LogicEngine.SCREEN_WIDTH*0.75f);
			}*/
			
			if(i_bossStepCounter > 190 && i_bossStepCounter%2==0)
			{
				in_manager.spawnWanderingSeeker(in_logicEngine);
			}
			
			if(i_bossShootCounter ==75)
				i_bossShootCounter=0;
			else
				i_bossShootCounter++;
			
			i_bossStepCounter++;
		}
		
		
		
	}
	SimpleAreaEffect ae_areaFullLeft;
	SimpleAreaEffect ae_areaFullRight;
	SimpleAreaEffect ae_areaPartialLeft;
	SimpleAreaEffect ae_areaPartialRight;
	
	
	private void spawnBoss(LogicEngine in_logicEngine) {
		
		//spawn pyramid in center of screen
		boss = new GameObject("data/"+GameRenderer.dpiFolder+"/minelayer.png",((float)LogicEngine.SCREEN_WIDTH/2),LogicEngine.SCREEN_HEIGHT+64,0);
				
		
		boss.v.addWaypoint(new Point2d(((float)LogicEngine.SCREEN_WIDTH/2),LogicEngine.SCREEN_HEIGHT-75));
		boss.v.setMaxForce(1.5f);
		boss.v.setMaxVel(1.5f);
		
		//set animation frame
		boss.i_animationFrame=0;
		boss.i_animationFrameRow=1;
		boss.i_animationFrameSizeWidth=128;
		boss.i_animationFrameSizeHeight=110;
		
		//drive onto map
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "25", null);
		
		boss.stepHandlers.add(b);

		boss.allegiance = GameObject.ALLEGIANCES.ENEMIES;

		boss.isBoss = true;
		HitpointShipCollision c =  new HitpointShipCollision(boss, 150, 64, true,1);
		c.addHitpointBossBar(in_logicEngine);
		c.setExplosion(Utils.getBossExplosion(boss));
		
		boss.collisionHandler = c;
		
		
		in_logicEngine.objectsEnemies.add(boss);
		b_bossSpawned = true;
		
		
		bossWeaponFlareLeft =  new GameObject("data/"+GameRenderer.dpiFolder+"/minelayer.png",0,0,0);
		//set animation frame
		bossWeaponFlareLeft.i_animationFrame=0;
		bossWeaponFlareLeft.i_animationFrameRow=7;
		bossWeaponFlareLeft.i_animationFrameSizeWidth=32;
		bossWeaponFlareLeft.i_animationFrameSizeHeight=32;
		bossWeaponFlareLeft.allegiance = GameObject.ALLEGIANCES.NONE;
		bossWeaponFlareLeft.v.setX((float)LogicEngine.SCREEN_WIDTH/2-16);
		bossWeaponFlareLeft.v.setY((float)LogicEngine.SCREEN_HEIGHT-75);
		
		
		bossWeaponFlareRight =  new GameObject("data/"+GameRenderer.dpiFolder+"/minelayer.png",0,0,0);
		//set animation frame
		bossWeaponFlareRight.i_animationFrame=0;
		bossWeaponFlareRight.i_animationFrameRow=7;
		bossWeaponFlareRight.i_animationFrameSizeWidth=32;
		bossWeaponFlareRight.i_animationFrameSizeHeight=32;
		bossWeaponFlareRight.allegiance = GameObject.ALLEGIANCES.NONE;
		bossWeaponFlareRight.v.setX((float)LogicEngine.SCREEN_WIDTH/2+16);
		bossWeaponFlareRight.v.setY((float)LogicEngine.SCREEN_HEIGHT-75);
		
		//////////////////area of effect death laz0rz////////////////////////
		Rect r = new Rect(0,0,LogicEngine.SCREEN_WIDTH/2 , LogicEngine.SCREEN_HEIGHT);
		Drawable d = new Drawable();
		d.str_spritename = "data/"+GameRenderer.dpiFolder+"/minelayer.png";
		d.i_animationFrame = 1;
		d.i_animationFrameRow=7;
		d.i_animationFrameSizeHeight=32;
		d.i_animationFrameSizeWidth=32;
				
		ae_areaFullLeft = new SimpleAreaEffect(r,SimpleAreaEffect.Effect.KILL_EVERYTHING,d);
		 
		r = new Rect(LogicEngine.SCREEN_WIDTH/2,0,LogicEngine.SCREEN_WIDTH , LogicEngine.SCREEN_HEIGHT);
		ae_areaFullRight = new SimpleAreaEffect(r,SimpleAreaEffect.Effect.KILL_EVERYTHING,d);
	
		r = new Rect(0,0,(LogicEngine.SCREEN_WIDTH/2) -30 , LogicEngine.SCREEN_HEIGHT);
		ae_areaPartialLeft= new SimpleAreaEffect(r,SimpleAreaEffect.Effect.KILL_EVERYTHING,d);
		
		r = new Rect((LogicEngine.SCREEN_WIDTH/2) +30 ,0,LogicEngine.SCREEN_WIDTH , LogicEngine.SCREEN_HEIGHT);		
		ae_areaPartialRight= new SimpleAreaEffect(r,SimpleAreaEffect.Effect.KILL_EVERYTHING,d);
	}
	@Override
	public void gameOver(LogicEngine in_logicEngine) {

		
	}
	

}
