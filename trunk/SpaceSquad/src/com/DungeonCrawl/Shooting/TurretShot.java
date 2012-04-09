package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Steps.FlyStraightStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class TurretShot implements ShotHandler {

	String str_bulletImage;
	float f_collisionRadius;
	Drawable d_turret;
	
	boolean b_shootForwards;
	float f_shootForwardsBulletSpeed;
	float f_bulletSpeed;
	
	public TurretShot(Drawable in_turret,String in_bulletImage, float in_collisionRadius, float in_bulletSpeed)
	{
		d_turret = in_turret;
		str_bulletImage = in_bulletImage;
		f_collisionRadius = in_collisionRadius;
		f_bulletSpeed = in_bulletSpeed;
	}
	
	
	@Override
	public void shoot(LogicEngine toRunIn,GameObject in_objectFiring) {
		GameObject bullet = new GameObject(str_bulletImage,(float)in_objectFiring.v.getX() ,(float)in_objectFiring.v.getY(),0);
		
		//face player
		Vector2d vtoPlayer = Utils.getVectorToClosestPlayer(toRunIn, in_objectFiring.v.getPos());
		
		if(vtoPlayer ==null)
			return;
		//rotate turret
		d_turret.f_forceRotation = Utils.getAngleOfRotation(vtoPlayer);
		
		//calculate bullet movement vector
		vtoPlayer.normalize();
		vtoPlayer.scale(f_bulletSpeed);
		vtoPlayer.setX(-vtoPlayer.getX());
		vtoPlayer.setY(-vtoPlayer.getY());
		
		bullet.stepHandlers.add(new FlyStraightStep(vtoPlayer));
		bullet.collisionHandler = new DestroyIfEnemyCollision(bullet,f_collisionRadius,false);
		//bullet inherits the allegiance of whoever fired it
		bullet.allegiance = in_objectFiring.allegiance;
	
		bullet.i_animationFrameSizeWidth=8;
		bullet.i_animationFrameSizeHeight=8;
		
		bullet.v.setMaxVel(f_bulletSpeed);
		
		if(bullet.allegiance==GameObject.ALLEGIANCES.ENEMIES)
			toRunIn.objectsEnemyBullets.add(bullet);
		else
			toRunIn.objectsPlayerBullets.add(bullet);
		
	}

}
