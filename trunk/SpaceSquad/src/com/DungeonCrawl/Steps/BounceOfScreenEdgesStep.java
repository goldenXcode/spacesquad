package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;

public class BounceOfScreenEdgesStep implements StepHandler {

	public boolean b_sidesOnly = false;
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {

		Vehicle v = o_runningOn.v;
		Vector2d vel = v.getVel();
		
		if(v.getX() < 1)
		{
			vel.setX(Math.abs(v.getX())); //make positive
			
			//hack to prevent balls bouncing back and forth on bottom
			if(Math.abs(vel.getY()) < 0.2f)
				vel.setY(1f * Math.random());
		}
		
		if(v.getX() > LogicEngine.SCREEN_WIDTH - 1)
		{
			vel.setX(Math.abs(v.getX()) * -1); //make negative
			
			//hack to prevent balls bouncing back and forth on bottom
			if(Math.abs(vel.getY()) < 0.2f)
				vel.setY(1f * Math.random());
		}
		
		if(!b_sidesOnly)
		{
			if(v.getY() < -1)
			{
				vel.setY(Math.abs(v.getY()) * 1); //make negative
	
				//hack to prevent balls bouncing back and forth on bottom
				if(Math.abs(vel.getX()) < 0.2f)
					vel.setX(1f * Math.random());
			}
			if(v.getY() > LogicEngine.SCREEN_HEIGHT - 1)
			{
				vel.setY(Math.abs(v.getY()) * -1); //make negative
				
				//hack to prevent balls bouncing back and forth on bottom
				if(Math.abs(vel.getX()) < 0.2f)
					vel.setX(1f * Math.random());
			}
			
		}
		//advance it along
		v.addForce(v.getVel());
		
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		BounceOfScreenEdgesStep b = new BounceOfScreenEdgesStep();
		b.b_sidesOnly = this.b_sidesOnly;
		
		return b;
	}

}
