package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public class AnimateRollStep implements StepHandler{

	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		o_runningOn.i_animationFrame=0;
		
		
		if(o_runningOn.v.getVel().getX()<-2)
			o_runningOn.i_animationFrame=7;
		
		if(o_runningOn.v.getVel().getX() <-4)
			o_runningOn.i_animationFrame=6;
		
		//adjust frame if vel high
		if(o_runningOn.v.getVel().getX()>2)
			o_runningOn.i_animationFrame=4;
		
		if(o_runningOn.v.getVel().getX()>4)
			o_runningOn.i_animationFrame=5;
		
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

}
