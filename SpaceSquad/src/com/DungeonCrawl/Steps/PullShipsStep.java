package com.DungeonCrawl.Steps;

import java.util.ArrayList;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;

public class PullShipsStep implements StepHandler{

	float f_range;
	float f_pullAmount;
	
	boolean b_pullAll;
	
	public PullShipsStep(float in_range, float in_pullAmount,boolean in_pullAll)
	{
		f_range = in_range;
		f_pullAmount = in_pullAmount;
		b_pullAll = in_pullAll;
		
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {

		if(b_pullAll)
		{
			pullInArray(in_theLogicEngine,o_runningOn,in_theLogicEngine.objectsPlayers);
			pullInArray(in_theLogicEngine,o_runningOn,in_theLogicEngine.objectsPlayerBullets);
			pullInArray(in_theLogicEngine,o_runningOn,in_theLogicEngine.objectsEnemies);
			pullInArray(in_theLogicEngine,o_runningOn,in_theLogicEngine.objectsObstacles);
		}
		else
			pullInArray(in_theLogicEngine,o_runningOn,in_theLogicEngine.objectsPlayers);
	
		
		return false;
	}

	private void pullInArray(LogicEngine in_theLogicEngine,
			GameObject o_runningOn, ArrayList<GameObject> in_array)
	{
				
		//pull players
		for(int i=0;i<in_array.size();i++)
		{
		
			Vector2d betweenObjects =in_array.get(i).v.getPos().sub(o_runningOn.v.getPos());
			
			if(betweenObjects.length() < f_range && betweenObjects.length() > -f_range)
			{
				//pull is proportional to distance
				//scale to length - effective range
				double range = Math.abs(betweenObjects.length())-f_range;
				
				betweenObjects.scale(f_pullAmount* range);
				
				Vehicle target =in_array.get(i).v;
		
				target.addForce(new Vector2d(betweenObjects.getX(), betweenObjects.getY()));
				
				
				
			}
			
		}
		
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	
}
