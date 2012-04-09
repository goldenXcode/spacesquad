package com.DungeonCrawl.Collisions;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StepHandler;

public class DestroyIfEnemyCollision implements CollisionHandler{

	GameObject thisObject;
	boolean b_explodes;
	double collisionRadius;
	
	
	
	public DestroyIfEnemyCollision(GameObject in_for,double in_collisionRadius,boolean in_explodes)
	{
		thisObject = in_for;
		b_explodes = in_explodes;
		collisionRadius = in_collisionRadius;
	}
	//returns true if to destroy 'this' - note that you cannot destroy the thing you are colliding with
	public boolean handleCollision(GameObject collidingWith,LogicEngine toRunIn) {
		
	
		
		if(collidingWith.allegiance!= GameObject.ALLEGIANCES.NONE)
			if(thisObject.allegiance !=  collidingWith.allegiance)
			{
				//collided with enemy so explode and return to delete self
				
				if(b_explodes)
				{
					//spawn an explosion
					GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",thisObject.v.getPos().getX(),thisObject.v.getPos().getY(),0);
				
					explosion.stepHandlers.add(new FlyStraightStep(thisObject.v.getVel()));
					explosion.i_animationFrame =0;
					explosion.i_animationFrameSizeWidth =16;
					explosion.i_animationFrameSizeHeight =16;
					explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
					toRunIn.objectsOverlay.add(explosion);
					SoundEffects.getInstance().explosion.play(0.5f);
				}
				return true;
			}	
		
		
		return false;
	}
	@Override
	public double getCollisionRadius() {
		return collisionRadius;
		
	}
	@Override
	public CollisionHandler cloneForShip(GameObject in_ship) {
		
		DestroyIfEnemyCollision toReturn = new DestroyIfEnemyCollision(in_ship,collisionRadius,b_explodes);
		
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

		return false;
	}
	@Override
	public boolean handleDestroy(LogicEngine toRunIn) {
		if(b_explodes)
		{
			//spawn an explosion
			GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",thisObject.v.getPos().getX(),thisObject.v.getPos().getY(),0);
		
			explosion.stepHandlers.add(new FlyStraightStep(thisObject.v.getVel()));
			explosion.i_animationFrame =0;
			explosion.i_animationFrameSizeWidth =16;
			explosion.i_animationFrameSizeHeight =16;
			explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
			toRunIn.objectsOverlay.add(explosion);
			SoundEffects.getInstance().explosion.play(0.5f);
		}
		return true;
	}

}
