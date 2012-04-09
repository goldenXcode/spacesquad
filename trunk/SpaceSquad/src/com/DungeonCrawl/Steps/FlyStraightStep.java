package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class FlyStraightStep implements StepHandler {

	Vector2d velocity;
	Vector2d currentVelocity;
	boolean b_isAccelleration = false;
	
	public FlyStraightStep(Vector2d in_velocity)
	{
		velocity = in_velocity;
	}
	
	public void setIsAccelleration(boolean in_isAccelleration)
	{
		b_isAccelleration = in_isAccelleration;
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine, GameObject o_runningOn) {
		
		
		currentVelocity = new Vector2d(
				velocity.getX(),
				velocity.getY()
				);
				
		if(currentVelocity.length()>1)
			currentVelocity.normalize();
		
		currentVelocity.scale(Math.min(velocity.length(), o_runningOn.v.getMaxVel()));
		
		
		
		if(b_isAccelleration)
			o_runningOn.v.addForce(currentVelocity);
		else
		{
			o_runningOn.v.setVel(currentVelocity);
			o_runningOn.v.setX(o_runningOn.v.getX()+currentVelocity.getX());
			o_runningOn.v.setY(o_runningOn.v.getY()+currentVelocity.getY());
			
			//();
			//o_runningOn.v.setY(o_runningOn.v.getY()+velocity.getY());
		}
		//if offscreen add to offscreen counter
		if(LogicEngine.rect_Screen.inRect(o_runningOn.v.getPos())==false)
			o_runningOn.i_offScreenCounter++;
		else
			o_runningOn.i_offScreenCounter=0;
		
		return false;
	}

	public void setXY(int x, float y) {
		velocity.setX(x);
		velocity.setY(y);
		//TODO calling this is likely to result in bullets going slower than intended due to maxvel of vehicle
		
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		FlyStraightStep s = new FlyStraightStep(velocity);
		s.b_isAccelleration = b_isAccelleration;
		
		
		return s;
	}

}
