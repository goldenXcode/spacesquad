package com.DungeonCrawl.Levels;

import java.util.ArrayList;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DoNothingCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Shooting.FlamerShot;
import com.DungeonCrawl.Shooting.FragmentationProjectile;
import com.DungeonCrawl.Shooting.TurretShot;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level9 implements Level{
	LevelManager levelManager;
	TextDisplaying levelText;
	int i_stepCounter=0;
	boolean b_bossSpawned;
	final int BOSS_STEP =3200;
	int i_levelEndCountdown=100;
	
	GameObject boss=null;
	

	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		if(i_stepCounter == 0)
			in_logicEngine.background.setBackground(1);

		if(i_stepCounter == 0)
			spawnBoss(in_manager, in_logicEngine);
		stepBoss(in_logicEngine);
		
		/*
		if(i_stepCounter%200 == 0)
		spawnSideLiner( in_logicEngine , true);
		
		if(i_stepCounter%200 == 100)
			spawnSideLiner( in_logicEngine , false);
		*/
		
		//level text
		if(i_stepCounter==0)
		{
			levelText = new TextDisplaying("Level 9",(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		
		i_stepCounter++;

		
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	
	ArrayList<GameObject> boss_parts;
	SeekNearestPlayerStep boss_seekPlayerStep;
	CustomBehaviourStep boss_followPathStep;
	FlamerShot boss_flame;
	GameObject boss_head;
	
	private void spawnBoss(LevelManager in_manager, LogicEngine in_logicEngine){
	
		boss_parts = new ArrayList<GameObject>();
		

		boss_seekPlayerStep = new SeekNearestPlayerStep(1000);
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "5", null);
		
		boss_followPathStep = new CustomBehaviourStep(followPathBehaviour);
		 
		
		
		for(int i=0 ; i<10;i++)
		{
			
			GameObject ship = null;
			//spawn tail
			if(i==9)
			{
				//turret
				Drawable turret = new Drawable();
				turret.i_animationFrame = 6;
				turret.i_animationFrameSizeWidth=16;
				turret.i_animationFrameSizeHeight=16;
				turret.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
				turret.f_forceRotation = 90;
				
				ship =  new GameObject("data/"+GameRenderer.dpiFolder+"/dragonhead.png",LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT,1);
				ship.i_animationFrameSizeWidth = 21;
				ship.i_animationFrameSizeHeight = 21;
				
				ship.visibleBuffs.add(turret);
				
				ship.i_animationFrame = 1;
				ship.i_animationFrameRow = 1;
				
				DoNothingCollision c = new DoNothingCollision(ship,10f);
				
				ship.collisionHandler = c;
				ship.v.setMaxForce(5.0f);
				
				ship.shootEverySteps=10;
				
				//String in_bulletImage,float in_collisionRadius, Vector2d in_direction)
				FragmentationProjectile fp = new FragmentationProjectile("data/"+GameRenderer.dpiFolder+"/redbullets2.png",8,new Vector2d(0,-5f));
				fp.setShootAtPlayer(true, 3f);
				fp.setTurret(turret);
				ship.shotHandler = fp; 
				ship.stepHandlers.add(boss_followPathStep);
			}
			else
			//spawn body
			if(i==4)
			{
				ship =  new GameObject("data/"+GameRenderer.dpiFolder+"/dragonbody.png",LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT,1);
				ship.i_animationFrameSizeWidth = 97;
				ship.i_animationFrameSizeHeight = 48;
				ship.i_animationFrame = 0;
				ship.i_animationFrameRow=0;
				ship.rotateToV=true;
				
				
				//make it lay mines
				GameObject mine = in_manager.spawnMine(in_logicEngine, Integer.MIN_VALUE, Integer.MIN_VALUE);
				ship.stepHandlers.add(new LaunchShipsStep(mine, 30, 1, 1, false));
				
				
				
				DoNothingCollision c = new DoNothingCollision(ship,10f);
				ship.collisionHandler = c;
				ship.v.setMaxForce(5.0f);
				ship.stepHandlers.add(boss_followPathStep);
			}
			else
			//spawn head
			if(i==0)
			{
				ship =  new GameObject("data/"+GameRenderer.dpiFolder+"/dragonhead.png",LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT,1);
				ship.i_animationFrameSizeWidth = 20;
				ship.i_animationFrameSizeHeight = 20;
				ship.i_animationFrame = 0;
				ship.rotateToV=true;
				
				//destroy ships that get too close
				HitpointShipCollision hps=null; 		
				hps = new HitpointShipCollision(ship, 250, 18);
				hps.addHitpointBossBar(in_logicEngine);
				
				hps.setExplosion(Utils.getBossExplosion(ship));
				
				ship.collisionHandler = hps; 

				//flame breath
				boss_flame = new FlamerShot(ship);
				ship.shotHandler = boss_flame;
				ship.shootEverySteps=2;
				

				//give the leading head a chase order
				boss_head = ship;
				ship.stepHandlers.add(boss_seekPlayerStep);
				ship.v.setMaxForce(2.0f);
			}
			else	
			{
				//skip 2 after body to give it some space
				
				
				ship =  new GameObject("data/"+GameRenderer.dpiFolder+"/dragonhead.png",LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT,1);
				ship.i_animationFrameSizeWidth = 21;
				ship.i_animationFrameSizeHeight = 21;
				
				ship.i_animationFrame = 1;
				
				if(i==5 )
					ship.i_animationFrame = 3;
				DoNothingCollision c = new DoNothingCollision(ship,10f);
				
				ship.collisionHandler = c;
				ship.v.setMaxForce(5.0f);
				
				ship.stepHandlers.add(boss_followPathStep);
			}
			
			
			
			
			ship.v.setMaxVel(5.0f);
			
				
			ship.allegiance= GameObject.ALLEGIANCES.ENEMIES;
			ship.isBoss = true;
			
			boss_parts.add(ship);
			
			in_logicEngine.objectsEnemies.add(ship);	
		}
		
	}
private void stepBoss(LogicEngine in_logicEngine) {
		//if its dead then return
		if(boss_parts.size()==0)
		{
			i_levelEndCountdown--;
			
			return;
		}
	
		boss_flame.a_flameVectors.clear();
		
		Vector2d flamevector = new Vector2d(boss_head.v.getVel());
		flamevector.normalize();
		flamevector.scale(8);
		
		boss_flame.a_flameVectors.add(flamevector);
		
		
		if(in_logicEngine.objectsEnemies.indexOf(boss_head) == -1)
		{
			//prevent flashing
			for(int i=1 ; i< boss_parts.size() ;i++)
			{
				in_logicEngine.objectsEnemies.remove(boss_parts.get(i));
				boss_parts.get(i).collisionHandler.handleDestroy(in_logicEngine);
				
			}
			boss_parts.clear();
		}
		else		
		//give head instruction
		for(int i=boss_parts.size()-1;i>0 ;i--)
		{
			GameObject thisPart = boss_parts.get(i);
			GameObject nextPart = boss_parts.get(i-1);
			
			//follow next piece along
			
			thisPart.v.clearWaypoints();
			thisPart.v.addWaypoint(nextPart.v.getPos());
			
		}
		
		return;
	}
	
	private void spawnSideLiner(LogicEngine in_logicEngine, boolean b_isRight) 
	{
		//turret
		Drawable turret = new Drawable();
		turret.i_animationFrame = 6;
		turret.i_animationFrameSizeWidth=16;
		turret.i_animationFrameSizeHeight=16;
		turret.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
		turret.f_forceRotation = 90;
		turret.f_offsetY = 8;
		turret.f_offsetX = 1;
		
		//spawn at left edge
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/sideliner.png",0,LogicEngine.SCREEN_HEIGHT,20);
		
		if(b_isRight)
		{
			ship.v.setX(LogicEngine.SCREEN_WIDTH - 40);
			ship.b_mirrorImageHorizontal = true;
		}
		else	
			ship.v.setX(40);
		
		ship.i_animationFrameSizeWidth = 32;
		ship.i_animationFrameSizeHeight = 64;
		ship.i_animationFrame = 0;
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		ship.v.setMaxForce(2.0f);
		ship.v.setMaxVel(6.0f);
		
		ship.visibleBuffs.add(turret);
		//fly down
		ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1f)));
		
		
		//String in_bulletImage,float in_collisionRadius, Vector2d in_direction)
		FragmentationProjectile fp = new FragmentationProjectile("data/"+GameRenderer.dpiFolder+"/redbullets2.png",8,new Vector2d(0,-5f));
		fp.setShootAtPlayer(true, 3f);
		ship.shotHandler = fp; 
		
		
		
		if(Difficulty.isEasy())
			ship.collisionHandler = new HitpointShipCollision(ship, 50, 32,true);
		else		
		if(Difficulty.isMedium())
		{
			ship.shootEverySteps=15;
			ship.collisionHandler = new HitpointShipCollision(ship, 50, 32,true);
		}
		if(Difficulty.isHard())
		{
			ship.shootEverySteps=10;
			ship.collisionHandler = new HitpointShipCollision(ship, 80, 32,true);
		}
		//destroy ships that get too close
		
		
		
		in_logicEngine.objectsEnemies.add(ship);	

		
	}
	@Override
	public void gameOver(LogicEngine in_logicEngine) {

		
	}
}
