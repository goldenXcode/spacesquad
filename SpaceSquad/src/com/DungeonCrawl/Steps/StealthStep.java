package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Collisions.CollisionHandler;

public class StealthStep implements StepHandler
{
	int i_durationToBeStealthed;
	int i_timeToFade;
	
	boolean b_isStealthed = false;
	int i_stealthCounter=0;
	
	
	CollisionHandler collisionHolder=null;
	
	public StealthStep(int in_durationToBeStealthed, int in_timeToFade)
	{
		i_durationToBeStealthed = in_durationToBeStealthed;
		i_timeToFade = in_timeToFade;
		
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {

		
		//if visible
		if(!b_isStealthed)
			if(i_stealthCounter < i_timeToFade)
			{	
				//animate fade in
				if(i_stealthCounter <= 10)  
					o_runningOn.c_Color.a = (i_stealthCounter/10f) ;
				
				//animate fade out
				if(i_timeToFade - i_stealthCounter <=10)  
					o_runningOn.c_Color.a = (i_timeToFade - i_stealthCounter)/10f ;
				
				o_runningOn.c_Color.a = Math.max(o_runningOn.c_Color.a, 0.2f);
				
				i_stealthCounter++;
			}
			else
			{
				//hide
				collisionHolder = o_runningOn.collisionHandler;
				o_runningOn.collisionHandler = null;
				i_stealthCounter = i_durationToBeStealthed;
				b_isStealthed = true;
				o_runningOn.c_Color.a = 0.2f;
			}
			
		else
			if(i_stealthCounter >0)
			{			
				
				i_stealthCounter--;
			}
			else
			{
				o_runningOn.collisionHandler = collisionHolder;
				b_isStealthed = false;
			}
			
			
		
		
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
