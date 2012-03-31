package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class FlyStraightStep implements StepHandler {

	Vector2d velocity;
	int i_offScreenCounter=0;
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
		
		if(b_isAccelleration)
			o_runningOn.v.addForce(velocity);
		else
		{
			o_runningOn.v.setVel(velocity);
			o_runningOn.v.setX(o_runningOn.v.getX()+velocity.getX());
			o_runningOn.v.setY(o_runningOn.v.getY()+velocity.getY());
			
			//();
			//o_runningOn.v.setY(o_runningOn.v.getY()+velocity.getY());
		}
		//if offscreen add to offscreen counter
		if(LogicEngine.rect_Screen.inRect(o_runningOn.v.getPos())==false)
			i_offScreenCounter++;
		else
			i_offScreenCounter=0;

		if(i_offScreenCounter >30 && !o_runningOn.isBoss)
			return true;
		else
			// TODO Auto-generated method stub
			return false;
	}

	public void setXY(int x, float y) {
		velocity.setX(x);
		velocity.setY(y);
		
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		FlyStraightStep s = new FlyStraightStep(velocity);
		s.b_isAccelleration = b_isAccelleration;
		s.i_offScreenCounter=i_offScreenCounter;
		
		return s;
	}

}
