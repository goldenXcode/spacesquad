package com.DungeonCrawl.Collisions;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StepHandler;

public class DoNothingCollision implements CollisionHandler{

	double d_collisionRadius;
	
	private GameObject forShip;
	
	public DoNothingCollision(GameObject in_forShip, double in_collisionRadius)
	{
		forShip = in_forShip;
		d_collisionRadius = in_collisionRadius;
	}
	
	@Override
	public boolean handleCollision(GameObject collidingWith, LogicEngine toRunIn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getCollisionRadius() {
		// TODO Auto-generated method stub
		return d_collisionRadius;
	}

	@Override
	public CollisionHandler cloneForShip(GameObject in_ship)
			throws CloneNotSupportedException {
		DoNothingCollision toReturn = new DoNothingCollision(in_ship, d_collisionRadius);
		
		
		return toReturn;
	}
	@Override
	public StepHandler clone()
	{
		return this;
	}
	

	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleDestroy(LogicEngine toRunIn) {
		
		GameObject explosion = Utils.getSimpleExplosion();
		explosion.v.setX(forShip.v.getX());
		explosion.v.setY(forShip.v.getY());
		toRunIn.objectsOverlay.add(explosion);
		
		SoundEffects.getInstance().explosion.play(0.5f);
		
		return true;
	}

}
