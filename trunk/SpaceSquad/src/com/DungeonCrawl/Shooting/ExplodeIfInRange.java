package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class ExplodeIfInRange implements ShotHandler{

	String str_bullet = "data/"+GameRenderer.dpiFolder+"/redbullets.png";
	float f_bulletSpeed = 4;
	float f_explodeDistance = 80;
	boolean b_upOnly=false;
	boolean b_spawnExplosion;
	
	public ExplodeIfInRange(boolean in_spawnExplosion)
	{
		b_spawnExplosion = in_spawnExplosion;
	}
	public ExplodeIfInRange(String in_bulletIcon, float in_explodeRange,boolean in_upOnly,float in_bulletSpeed)
	{
		str_bullet = in_bulletIcon;
		f_explodeDistance = in_explodeRange;
		b_upOnly = in_upOnly;
		f_bulletSpeed = in_bulletSpeed;
	}
	
	private void playerFragmentation(LogicEngine in_theLogicEngine, GameObject in_objectFiring) {
		//if within range
		if(Utils.getDistanceToClosestEnemyShip(in_theLogicEngine, in_objectFiring.v.getPos()) < f_explodeDistance)
		{
			//create bullets
			for(int i=0;i<8;i++)
			{
				GameObject bullet = new GameObject(str_bullet,(float)in_objectFiring.v.getX() + (float)(in_objectFiring.v.getBoundingBox().getWidth()/2.0),(float)in_objectFiring.v.getY(),0);
				bullet.v.setMaxVel(f_bulletSpeed);
				
				if(!b_upOnly || (i==0 || i==1 ||  i==7 ))
				{
					//shoot left or right
					if(i==0)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(0,f_bulletSpeed)));
					else
					if(i==1)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(f_bulletSpeed,f_bulletSpeed)));
					else
					if(i==2)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(f_bulletSpeed,0)));
					else
					if(i==3)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(f_bulletSpeed,-f_bulletSpeed)));
					else
					if(i==4)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-f_bulletSpeed)));
					else
					if(i==5)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(-f_bulletSpeed,-f_bulletSpeed)));
					else
					if(i==6)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(-f_bulletSpeed,0)));
					else
					if(i==7)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(-f_bulletSpeed,f_bulletSpeed)));
	
					bullet.collisionHandler = new DestroyIfEnemyCollision(bullet,7.0,false);
					//bullet inherits the allegiance of whoever fired it
					bullet.allegiance = in_objectFiring.allegiance;
					
					in_theLogicEngine.objectsPlayerBullets.add(bullet);
						}
				//destroy the thing firing
				in_objectFiring.i_offScreenCounter = Integer.MAX_VALUE;
				

			}
		}
		
	}
	
	@Override
	public void shoot(LogicEngine in_theLogicEngine, GameObject in_objectFiring) {
		
		//if its a player
		if(in_objectFiring.allegiance == GameObject.ALLEGIANCES.PLAYER)
		{
			playerFragmentation(in_theLogicEngine, in_objectFiring);
		}
		else 
		{
			//if within range
			if(Utils.getDistanceToClosestPlayerShip(in_theLogicEngine, in_objectFiring.v.getPos()) < f_explodeDistance)
			{
				//create explosion
				if(b_spawnExplosion)
				{
					//spawn an explosion
					GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",in_objectFiring.v.getPos().getX(),in_objectFiring.v.getPos().getY(),0);
				
					explosion.i_animationFrame =0;
					explosion.i_animationFrameSizeWidth =16;
					explosion.i_animationFrameSizeHeight =16;
					explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
					in_theLogicEngine.objectsOverlay.add(explosion);
					SoundEffects.getInstance().explosion.play(0.5f);
				}
				
				//create bullets
				for(int i=0;i<8;i++)
				{
					GameObject bullet = new GameObject(str_bullet,(float)in_objectFiring.v.getX() + (float)(in_objectFiring.v.getBoundingBox().getWidth()/2.0),(float)in_objectFiring.v.getY(),0);
					bullet.v.setMaxVel(f_bulletSpeed);
					
					//shoot left or right
					if(i==0)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(0,f_bulletSpeed)));
					else
					if(i==1)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(f_bulletSpeed,f_bulletSpeed)));
					else
					if(i==2)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(f_bulletSpeed,0)));
					else
					if(i==3)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(f_bulletSpeed,-f_bulletSpeed)));
					else
					if(i==4)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-f_bulletSpeed)));
					else
					if(i==5)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(-f_bulletSpeed,-f_bulletSpeed)));
					else
					if(i==6)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(-f_bulletSpeed,0)));
					else
					if(i==7)
						bullet.stepHandlers.add( new FlyStraightStep(new Vector2d(-f_bulletSpeed,f_bulletSpeed)));
	
					
					bullet.collisionHandler = new DestroyIfEnemyCollision(bullet,7.0,false);
					//bullet inherits the allegiance of whoever fired it
					bullet.allegiance = in_objectFiring.allegiance;
					
					if(bullet.allegiance==GameObject.ALLEGIANCES.ENEMIES || bullet.allegiance==GameObject.ALLEGIANCES.LETHAL)
						in_theLogicEngine.objectsEnemyBullets.add(bullet);
					else
						in_theLogicEngine.objectsPlayerBullets.add(bullet);
					
					
					//destroy the thing firing
					in_objectFiring.i_offScreenCounter = Integer.MAX_VALUE;
				}
			}
			

		}	
		
	}

}
