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
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Shooting.TurretShot;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;

import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.behaviors.Seek;
import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level7 implements Level{
	LevelManager levelManager;
	TextDisplaying levelText;
	int i_stepCounter=0;
	boolean b_bossSpawned;
	final int BOSS_STEP =3000;
	int i_levelEndCountdown=100;
	Random r = new Random();
	
	GameObject boss=null;
	
	Walls myWalls = new Walls();
	
	public Level7()
	{
		i_stepCounter = 0;
	}
	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		if(i_stepCounter ==0 )
			in_logicEngine.background.setBackground(2);
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);

		//spawn powerups
		if(i_stepCounter%400 == 0)
			in_manager.spawnRandomPowerup(in_logicEngine, (float) (Math.random()*LogicEngine.SCREEN_WIDTH));
		
		//spawn clouds
		if(i_stepCounter%40 ==0 && i_stepCounter < 1000)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));


		if(i_stepCounter < 340 && i_stepCounter%20 ==0)
		{
			GameObject asteroid = in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
			asteroid.c_Color = new Color(0.9f,0,0,1);
			((HitpointShipCollision)(asteroid.collisionHandler)).c_initialColour = new Color(0.9f,0,0,1);
			
		}
		
		
		//waves of pathfinders 0-500
		if(i_stepCounter >= 40  && i_stepCounter < 300)
		{
			
			if(i_stepCounter % 80 == 10)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 + 40));
			else
			if(i_stepCounter % 80 == 13)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 + 55));
			else
			if(i_stepCounter % 80 == 16)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 + 70));
			else
			if(i_stepCounter % 80 == 19)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 + 85));
			else
			if(i_stepCounter % 80 == 22)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 + 100));
			else
				
				

			if(i_stepCounter % 80 == 40)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 - 100));
			else
			if(i_stepCounter % 80 == 43)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 - 85));
			else
			if(i_stepCounter % 80 == 46)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 - 70));
			else
			if(i_stepCounter % 80 == 49)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 - 55));
			else
			if(i_stepCounter % 80 == 52)
				in_manager.pathFinderStraightDown(in_logicEngine, (int) (LogicEngine.SCREEN_WIDTH/2 - 40));
		
		}
		
		//pathFinderStraightDown
		////////////////////////////////////500-1500 CARRIERS AND VARIOUS SHIPS////////////////////////////		
		if(i_stepCounter >= 300  && i_stepCounter < 1500)
		{

			if(i_stepCounter==350)
				this.spawnCarrier(in_logicEngine, LogicEngine.SCREEN_WIDTH /2,CARRIER_TYPE.PATHFINDERS_BOTH_SIDES);
			
			if(i_stepCounter %200 == 0 )
			{
				if(Math.random()<0.5f)
					in_manager.spawnTurretShip(in_logicEngine, LogicEngine.SCREEN_WIDTH-60);
				else
					in_manager.spawnTurretShip(in_logicEngine, 60);
			}
			
			if(i_stepCounter==1300)
				this.spawnCarrier(in_logicEngine, LogicEngine.SCREEN_WIDTH /3,CARRIER_TYPE.PATHFINDERS_BOTH_SIDES);
		}
		
		
		////////////////////////////////////1000-2000 WALLS AND LAZORS////////////////////////////
		if(i_stepCounter>=1400 && i_stepCounter<2500)
		{
			//narrow the steps
			if(i_stepCounter>=1400 && i_stepCounter%32==0 && i_stepCounter < 1700)
				myWalls.narrowTunnel();
			
			if(i_stepCounter == 1400)
			{
				myWalls.setBlockSpeed(in_logicEngine, 8,i_stepCounter);
				myWalls.setColour(Color.RED);
			}
			
			if(i_stepCounter > 1600 && i_stepCounter%50 == 0)
			{
				in_manager.spawnSeeker(in_logicEngine, (float) (myWalls.getMiddleOfTunnel() - 40f));
				in_manager.spawnSeeker(in_logicEngine, (float) ( myWalls.getMiddleOfTunnel() - 20f));
				in_manager.spawnSeeker(in_logicEngine, (float) ( myWalls.getMiddleOfTunnel() + 40f));
				in_manager.spawnSeeker(in_logicEngine, (float) ( myWalls.getMiddleOfTunnel() + 20f));
			}
			
			//move cave around
			if(i_stepCounter%16 == 0 )
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
			
			if(i_stepCounter%150 ==0)
			{
				if(Math.random()<0.5f)
					in_manager.spawnTurretShip(in_logicEngine, myWalls.getMiddleOfTunnel() -20);
				else
					in_manager.spawnTurretShip(in_logicEngine, myWalls.getMiddleOfTunnel() +20);
			}
			
			if(i_stepCounter>=2300 &&  i_stepCounter%32==0 && i_stepCounter < 2500)
				myWalls.openTunnel();
			
			
			if(i_stepCounter%100 == 0 && i_stepCounter>=1600 && i_stepCounter < 2300)
			{ 
				GameObject shooter = myWalls.spawnShooterBlock(in_logicEngine);
				shooter.c_Color = Color.RED;
			}
			
		}
		////////////////////////////////////2000-3000 Carrier pairs and asteroids////////////////////////////
		if(i_stepCounter >= 2500 && i_stepCounter <= 3000 )
		{	
			if(i_stepCounter == 2500)
			{
				finalCarrier1 = this.spawnCarrier(in_logicEngine, LogicEngine.SCREEN_WIDTH/3,CARRIER_TYPE.PATHFINDERS_LEFT_ONLY);
				finalCarrier2 = this.spawnCarrier(in_logicEngine, (int)(LogicEngine.SCREEN_WIDTH/1.5),CARRIER_TYPE.PATHFINDERS_RIGHT_ONLY);
				
				finalCarrier1.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1.5)));
				finalCarrier2.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1.5)));
				
				finalCarrier1.c_Color = new Color(0.8f,0.8f,0.8f,1.0f);
				finalCarrier2.c_Color = new Color(0.8f,0.8f,0.8f,1.0f);
			}
		}
		
		if(i_stepCounter == BOSS_STEP)
			spawnBoss(in_logicEngine, in_manager);
		
		myWalls.spawnBlockIfNeeded(in_logicEngine, i_stepCounter);
		
		//level text
		if(i_stepCounter==0)
		{
			SoundEffects.getInstance().levels[7].play(SoundEffects.SPEECH_VOLUME);
			levelText = new TextDisplaying("Level 7",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
		
		if(i_stepCounter >= BOSS_STEP)
			if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
			{
				i_levelEndCountdown--;
			}
			else
				this.stepBoss(in_logicEngine);
			

		i_stepCounter++;

		
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	
	enum CARRIER_TYPE
	{
		PATHFINDERS_BOTH_SIDES,
		PATHFINDERS_RIGHT_ONLY,
		PATHFINDERS_LEFT_ONLY
		
	}
	
	GameObject finalCarrier1;
	GameObject finalCarrier2;
	
	private void spawnBoss(LogicEngine in_logicEngine,LevelManager in_manager)
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/thrusterboss.png",LogicEngine.SCREEN_WIDTH - 100, LogicEngine.SCREEN_HEIGHT+60, 0);
		boss = ship;
		
		ship.i_animationFrame=5;
		ship.i_animationFrameRow=0;
		ship.i_animationFrameSizeWidth=32;
		ship.i_animationFrameSizeHeight=32;
		
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		ship.v.setMaxVel(3);
		ship.v.setMaxForce(1);
		
		bossTarget = null;
		bossTarget = new Arrive();
		bossTarget.setActiveDistance(Integer.MAX_VALUE);

		ship.c_Color = new Color(1,1,1,1);
		
		ship.rotateToV=true;
		
		HitpointShipCollision c =  new HitpointShipCollision(ship, 90, 9, true,6);
		c.setExplosion(Utils.getBossExplosion(ship));
		c.addHitpointBossBar(in_logicEngine);

		bossSpawnLocations = new Point2d[4];
		
		bossSpawnLocations[0] = new Point2d(LogicEngine.SCREEN_WIDTH - 50 , LogicEngine.SCREEN_HEIGHT - 50);
		bossSpawnLocations[1] = new Point2d( 50 ,   LogicEngine.SCREEN_HEIGHT - 50);
		bossSpawnLocations[2] = new Point2d( 50 ,  50);
		bossSpawnLocations[3] = new Point2d(LogicEngine.SCREEN_WIDTH - 50 ,  50);
		
		
		bossTarget.setTargetXY(bossSpawnLocations[2].getX(),bossSpawnLocations[2].getY());
		currentTarget = bossSpawnLocations[2];
		
		ship.isBoss = true;
		
		ship.collisionHandler = c;
		
		CustomBehaviourStep b = new CustomBehaviourStep(bossTarget);
		ship.stepHandlers.add(b);
		
		//make it lay mines
		GameObject mine = in_manager.spawnMine(in_logicEngine, Integer.MIN_VALUE, Integer.MIN_VALUE);
		mine.v.setMaxForce(0.4);
		mine.v.setMaxVel(0.4);
		mine.stepHandlers.clear();
		mine.stepHandlers.add(new SeekNearestPlayerStep(1000));
		ship.stepHandlers.add(new LaunchShipsStep(mine, 30, 1, 1, false));
		
		
		
		in_logicEngine.objectsEnemies.add(boss);
		
		//fly carries off bottom of screen rapidly
		if(finalCarrier1 != null)
		{
			finalCarrier1.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-4f)));
			finalCarrier2.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-4f)));
		}
		
		b_bossSpawned = true;
	}
	
	Arrive bossTarget;
	Point2d[] bossSpawnLocations;
	
	        
	Point2d currentTarget;
	
	int i_invisibleCounter = 0;
	int i_invisibleDuration = 20;
	
	boolean b_vanishing=false;
	
	private void stepBoss(LogicEngine in_logicEngine)
	{
		
		boss.i_animationFrame=5;
		
		if(b_vanishing)
		{
			//fade
			if(boss.c_Color.a >0)
			{
				boss.c_Color.a-=0.1f;
				i_invisibleCounter = i_invisibleDuration;
			}
			else
			{
				//if is invisible
				if(i_invisibleCounter > 0)
				{
					//take it off the game board
					i_invisibleCounter--;
					boss.v.setX(Integer.MAX_VALUE);
				}
				else
				{
					//rematerialse it
				
					//teleport and make visible slowly
					int i_newLocation = r.nextInt(4);
					int i_newTarget = r.nextInt(4);

					//dont fly to same node as spawns at
					while(i_newLocation == i_newTarget ||
							(i_newTarget == 2 && i_newLocation == 3) ||
							(i_newTarget == 3 && i_newLocation == 2) )
					{
						
						i_newTarget = r.nextInt(4);
						
					}
					
					//set new location 
					boss.v.setX(bossSpawnLocations[i_newLocation].getX());
					boss.v.setY(bossSpawnLocations[i_newLocation].getY());
					
					//set new target
					bossTarget.setTargetXY(bossSpawnLocations[i_newTarget].getX(), bossSpawnLocations[i_newTarget].getY());
					currentTarget = bossSpawnLocations[i_newTarget]; 
					
					boss.c_Color.a = 1.0f;
					
					b_vanishing = false;
				}
			}
		}
		
		//if at waypoint
		if(boss.v.getPos().sub(currentTarget).length() < 50)
		{
			//vanish
			b_vanishing = true;
		}
		
	}
	
	public GameObject spawnCarrier(LogicEngine in_logicEngine, float in_x , CARRIER_TYPE in_type)
	{
		
		//spawn left and right halves next to one another
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/thrusterboss.png",in_x,LogicEngine.SCREEN_HEIGHT+64,30);
		
		
		//set animation frame
		ship.i_animationFrame=4;
		ship.i_animationFrameRow=0;
		ship.i_animationFrameSizeWidth=32;
		ship.i_animationFrameSizeHeight=132;
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		//pause at the first waypoint
		followPathBehaviour.waitAtWaypoint(1, 50);
		
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "50", null);
		
		followPathBehaviour.waitAtWaypoint(1, 300);
		
		//go straight down then split
		ship.v.addWaypoint(new Point2d(in_x, LogicEngine.SCREEN_HEIGHT/1.5));
		ship.v.addWaypoint(new Point2d(in_x,-100));
		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		
		//turret
		//turret
		Drawable turret = new Drawable();
		turret.i_animationFrame = 6;
		turret.i_animationFrameSizeWidth=16;
		turret.i_animationFrameSizeHeight=16;
		turret.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
		turret.f_forceRotation = 90;
		ship.shotHandler = new TurretShot(turret,"data/"+GameRenderer.dpiFolder+"/redbullets.png",6,5.0f);
		ship.visibleBuffs.add(turret);
		
		ship.v.setMaxForce(1);
		ship.v.setMaxVel(1);
		
		ship.stepHandlers.add(b);
		
		HitpointShipCollision s;
		if(!Difficulty.isHard())
			 s = new HitpointShipCollision(ship, 50, 32);
		else
			 s = new HitpointShipCollision(ship, 50, 32);
			
		s.setExplosion(Utils.getBossExplosion(ship));
		ship.collisionHandler = s; 
	
		//TODO:launch ships handler for shooting
		if(in_type == CARRIER_TYPE.PATHFINDERS_BOTH_SIDES)
		{
			ship.stepHandlers.add(new LaunchShipsStep(pathFinder(true), 50, 5, 5,true));
			ship.stepHandlers.add(new LaunchShipsStep(pathFinder(false), 50, 5, 5,false));
		}
		else
			if(in_type == CARRIER_TYPE.PATHFINDERS_RIGHT_ONLY)
			{
				ship.stepHandlers.add(new LaunchShipsStep(pathFinder(true), 50, 5, 5,true));
			}
			else
				if(in_type == CARRIER_TYPE.PATHFINDERS_LEFT_ONLY)
				{
					ship.stepHandlers.add(new LaunchShipsStep(pathFinder(false), 50, 5, 5,false));
				}
						 
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		in_logicEngine.objectsEnemies.add(ship);
		
		return ship;
	
	}
	
	
	private GameObject pathFinder(boolean b_goRight)
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",(LogicEngine.SCREEN_WIDTH+10),LogicEngine.SCREEN_HEIGHT-10,0);
		
		ship.setRotateToVelocity(true);
		ship.i_animationFrame=0;
		ship.i_animationFrameSizeWidth=16;
		ship.i_animationFrameSizeHeight=16;
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		followPathBehaviour.setInfluence(1);
		
		followPathBehaviour.setAttribute("arrivedistance", "50", null);
		
		if(b_goRight)
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2 + 75,LogicEngine.SCREEN_HEIGHT/1.5));
		else
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2 - 75,LogicEngine.SCREEN_HEIGHT/1.5));
		
		ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2,25));
		
		if(b_goRight)
			ship.v.addWaypoint(new Point2d(50,50));
		else
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,50));
		
		
		if(b_goRight)
			ship.v.addWaypoint(new Point2d(50,Integer.MAX_VALUE));
		else
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,Integer.MAX_VALUE));
		

		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		
		
		ship.v.setMaxForce(2.5);
		ship.v.setMaxVel(7.5);
		
	
		
		ship.stepHandlers.add(b);		
		ship.collisionHandler = new DestroyIfEnemyCollision(ship, 6, true);
		
		
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		return ship;
	}


}
