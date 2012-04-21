package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Steps.FlyStraightStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class FragmentationProjectile implements ShotHandler{

	Vector2d direction;
	String str_bulletImage;
	float f_collisionRadius;
	
	boolean b_shootForwards;
	float f_shootForwardsBulletSpeed;
	
	boolean b_shootAtPlayer = false;
	public void setShootAtPlayer(boolean in_shootAtPlayer, float in_bulletSpeed)
	{
		b_shootAtPlayer = in_shootAtPlayer;
		f_shootForwardsBulletSpeed = in_bulletSpeed;
	}
	public void setShootForwards(boolean in_shootForwards,float f_bulletSpeed)
	{
		b_shootForwards = in_shootForwards;
		f_shootForwardsBulletSpeed = f_bulletSpeed;
	}
	
	public FragmentationProjectile(String in_bulletImage,float in_collisionRadius, Vector2d in_direction)
	{
		direction = in_direction;
		str_bulletImage = in_bulletImage;
		f_collisionRadius = in_collisionRadius;
	}
	
	@Override
	public void shoot(LogicEngine in_toShootIn, GameObject in_objectFiring) {

		GameObject bullet = new GameObject(str_bulletImage,(float)in_objectFiring.v.getX() ,(float)in_objectFiring.v.getY(),0);
		
		if(b_shootForwards)
		{
			direction = in_objectFiring.v.getVel();
			direction.normalize();
			direction.scale(f_shootForwardsBulletSpeed);
		}
		
		if(b_shootAtPlayer)
		{
			//face player
			Vector2d vtoPlayer = Utils.getVectorToClosestPlayer(in_toShootIn, in_objectFiring.v.getPos());
			
			if(vtoPlayer ==null)
				return;
			
			//calculate bullet movement vector
			vtoPlayer.normalize();
			vtoPlayer.scale(f_shootForwardsBulletSpeed);
			vtoPlayer.setX(-vtoPlayer.getX());
			vtoPlayer.setY(-vtoPlayer.getY());
			
			direction = vtoPlayer;
			bullet.stepHandlers.add(new FlyStraightStep(vtoPlayer));
		}
		
		bullet.stepHandlers.add(new FlyStraightStep(direction));
		bullet.collisionHandler = new DestroyIfEnemyCollision(bullet,f_collisionRadius,false);
		bullet.shootEverySteps=2;
		if(in_objectFiring.allegiance == GameObject.ALLEGIANCES.PLAYER)
			bullet.shotHandler = new ExplodeIfInRange("data/"+GameRenderer.dpiFolder+"/fragbullet.png",60,true,3);
		else
			bullet.shotHandler = new ExplodeIfInRange("data/"+GameRenderer.dpiFolder+"/redbullets.png",60,true,3);
		//bullet inherits the allegiance of whoever fired it
		bullet.allegiance = in_objectFiring.allegiance;
	
		bullet.i_animationFrameSizeWidth=8;
		bullet.i_animationFrameSizeHeight=8;
		
		if(bullet.allegiance==GameObject.ALLEGIANCES.ENEMIES)
			in_toShootIn.objectsEnemyBullets.add(bullet);
		else
			in_toShootIn.objectsPlayerBullets.add(bullet);
		
	}
	@Override
	public String getImagePath() {
		
		return str_bulletImage;
	}
	

}
