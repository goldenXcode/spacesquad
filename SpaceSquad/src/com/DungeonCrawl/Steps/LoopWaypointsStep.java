package com.DungeonCrawl.Steps;

import java.util.ArrayList;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class LoopWaypointsStep implements StepHandler{
	
	Behavior behaviour;
	
	public ArrayList<Point2d> waypoints = new ArrayList<Point2d>();
	
	int i_currentWaypoint=0;
	public boolean b_flipImage=true;

	
	
	public LoopWaypointsStep()
	{
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "50", null);
		
		behaviour = followPathBehaviour;
	}
	
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		//if they have arrived
		if(o_runningOn.v.getPath().size() <= 1 && waypoints.size() > 0)
		{
			//add a new waypoint
			o_runningOn.v.addWaypoint(waypoints.get(i_currentWaypoint));
			
			//circular increment
			i_currentWaypoint = (i_currentWaypoint+1) % waypoints.size();
			
			//add a new waypoint
			o_runningOn.v.addWaypoint(waypoints.get(i_currentWaypoint));
			o_runningOn.i_offScreenCounter=0;
			
			if(b_flipImage)
				o_runningOn.b_mirrorImageHorizontal = !o_runningOn.b_mirrorImageHorizontal;
		}
		
		Vector2d vector = behaviour.calculate(o_runningOn.v);
		
		o_runningOn.v.addForce(vector);
		
		if(LogicEngine.rect_Screen.inRect(o_runningOn.v.getPos())==false)
			o_runningOn.i_offScreenCounter++;
		else
			o_runningOn.i_offScreenCounter=0;
		
		return false;
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
		//CustomBehaviourStep c = new CustomBehaviourStep(behaviour);
		
	//	return c;
	}

}
