package com.DungeonCrawl.Collisions;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Powerups.Powerup;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StepHandler;

public class PowerupCollision implements CollisionHandler {

	Powerup forPowerup;
	boolean b_destructable=false;
	GameObject thisObject;
	boolean b_applyToAllShips;
	
	public PowerupCollision(Powerup in_powerup, boolean in_applyToAllShips)
	{
		forPowerup = in_powerup;
		b_applyToAllShips = in_applyToAllShips;
	}
	
	public PowerupCollision(Powerup in_powerup , boolean in_applyToAllShips, boolean in_destructable, GameObject in_gameObject)
	{
		forPowerup = in_powerup;
		b_destructable = in_destructable;
		thisObject = in_gameObject;
		b_applyToAllShips = in_applyToAllShips;
	}
	
	@Override
	public boolean handleCollision(GameObject collidingWith, LogicEngine toRunIn) {

		//if collected by player and NOT a player bullet
		if(collidingWith.allegiance == GameObject.ALLEGIANCES.PLAYER && toRunIn.objectsPlayers.indexOf(collidingWith)!=-1)
		{
			forPowerup.collected();
			
			//powerup all player ships
			if(b_applyToAllShips)
				for(int i=0;i<toRunIn.objectsPlayers.size();i++)
				{
					//if its an actual player
					if(toRunIn.objectsPlayers.get(i).collisionHandler instanceof PlayerCollision)
						forPowerup.applyPowerup(toRunIn.objectsPlayers.get(i),toRunIn);
				}
			else
				forPowerup.applyPowerup(collidingWith,toRunIn);
			
			return true;
		}
		else
		{
			//was not a player so see if it is something that can destroy us
			
			//if its destructable and hit an enemy
			if(b_destructable && collidingWith.allegiance != thisObject.allegiance)
			{
				//spawn explosion
				//spawn an explosion
				GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",thisObject.v.getPos().getX(),thisObject.v.getPos().getY(),0);
			
				explosion.i_animationFrame =0;
				explosion.i_animationFrameSizeWidth =16;
				explosion.i_animationFrameSizeHeight =16;
				explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
				toRunIn.objectsOverlay.add(explosion);
				SoundEffects.getInstance().explosion.play();
				
				return true;
			}
		}
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getCollisionRadius() {
		// TODO Auto-generated method stub
		return 25.0;
	}

	@Override
	public CollisionHandler cloneForShip(GameObject in_ship)
			throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleDestroy(LogicEngine toRunIn) {
		if(b_destructable) 
		{
			//spawn explosion
			//spawn an explosion
			GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",thisObject.v.getPos().getX(),thisObject.v.getPos().getY(),0);
		
			explosion.i_animationFrame =0;
			explosion.i_animationFrameSizeWidth =16;
			explosion.i_animationFrameSizeHeight =16;
			explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
			toRunIn.objectsOverlay.add(explosion);
			SoundEffects.getInstance().explosion.play();
		}
		return true;
	}

}
