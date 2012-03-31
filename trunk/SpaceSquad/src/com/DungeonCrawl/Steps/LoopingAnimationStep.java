package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public class LoopingAnimationStep implements StepHandler {

	int i_stepCounter;
	int i_changeEverySteps;
	int i_numberOfFramesToLoop;
	
	public LoopingAnimationStep(int in_changeEverySteps, int in_numberOfFramesToLoop)
	{
		i_changeEverySteps = in_changeEverySteps;
		i_numberOfFramesToLoop = in_numberOfFramesToLoop;
	}
	@Override
	public boolean handleStep(LogicEngine toRunIn,GameObject o_runningOn) {
		
		
		//record current step counter in param 3 because its not used
		i_stepCounter++;
		
		
		//set frame to current timer - start timer divided by change every x and cast to int to remove fractions
		int i_newFrame = i_stepCounter/i_changeEverySteps;
		
		
		
		if( i_newFrame >= i_numberOfFramesToLoop)
		{
			i_newFrame=0;
			i_stepCounter=0;
		}
		
		//set animation frame
		o_runningOn.i_animationFrame = i_newFrame;
		
		return false;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
