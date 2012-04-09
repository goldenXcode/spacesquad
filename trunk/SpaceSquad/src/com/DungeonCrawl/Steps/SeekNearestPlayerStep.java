package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Utils;

import de.steeringbehaviors.simulation.behaviors.Seek;

public class SeekNearestPlayerStep implements StepHandler{

	
	Seek seek = null;
	double d_distanceToStartSeeking=0;
	
	public SeekNearestPlayerStep(double in_distanceToStartSeeking)
	{
		d_distanceToStartSeeking = in_distanceToStartSeeking;
		seek = new Seek();
		seek.setActiveDistance(in_distanceToStartSeeking);

	}
	
	int i_stepCounter=0;
	int i_playerToSeek;
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,GameObject o_runningOn) {

		
		//seek enemy player
		i_stepCounter++;
		
		if(i_stepCounter%3 ==0)
		{
			i_playerToSeek =Utils.getClosestPlayerShip(in_theLogicEngine,o_runningOn.v);
			//TODO could calculate this only every x steps
			seek.setTarget(in_theLogicEngine.objectsPlayers.get(i_playerToSeek).v);	
		}
		
		o_runningOn.v.addForce(seek.calculate(o_runningOn.v));

		if(LogicEngine.rect_Screen.inRect(o_runningOn.v.getPos())==false)
			o_runningOn.i_offScreenCounter++;
		else
			o_runningOn.i_offScreenCounter=0;
		
		if(o_runningOn.i_offScreenCounter >50 && !o_runningOn.isBoss)
			return true;
		
		
		return false;
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		SeekNearestPlayerStep s = new SeekNearestPlayerStep(d_distanceToStartSeeking);
		
		return s;
	}
}
