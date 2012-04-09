package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.SeekNearestEnemyStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class SplitShot implements ShotHandler {
	
	double i_bulletSpeed;
	String str_path;
	
	public boolean b_homing = false;
	
	public SplitShot( String in_path, double in_bulletSpeed)
	{
		i_bulletSpeed = in_bulletSpeed;
		str_path = in_path;
	}
	
	@Override
	public void shoot(LogicEngine toRunIn,GameObject in_objectFiring) {
		
		//TODO make this split beyond 2
		for(int i=0;i<2;i++)
		{
			GameObject bullet = new GameObject(str_path,(float)in_objectFiring.v.getX() + (float)(in_objectFiring.v.getBoundingBox().getWidth()/2.0),(float)in_objectFiring.v.getY(),0);
			
			FlyStraightStep flyStep =null;
			
			//shoot left or right
			if(i==0)
				flyStep =  new FlyStraightStep(new Vector2d(-4.5,i_bulletSpeed));
			else
				flyStep = new FlyStraightStep(new Vector2d(4.5,i_bulletSpeed));
			
			bullet.stepHandlers.add(flyStep);
			
			if(b_homing)
				if(in_objectFiring.allegiance == GameObject.ALLEGIANCES.PLAYER )
				{
					bullet.stepHandlers.add(new SeekNearestEnemyStep(1000,0.5f));
					bullet.v.setMaxVel(i_bulletSpeed);
				}
				else
				{
					bullet.stepHandlers.add(new SeekNearestPlayerStep(1000));
					bullet.v.setMaxVel(i_bulletSpeed);
				}
		
			bullet.collisionHandler = new DestroyIfEnemyCollision(bullet,7.0,false);
			//bullet inherits the allegiance of whoever fired it
			bullet.allegiance = in_objectFiring.allegiance;
			
			bullet.v.setMaxVel(7.0f);
			
			//if its enemy shots just add it to enemy list
			if(bullet.allegiance==GameObject.ALLEGIANCES.ENEMIES)
				toRunIn.objectsEnemyBullets.add(bullet);
			else
				toRunIn.objectsPlayerBullets.add(bullet);
		}
	}

}
