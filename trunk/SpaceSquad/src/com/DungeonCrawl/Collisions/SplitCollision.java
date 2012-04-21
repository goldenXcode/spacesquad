package com.DungeonCrawl.Collisions;

import java.util.ArrayList;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Steps.FlyStraightStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class SplitCollision extends HitpointShipCollision
{

	public SplitCollision(GameObject in_for, int in_numberOfHitpoints,
			double in_collisionRadius) {
		super(in_for, in_numberOfHitpoints, in_collisionRadius);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<GameObject> splitObjects = new ArrayList<GameObject>(); 

	@Override
	public boolean handleCollision(GameObject collidingWith, LogicEngine toRunIn) {
		if(	c_initialColour ==null)
			 c_initialColour = thisObject.c_Color;
		
		//remove flash if we are to support flashing AND the current frame is a flash frame AND at least 1 step has happened since the flash was set
		if(b_flash &&  thisObject.i_animationFrame == i_flashFrame &&  toRunIn.i_stepCounter != i_flashStartedOnStep)
		{
			thisObject.i_animationFrame = i_oldFrameBeforeFlash;
			
		}
		
		
		//if it's not lethal collision or a collision with a non friendly 
		if(collidingWith.allegiance == GameObject.ALLEGIANCES.LETHAL || (collidingWith.allegiance!= GameObject.ALLEGIANCES.NONE && thisObject.allegiance != collidingWith.allegiance ))
		{
			//if colliding with another hp ship
			if(collidingWith.collisionHandler instanceof HitpointShipCollision)
				f_numberOfHitpoints -= Math.max(0,((HitpointShipCollision)collidingWith.collisionHandler).f_numberOfHitpoints);
			else
			{
				f_numberOfHitpoints--;
				
				//if theres a health bar reduce its size
				if(go_healthBar != null)
				{
					go_healthBar.f_forceScaleX = (float)f_numberOfHitpoints/(float)f_startingHitpoints;
					go_healthBar.f_forceScaleY = 1f;
				}
			}
			
			
			//if graphics flash supported by this collision 
			if(b_flash)
			{
				//if not already flashing
				if(thisObject.i_animationFrame != i_flashFrame)
				{
					i_oldFrameBeforeFlash = thisObject.i_animationFrame;
					thisObject.i_animationFrame = i_flashFrame;
					i_flashStartedOnStep = toRunIn.i_stepCounter;
				}
			}
			else
			{
				i_flashCounter = i_flashDuration;
				thisObject.c_Color = this.c_flashColor;
			}
			
			if(f_numberOfHitpoints <=0)
			{
				return handleDestroy(toRunIn);
			}
			
			
			
		}
		// TODO Auto-generated method stub
		return false;
		
	}
	
	
	@Override
	public SplitCollision cloneForShip(GameObject in_ship) {
		
		SplitCollision returnVal = new SplitCollision(in_ship, (int)f_numberOfHitpoints, collisionRadius);
		
		for(int i=0 ; i< this.splitObjects.size();i++)
			returnVal.splitObjects.add(this.splitObjects.get(i).clone());
		
		returnVal.c_flashColor = this.c_flashColor;
		returnVal.c_initialColour = this.c_initialColour;
		returnVal.i_flashDuration = this.i_flashDuration;
		
		
		returnVal.setSimpleExplosion();
		
		return returnVal;
		
	}
	
	@Override
	public boolean handleDestroy(LogicEngine toRunIn) {
		
		super.handleDestroy(toRunIn);
				
		//split
		for(int i=0;i<splitObjects.size();i++)
		{
			splitObjects.get(i).v.setX(thisObject.v.getX());
			splitObjects.get(i).v.setY(thisObject.v.getY());
			toRunIn.toAddObjectsEnemies.add(splitObjects.get(i));
		}
		
		// TODO Auto-generated method stub
		return true;
	}

}
