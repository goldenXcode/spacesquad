package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class CustomBehaviourStep implements StepHandler{

	Behavior behaviour;
	
	
	public CustomBehaviourStep(Behavior in_behaviour)
	{
		behaviour = in_behaviour;
		
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		Vector2d vector = behaviour.calculate(o_runningOn.v);
		
		o_runningOn.v.addForce(vector);
		
		//if offscreen add to offscreen counter
		if(LogicEngine.rect_Screen.inRect(o_runningOn.v.getPos())==false)
			o_runningOn.i_offScreenCounter++;
		
		return false;
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		CustomBehaviourStep c = new CustomBehaviourStep(behaviour);
		
		return c;
	}
}
