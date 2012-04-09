package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Powerups.Powerup;

public class TimedLifePowerupStep implements StepHandler{
	int i_lifeRemaining;
	
	Powerup thePowerup;
	
	public TimedLifePowerupStep(int in_life, Powerup in_powerup)
	{
		thePowerup = in_powerup;
		i_lifeRemaining = in_life;
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		if(--i_lifeRemaining==0)
		{
			//unapply powerup
			thePowerup.unApplyPowerup(o_runningOn);
			o_runningOn.stepHandlers.remove(this);
		}
		
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		//TODO: warning this is a reference to the original powerup not a clone but shouldnt matter in most cases
		TimedLifePowerupStep toReturn = new TimedLifePowerupStep(i_lifeRemaining,thePowerup);
		
		
		return toReturn;
		
	}
}
