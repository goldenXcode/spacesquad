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
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Collisions.SplitCollision;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.BounceOfScreenEdgesStep;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.TimedLifeStep;
import com.DungeonCrawl.Steps.WanderStep;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level8 extends BasicLevel{

	
	Walls myWalls = new Walls();
	
	Random r = new Random();
	
	public Level8()
	{
		i_levelNumber = 8;
		
		i_stepCounter = 0;
		myWalls.setColour(new Color(0,0,1,1));
	}
	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		if(i_stepCounter ==0)
		{	
			in_logicEngine.background.setBackground(1);
			in_manager.spawnDifficultyIcon(in_logicEngine);
		}
		
		if(i_stepCounter %400 == 0)
			in_manager.spawnRandomPowerup(in_logicEngine, (float) (Math.random()* LogicEngine.SCREEN_WIDTH));
		
		/////////////////////////BUBBLES ////////////////////////////////////////
		if(i_stepCounter < 500)
		{
			if(i_stepCounter%150==0)
				spawnBubble(in_logicEngine,LogicEngine.SCREEN_WIDTH/3);
			
			if(i_stepCounter%150==75)
				spawnBubble(in_logicEngine,(LogicEngine.SCREEN_WIDTH*2)/3);
		}
		
		if(i_stepCounter > 500 && i_stepCounter < 850)
		{
			if(i_stepCounter%10 == 0 )
			{
				GameObject slime = in_manager.spawnSlime(in_logicEngine, myWalls.getMiddleOfTunnel() + (r.nextDouble() -0.5 )*100);
				
				slime.v.setMaxVel(16);
				for(int i=0;i<slime.stepHandlers.size();i++)
					if(slime.stepHandlers.get(i) instanceof FlyStraightStep)
						((FlyStraightStep)slime.stepHandlers.get(i)).setXY(0, -8);
			}
			
			///////////////////////// TUNNEL ////////////////////////////////////////
			if(i_stepCounter < 600)
			{
			
				myWalls.i_tunnelSpeed = 16;
				
				myWalls.f_maximumBlockWidth = LogicEngine.SCREEN_WIDTH - 135;
				
				
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
				
				if(i_stepCounter % 16 ==0)
					myWalls.narrowTunnel();
			}
			else
			if(i_stepCounter < 700)
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
				
				if(i_stepCounter % 16 ==0)
					myWalls.narrowTunnel();
			}
			else
			if(i_stepCounter <= 850)
			{
				if(i_stepCounter % 16 ==0)
					myWalls.openTunnel();
			}
		}
		//////////////// WAVE RIDERS //////////////////////////////////
		if(i_stepCounter == 800)
			spawnWaveRider(in_logicEngine, LogicEngine.SCREEN_WIDTH/3);
		
		if(i_stepCounter == 1000)
			spawnWaveRider(in_logicEngine, (LogicEngine.SCREEN_WIDTH*2)/3);
		
		
		if(i_stepCounter == 1200)
		{
			spawnWaveRider(in_logicEngine, LogicEngine.SCREEN_WIDTH/3);
			spawnWaveRider(in_logicEngine, (LogicEngine.SCREEN_WIDTH*2)/3);
		}
		
		
		if(i_stepCounter == 1400)
		{
			spawnWaveRider(in_logicEngine, LogicEngine.SCREEN_WIDTH/3);
			spawnWaveRider(in_logicEngine, (LogicEngine.SCREEN_WIDTH*2)/3);
		}

		///////////////////////// TUNNEL 2////////////////////////////////////////
		if(i_stepCounter > 1550 && i_stepCounter < 1700)
		{
			if(i_stepCounter%10 == 0 )
			{
				GameObject slime = in_manager.spawnSlime(in_logicEngine, myWalls.getMiddleOfTunnel() + (r.nextDouble() -0.5 )*100);
				
				slime.v.setMaxVel(16);
				for(int i=0;i<slime.stepHandlers.size();i++)
					if(slime.stepHandlers.get(i) instanceof FlyStraightStep)
						((FlyStraightStep)slime.stepHandlers.get(i)).setXY(0, -8);
			}
			
			//wave cave
			if(i_stepCounter%4 == 0 )
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
			
			if(i_stepCounter % 16 ==0)
				myWalls.narrowTunnel();
		}
		else
		if(i_stepCounter > 1550 && i_stepCounter <= 1800)
		{
			if(i_stepCounter % 16 ==0)
				myWalls.openTunnel();
		}
	
	
		if(i_stepCounter >= 1800 && i_stepCounter <= 2600)
		{
			if(i_stepCounter%120==0)
				this.spawnBubble(in_logicEngine, (float) (r.nextDouble() * in_logicEngine.SCREEN_WIDTH));
		
			if(i_stepCounter%20==0)
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
			
		}
		
		if(i_stepCounter == 2650)
			{
				spawnWaveRider(in_logicEngine, LogicEngine.SCREEN_WIDTH/5);
				spawnWaveRider(in_logicEngine, (LogicEngine.SCREEN_WIDTH*2)/4);
				spawnWaveRider(in_logicEngine, (LogicEngine.SCREEN_WIDTH*3)/4);
			}
		
		
		
		if(i_stepCounter == BOSS_STEP)
		{
			myWalls.i_tunnelSpeed = 8;
			myWalls.narrowTunnel();
		}
		
		if(this.b_bossSpawned)
			stepBoss(in_logicEngine);
			
		if(i_stepCounter == BOSS_STEP+70)
		{
			myWalls.setBlockSpeed(in_logicEngine, 0,i_stepCounter);
			myWalls.makeHarmless();
			this.b_bossSpawned = true;
			in_logicEngine.objectsEnemies.add(spawnBoss(in_logicEngine));
		}
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
		
		if(in_logicEngine.objectsEnemies.size() == 0 && b_bossSpawned)
			i_levelEndCountdown--;
			
		myWalls.spawnBlockIfNeeded(in_logicEngine, i_stepCounter);
		
		return super.stepLevel(in_manager, in_logicEngine);
	}

	Drawable d_eye;
	int i_stepBoss=0;
	int i_bossBubbleEvery=150;
	
	Arrive boss_arrive = new Arrive();
	
	private GameObject spawnBoss(LogicEngine in_logicEngine)
	{
		//75 x 93 - redcube.png - row 1 column 0
		//eye 3x0 -  8x8
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_logicEngine.SCREEN_WIDTH/2,LogicEngine.rect_Screen.getHeight()+50,0);
		boss = go;
		
		boss.i_animationFrameRow = 1;
		boss.i_animationFrame =0;
		boss.i_animationFrameSizeWidth =75;
		boss.i_animationFrameSizeHeight =93;
		
		boss.v.setMaxForce(1);
		boss.v.setMaxVel(5);
		boss.stepHandlers.add( new BounceOfScreenEdgesStep());
		
		
		boss_arrive.setAttribute("arrivedistance", "50", null);
		boss.stepHandlers.add( new CustomBehaviourStep(boss_arrive));
		boss.isBoss = true;
		
		
			
		HitpointShipCollision c =  new HitpointShipCollision(boss, 150, 40, true,1);
		
		if(Difficulty.isMedium())
			c.f_numberOfHitpoints = 200;
		
		if(Difficulty.isHard())
			c.f_numberOfHitpoints = 250;
		
		if(Difficulty.isMedium())
			i_bossBubbleEvery = 125;
		
		if(Difficulty.isHard())
			i_bossBubbleEvery = 100;
		
		
		c.addHitpointBossBar(in_logicEngine);
		c.setExplosion(Utils.getBossExplosion(boss));
		boss.collisionHandler = c;
		
		boss.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		//initial velocity of first one 
		boss.v.setVel(new Vector2d(0,-5));
		
		GameObject Tadpole1 = spawnTadpole(in_logicEngine);
		GameObject Tadpole2 = spawnTadpole(in_logicEngine);
		
		GameObject Bubble = null;
		
		
		Bubble = spawnBossBubble(in_logicEngine, 0, 3);
		
		
		
		Tadpole1.v.setVel(new Vector2d(-10,5));
		Tadpole2.v.setVel(new Vector2d(10,5));
		Bubble.v.setVel(new Vector2d(0,-5));
		
		LaunchShipsStep l1 = new LaunchShipsStep(Tadpole1 , 50, 5, 1, false);
		LaunchShipsStep l2 = new LaunchShipsStep(Tadpole2, 50, 5, 1, true);
		LaunchShipsStep l3 = new LaunchShipsStep(Bubble, i_bossBubbleEvery, 1, 1, true);
		l1.b_addToBullets = true;
		l2.b_addToBullets = true;
		
		boss.stepHandlers.add(l1);
		boss.stepHandlers.add(l2);
		boss.stepHandlers.add(l3);
		
		
		
		d_eye = new Drawable();
		d_eye.i_animationFrameSizeHeight=8;
		d_eye.i_animationFrameSizeWidth=8;
		d_eye.i_animationFrameRow = 3;
		d_eye.str_spritename = "data/"+GameRenderer.dpiFolder+"/eye.png";
		
		boss.visibleBuffs.add(d_eye);
		
		return boss;
	}
	
	
	private void stepBoss(LogicEngine in_logicEngine)
	{
		
		
		d_eye.f_forceRotation = 195f+ Utils.getAngleOfRotation(Utils.getVectorToClosestPlayer(in_logicEngine,boss.v.getPos())); 
		d_eye.i_animationFrame = boss.i_animationFrame;
		
		//blink
		if(i_stepBoss == i_bossBubbleEvery-15)
			boss.visibleBuffs.clear();
		
		if(i_stepBoss == i_bossBubbleEvery-10)
			boss.visibleBuffs.add(d_eye);
		
		if(i_stepBoss == i_bossBubbleEvery-5)
			boss.visibleBuffs.clear();
		
		if(i_stepBoss == i_bossBubbleEvery)
			boss.visibleBuffs.add(d_eye);
		
		//every 50 set a new target
		if(i_stepBoss%50 == 0)
		{
			
			boss_arrive.setTargetXY(r.nextFloat()*LogicEngine.SCREEN_WIDTH,r.nextFloat()*LogicEngine.SCREEN_HEIGHT);
		}
		
		if(i_stepBoss == i_bossBubbleEvery)
			i_stepBoss =0;
		
		i_stepBoss++;
	}
	
	//recursive method to spawn boss and all splits
	private GameObject spawnBossBubble(LogicEngine toRunIn, int i_level, int i_maxLevel)
	{
		float f_sizeMultiplier = 1 - ((float)i_level/(float)i_maxLevel);
		
		if(i_level == i_maxLevel)
			return null;
		else
		{
			GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",toRunIn.SCREEN_WIDTH/2,LogicEngine.rect_Screen.getHeight()+50,0);
			go.i_animationFrameRow = 0;
			go.i_animationFrame =1;
			go.i_animationFrameSizeWidth =40;
			go.i_animationFrameSizeHeight =37;
			go.f_forceScaleX = 2f * f_sizeMultiplier;
			go.f_forceScaleY =  2f * f_sizeMultiplier;
			go.v.setMaxForce(1);
			go.v.setMaxVel(5);
			go.stepHandlers.add( new BounceOfScreenEdgesStep());
	
			
			go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
			//initial velocity of first one 
			go.v.setVel(new Vector2d(0,-5));
			
			
			
			go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
			
			
			
			
			//if is last one
			if(i_level == i_maxLevel - 1)
			{
				HitpointShipCollision collision =  new HitpointShipCollision(go,1, 40.0 * f_sizeMultiplier);
				go.collisionHandler = collision;
				collision.setSimpleExplosion();
				

				return go;
			}
			else //has children
			{
				SplitCollision collision =  new SplitCollision(go,2, 15.0 * f_sizeMultiplier);
				collision.setSimpleExplosion();
				
				go.collisionHandler = collision;
				
				//add children				
				for(int i=0;i<4;i++)
				{
					GameObject go2 = spawnBossBubble(toRunIn,i_level+1,i_maxLevel);
					
					if(i==0)
						go2.v.setVel(new Vector2d(-5,-5));
					if(i==1)
						go2.v.setVel(new Vector2d(5,-5));
					if(i==2)
						go2.v.setVel(new Vector2d(-5,5));
					if(i==3)
						go2.v.setVel(new Vector2d(5,5));
				
					collision.splitObjects.add(go2);
				}
				
			}
			
			return go;		
		}
	}
	
	
	private GameObject spawnWaveRider(LogicEngine in_logicEngine,float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,in_logicEngine.SCREEN_HEIGHT+25,0);
		
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		go.i_animationFrameSizeWidth=40;
		go.i_animationFrameSizeHeight=43;
		go.i_animationFrameRow = 1;
		go.i_animationFrame = 0;
		//go.rotateToV = true;
		
		go.v.setMaxForce(2.5);
		go.v.setMaxVel(0.5);
		
		
		
		CustomBehaviourStep cb = new CustomBehaviourStep(new Wander(-2.5,2.5,20,0.1));
		go.stepHandlers.add( cb);
		go.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-2)));
		
		HitpointShipCollision collision = new HitpointShipCollision(go, 15, 25);
		
		if(Difficulty.isMedium() || Difficulty.isHard())
		{
			collision.f_numberOfHitpoints = 20;
			go.v.setMaxVel(0.3);
		}
		
		collision.setExplosion(Utils.getMiniBossExplosion(go));
		go.collisionHandler = collision;
		
		LaunchShipsStep l1 = new LaunchShipsStep( spawnTadpole(in_logicEngine), 50, 5, 1, false);
		LaunchShipsStep l2 = new LaunchShipsStep( spawnTadpole(in_logicEngine), 50, 5, 1, true);
		l1.b_addToBullets = true;
		l2.b_addToBullets = true;
	
		if(Difficulty.isMedium())
		{
			l1.i_launchEvery = 40;
			l2.i_launchEvery = 40;
			
		}
		else
		if(Difficulty.isHard())
		{
			l1.i_launchEvery = 30;
			l2.i_launchEvery = 30;
		}
		go.stepHandlers.add(l1);
		go.stepHandlers.add(l2);
	
		in_logicEngine.objectsEnemies.add(go);
		
		//bullet/
		//75 * 12
		return null;
	}
	
	private GameObject spawnTadpole(LogicEngine in_logicEngine)
	{
		
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",0,0,0);
		
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		go.i_animationFrameSizeWidth=5;
		go.i_animationFrameSizeHeight=16;
		go.i_animationFrameRow = 1;
		go.i_animationFrame = 1;
		go.rotateToV = true;
		go.stepHandlers.add( new SeekNearestPlayerStep(1000));
		go.v.setMaxForce(0.25f);
		go.v.setMaxVel(5);
		go.collisionHandler = new DestroyIfEnemyCollision(go, 5, true);
		
		TimedLifeStep destroyAfter = new TimedLifeStep(100);
		destroyAfter.explosion = Utils.getSimpleExplosion();
		go.stepHandlers.add(destroyAfter);
		
		return go;
	}
	
	private GameObject spawnBubble(LogicEngine in_logicEngine,float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,LogicEngine.rect_Screen.getHeight()+20,0);
		
		//lethal to everyone
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
	//	double xDrift = (Math.random()*2.0)-1.0;
		go.i_animationFrameSizeWidth=40;
		go.i_animationFrameSizeHeight=37;
		go.i_animationFrame = 0;
		
		
		go.v.setMaxForce(0.1);
		go.v.setMaxVel(3);
		go.stepHandlers.add( new SeekNearestPlayerStep(1000));
		
		//hitpoint collision with explosion
		SplitCollision c =  new SplitCollision(go,4, 32.0);
		c.setSimpleExplosion();
		c.c_flashColor = new Color(0,0,1,1);
		
		//---------------SUB BUBBLE---------------
		for(int i=0;i<2;i++)
		{
			GameObject go2 = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
			go2.i_animationFrameRow = 0;
			go2.i_animationFrame =0;
			go2.i_animationFrameSizeWidth =40;
			go2.i_animationFrameSizeHeight =37;
			go2.f_forceScaleX = 0.75f;
			go2.f_forceScaleY = 0.75f;
			go2.allegiance = GameObject.ALLEGIANCES.ENEMIES;
	
			if(i==0)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-1)));
			if(i==1)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-1)));
			if(i==2)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-2)));
			if(i==3)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-2)));
			
			go2.v.setMaxForce(2);
			go2.v.setMaxVel(3);
			go2.stepHandlers.add( new SeekNearestPlayerStep(1000));
			
			SplitCollision c2 =  new SplitCollision(go2,4, 25.0);
			
			//-------------SUB-SUB BUBBLE--------------
			for(int j=0;j<4;j++)
			{
				GameObject go3 = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
				go3.i_animationFrameRow = 0;
				go3.i_animationFrame =0;
				go3.i_animationFrameSizeWidth =40;
				go3.i_animationFrameSizeHeight =37;
				go3.f_forceScaleX = 0.5f;
				go3.f_forceScaleY = 0.5f;
				go3.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
				if(j==0)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-1)));
				if(j==1)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-2)));
				if(j==2)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-1)));
				if(j==3)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-2)));
				
				
				HitpointShipCollision c3 =  new HitpointShipCollision(go3,1, 15.0);
				
				c3.setSimpleExplosion();
				
				go3.collisionHandler = c3;
				
				go3.v.setMaxForce(2);
				go3.v.setMaxVel(3);
				go3.stepHandlers.add( new SeekNearestPlayerStep(1000));
				
				c2.splitObjects.add(go3);
			}
			
			//set the bubble to pop into another bubble
			
			c2.c_flashColor = new Color(0,0,1,1);
			c2.setSimpleExplosion();
			c.splitObjects.add(go2);
			go2.collisionHandler = c2;
		}

		go.collisionHandler = c;
		
		in_logicEngine.objectsEnemies.add(go);
		
		return go;
	
	}
}
