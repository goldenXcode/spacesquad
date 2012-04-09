package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.SeekNearestEnemyStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class StraightLineShot implements ShotHandler {

	Vector2d direction = null;
	String str_bulletImage;
	float f_collisionRadius;
	
	boolean b_shootForwards;
	float f_shootForwardsBulletSpeed;
	
	public boolean b_homing = false;
	
	public void setShootForwards(boolean in_shootForwards,float f_bulletSpeed)
	{
		b_shootForwards = in_shootForwards;
		f_shootForwardsBulletSpeed = f_bulletSpeed;
	}
	
	public StraightLineShot(String in_bulletImage,float in_collisionRadius, Vector2d in_direction)
	{
		direction = in_direction;
		str_bulletImage = in_bulletImage;
		f_collisionRadius = in_collisionRadius;
	}
	@Override
	public void shoot(LogicEngine toRunIn,GameObject in_objectFiring) {
		GameObject bullet = new GameObject(str_bulletImage,(float)in_objectFiring.v.getX() ,(float)in_objectFiring.v.getY(),0);
		
		
		if(b_shootForwards)
		{
			direction = in_objectFiring.v.getVel();
			direction.normalize();
			direction.scale(f_shootForwardsBulletSpeed);
		}
		
		
		bullet.v.setMaxVel(direction.length());
		bullet.stepHandlers.add(new FlyStraightStep(direction));
		
		if(b_homing)
			if(in_objectFiring.allegiance == GameObject.ALLEGIANCES.PLAYER )
			{
				bullet.stepHandlers.add(new SeekNearestEnemyStep(1000,0.5f));
				bullet.v.setMaxVel(direction.length());
			}
			else
			{
				bullet.stepHandlers.add(new SeekNearestPlayerStep(1000));
				bullet.v.setMaxVel(direction.length());
			}
		
		bullet.collisionHandler = new DestroyIfEnemyCollision(bullet,f_collisionRadius,false);
		//bullet inherits the allegiance of whoever fired it
		bullet.allegiance = in_objectFiring.allegiance;
	
		bullet.i_animationFrameSizeWidth=8;
		bullet.i_animationFrameSizeHeight=8;
		
		if(bullet.allegiance==GameObject.ALLEGIANCES.ENEMIES)
			toRunIn.objectsEnemyBullets.add(bullet);
		else
			toRunIn.objectsPlayerBullets.add(bullet);
		
	}

}
