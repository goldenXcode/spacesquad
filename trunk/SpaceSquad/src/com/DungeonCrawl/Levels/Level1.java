package com.DungeonCrawl.Levels;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Walls;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Shooting.ShotHandler;
import com.DungeonCrawl.Shooting.SplitShot;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LoopingAnimationStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StepHandler;
import com.DungeonCrawl.Steps.WanderStep;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level1 implements Level {
	//DEBUG
	public int i_stepCounter=0;
	public final int BOSS_STEP =2000;
	boolean b_bossSpawned=false;
	int i_levelEndCountdown=100;
	
	TextDisplaying level1Text;
	ShotHandler BossWeapon1 = new SplitShot("data/"+GameRenderer.dpiFolder+"/redbullets.png",-3);
	ShotHandler BossWeapon2 = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/redbullets.png",6.0f,new Vector2d(0,-10));
	StepHandler BossChasePlayer = new SeekNearestPlayerStep(500);
	StepHandler BossWander = new WanderStep();
	GameObject boss=null;
	Drawable d_eye ;
	
	Walls myWalls = new Walls();
	
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine)
	{
		if(i_stepCounter==0)
		{
			in_logicEngine.background.setBackground(0);
			
			SoundEffects.getInstance().levels[1].play(SoundEffects.SPEECH_VOLUME);
			in_manager.spawnDifficultyIcon(in_logicEngine);
			level1Text = new TextDisplaying("Level 1",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(level1Text);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(level1Text);
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
		//boss
		if(i_stepCounter==BOSS_STEP)
		{
			boss = new GameObject("data/"+GameRenderer.dpiFolder+"/boss1.png",LogicEngine.rect_Screen.getWidth()/2,LogicEngine.rect_Screen.getHeight()+10,0);
			
			boss.isBoss = true;
			
			boss.v.setMaxForce(1);
			boss.v.setMaxVel(6);
			boss.allegiance = GameObject.ALLEGIANCES.ENEMIES;
			
			boss.i_animationFrameSizeWidth=64;
			boss.i_animationFrameSizeHeight=64;
			
			d_eye = new Drawable();
			d_eye.i_animationFrameSizeHeight=16;
			d_eye.i_animationFrameSizeWidth=16;
			d_eye.str_spritename = "data/"+GameRenderer.dpiFolder+"/eye.png";
			
			
			HitpointShipCollision c =  new HitpointShipCollision(boss, 100, 40, true,1);
			c.addHitpointBossBar(in_logicEngine);
			
		

			c.setExplosion(Utils.getBossExplosion(boss));
			
			boss.collisionHandler = c;
			
			
			boss.shotHandler = BossWeapon1;
			
				
			boss.shootEverySteps = 5;
			
			if(Difficulty.isMedium())
				boss.shootEverySteps = 10;
			if(Difficulty.isHard())
				boss.shootEverySteps = 10;
			
			boss.i_animationFrameSizeWidth=64;
			boss.i_animationFrameSizeHeight =64;
			
			boss.stepHandlers.add(BossWander);
			
			
			in_logicEngine.objectsEnemies.add(boss);
			b_bossSpawned = true;
		}
		

		
		if(in_logicEngine.objectsEnemies.indexOf(boss)!=-1)
		{
			//swap weapons
			if(i_stepCounter%150 == 0)
				if(boss.shotHandler == BossWeapon2)
						boss.shotHandler = BossWeapon1;
			
			if(i_stepCounter%150 == 75)
				if(boss.shotHandler == BossWeapon1)
					boss.shotHandler = BossWeapon2;
			
			//open eye
			if(i_stepCounter%220 == 165)
				boss.visibleBuffs.add(d_eye);
			
			//keep eye on player and flash with boss
			d_eye.f_forceRotation = 180f+ Utils.getAngleOfRotation(Utils.getVectorToClosestPlayer(in_logicEngine,boss.v.getPos())); 
			d_eye.i_animationFrame = boss.i_animationFrame;
			
			//chase player
			if(i_stepCounter%220 == 175)
			{
				//stop firing if on easy
				if(Difficulty.isEasy())
					boss.shotHandler = null;
				
				boss.stepHandlers.remove(BossWander);
				boss.stepHandlers.remove(BossChasePlayer);
				boss.stepHandlers.add(BossChasePlayer);
			}
			
			//stand still
			if(Difficulty.isEasy() || Difficulty.isMedium())
				if(i_stepCounter%220 == 0)
				{
					boss.shotHandler = BossWeapon2;
					boss.visibleBuffs.clear();
					boss.stepHandlers.remove(BossChasePlayer);
					boss.stepHandlers.add(BossWander);
				}
		}
		if(i_stepCounter > 800 && i_stepCounter < 1100)
		{
			myWalls.i_tunnelSpeed = 4;
			
			
			if(i_stepCounter == 832)
			{
				myWalls.narrowTunnel();
				
			}
			if(i_stepCounter == 864)
			{
				myWalls.narrowTunnel();
				
			}
			if(i_stepCounter == 880)
			{
				myWalls.moveTunnelLeft();
			}
			
			if(i_stepCounter == 896)
			{
				myWalls.moveTunnelLeft();	
				
			}

			if(i_stepCounter == 928)
			{
				myWalls.spawnWeakShooterBlock(in_logicEngine);
			}
			
			
			if(i_stepCounter == 960)
			{
				myWalls.spawnWeakShooterBlock(in_logicEngine);
			}
			
			if(i_stepCounter == 992)
			{
				myWalls.spawnShooterBlock(in_logicEngine);
			}
			if(i_stepCounter == 1024)
			{
				myWalls.openTunnel();
			}
			if(i_stepCounter == 1056)
			{
				myWalls.openTunnel();
			}
			if(i_stepCounter == 1088)
			{
				myWalls.openTunnel();
			}
			
			myWalls.spawnBlockIfNeeded(in_logicEngine, i_stepCounter);
		}
		
		if(i_stepCounter < 800 &&i_stepCounter > 200)
		{
			if(i_stepCounter%30 ==0)
			{
				FlyStraightStep fs = new FlyStraightStep(new Vector2d(0,-5));
				fs.setIsAccelleration(true);
				
				//random chance of spawning 2
				if(((int)(Math.random()*100.0f)%2) ==0)
					in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH/2)*Math.random()) +LogicEngine.SCREEN_WIDTH/4 , LogicEngine.f_worldCoordTop+25,fs);
				
				//spawn in pairs
				in_manager.spawnBaterang(in_logicEngine, (float) ((LogicEngine.SCREEN_WIDTH/2)*Math.random()) +LogicEngine.SCREEN_WIDTH/4 , LogicEngine.f_worldCoordTop +25,fs);
				
			}
			
			if(i_stepCounter > 600 && i_stepCounter < 900)
				if(i_stepCounter%20 ==0)
					in_manager.spawnSlime(in_logicEngine,(float)(Math.random()*320.0+1.0f));
			
			if(i_stepCounter%300 ==0)
				in_manager.spawnRandomPowerup(in_logicEngine, (float)(Math.random()*320.0+1.0f));
			
		}
		
		if(i_stepCounter%40 ==0)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));
		
		if(i_stepCounter>=1100 && i_stepCounter < BOSS_STEP -100)
		{
			//spawn enemies
			if(i_stepCounter%20 ==0)
				in_manager.spawnSlime(in_logicEngine,(float)(Math.random()*320.0+1.0f));
	
			if(i_stepCounter%50 ==0)
				in_manager.spawnSeeker(in_logicEngine, (float)(Math.random()*320.0+1.0f));
			if(i_stepCounter%35 ==0)
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
			if(i_stepCounter%500 ==0)
				in_manager.spawnRandomPowerup(in_logicEngine, (float)(Math.random()*320.0+1.0f));
			
			
			if(i_stepCounter > 1500 && i_stepCounter < 1700 )
			{
				myWalls.i_tunnelSpeed = 4;
				
				if(i_stepCounter == 1504)
				{
					myWalls.narrowTunnel();
				}
				
				if(i_stepCounter == 1520)
				{
					myWalls.narrowTunnel();
				}
				
				
				if(i_stepCounter == 1568)
				{
					myWalls.spawnShooterBlock(in_logicEngine);
				}
				
				if(i_stepCounter == 1616)
				{
					myWalls.spawnWeakShooterBlock(in_logicEngine);
				}
				
				if(i_stepCounter == 1664)
				{
					myWalls.spawnWeakShooterBlock(in_logicEngine);
				}
				
				if(i_stepCounter == 1696)
				{
					myWalls.openTunnel();
				}
				
				myWalls.spawnBlockIfNeeded(in_logicEngine, i_stepCounter);
			}
		}
		i_stepCounter++;
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	
	
}
