package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Utils;

import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.behaviors.Containment;
import de.steeringbehaviors.simulation.behaviors.Seek;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;

public class WanderStep implements StepHandler{

	Wander wander = null;
	Arrive arriveCenter = null;	
	
	public WanderStep()
	{
		//public Wander(double x, double y, double r, double influence)
		wander = new Wander(-2.5,-2.5,20,1);
		arriveCenter = new Arrive();
		
		arriveCenter.setTargetXY(LogicEngine.rect_Screen.getWidth()/2,LogicEngine.rect_Screen.getHeight()/1.5);
	}
	
	
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,GameObject o_runningOn) {

		
		//seek enemy player
		
		//TODO could calculate this only every x steps

	
		
		o_runningOn.v.addForce(arriveCenter.calculate(o_runningOn.v));
		//o_runningOn.v.addForce(wander.calculate(o_runningOn.v));
		
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
