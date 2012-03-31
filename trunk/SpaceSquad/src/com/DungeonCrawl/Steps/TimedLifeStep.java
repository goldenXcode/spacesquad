package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public class TimedLifeStep implements StepHandler{

	int i_lifeRemaining;
	public GameObject explosion;
	
	public TimedLifeStep(int in_life)
	{
		i_lifeRemaining = in_life;
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		if(--i_lifeRemaining==0)
		{
			if(explosion != null)
			{
				explosion.v.setX( o_runningOn.v.getX());
				explosion.v.setY( o_runningOn.v.getY());
				
				in_theLogicEngine.objectsOverlay.add(explosion);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		TimedLifeStep toReturn = new TimedLifeStep(i_lifeRemaining);
		
		if(explosion != null)
			toReturn.explosion = explosion.clone();
		
		return toReturn;
		
	}

}
