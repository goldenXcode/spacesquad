package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public class StaticAnimationStep implements StepHandler {

	int i_stepCounter;
	int i_numberOfAnimationFrames;
	int i_changeEverySteps;
	int i_startingFrame;
	
	public StaticAnimationStep(int in_numberOfAnimationFrames, int in_changeEverySteps, int in_startingFrame)
	{
		i_numberOfAnimationFrames = in_numberOfAnimationFrames;
		i_changeEverySteps = in_changeEverySteps;
		i_startingFrame = in_startingFrame;
		i_stepCounter=0;
		
	}
	@Override
	public boolean handleStep(LogicEngine toRunIn,GameObject o_runningOn) {
		

		
		//record current step counter in param 3 because its not used
		i_stepCounter++;
		
		
		//set frame to current timer - start timer divided by change every x and cast to int to remove fractions
		int i_newFrame = i_stepCounter/ i_changeEverySteps;
		
		i_newFrame += i_startingFrame;
		//if this goes beyond the last frame
		if(i_newFrame < i_numberOfAnimationFrames + i_startingFrame)
			o_runningOn.i_animationFrame = i_newFrame;
		else
			return true;
		
		
		return false;
		// TODO Auto-generated method stub
		
	}
	@Override
	public StepHandler clone() 
	{
		StaticAnimationStep s = new StaticAnimationStep(i_numberOfAnimationFrames,i_changeEverySteps,i_startingFrame);
		
		return s;
	}
}
