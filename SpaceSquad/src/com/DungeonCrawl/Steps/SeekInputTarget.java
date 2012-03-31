package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class SeekInputTarget implements StepHandler
{
	int i_positionInFleetOffset=0;
	Arrive behaviourArrive = new Arrive(0,0,10,1);

	private float f_xOffset =0;
	private float f_yOffset=0;
	
	
	public SeekInputTarget(float in_xOffset, float in_yOffset, boolean in_followTarget2)
	{
		if(in_followTarget2 == false)
			i_positionInFleetOffset = 0;
		else
			i_positionInFleetOffset = 2; 
	
		f_yOffset = in_yOffset;
		f_xOffset = in_xOffset;
		
	}
	
	public SeekInputTarget(int in_positionInFleetOffset)
	{
		
		i_positionInFleetOffset = in_positionInFleetOffset;
	}
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {

		//if there is at least one target
		if(in_theLogicEngine.MyInputProcessor.targetsActive[0]!=false || in_theLogicEngine.MyInputProcessor.targetsActive[1]!=false)
		{
			
			//if there is only one target
			if(in_theLogicEngine.MyInputProcessor.targetsActive[0]==true ^ in_theLogicEngine.MyInputProcessor.targetsActive[1]==true)
			{
				int i_follow = 0;
				if(in_theLogicEngine.MyInputProcessor.targetsActive[1]==true)
				i_follow=1;
				
				//even ships follow target 0, odd ships follow target 1
				switch(i_positionInFleetOffset)
				{
				case 0:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[i_follow].x+ f_xOffset,
						in_theLogicEngine.MyInputProcessor.Targets[i_follow].y+ f_yOffset);
				break;
				case 1:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[i_follow].x -25,
							in_theLogicEngine.MyInputProcessor.Targets[i_follow].y -25);
					break;
				case 2:
					if(f_yOffset != 0)
						behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[i_follow].x+ f_xOffset,
								in_theLogicEngine.MyInputProcessor.Targets[i_follow].y + f_yOffset);
					else
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[i_follow].x+ f_xOffset,
							in_theLogicEngine.MyInputProcessor.Targets[i_follow].y -25 + f_yOffset);
					break;
				case 3:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[i_follow].x +25,
							in_theLogicEngine.MyInputProcessor.Targets[i_follow].y -25);
					break;
				}
			}
			else
			{
				//distribute between the two targets
				//0 and 1 follow target 0, 2 and 3 ships follow target 1
				switch(i_positionInFleetOffset)
				{
				case 0:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[0].x + f_xOffset,
						in_theLogicEngine.MyInputProcessor.Targets[0].y + f_yOffset);
				break;
				case 1:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[0].x -25,
							in_theLogicEngine.MyInputProcessor.Targets[0].y -25);
					break;
				case 2:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[1].x + f_xOffset,
							in_theLogicEngine.MyInputProcessor.Targets[1].y + f_yOffset);
					break;
				case 3:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[1].x +25,
							in_theLogicEngine.MyInputProcessor.Targets[1].y -25);
					break;
				}
			}
	
			Vector2d movementVector = behaviourArrive.calculate(o_runningOn.v);
				
			o_runningOn.move(movementVector);
			
		}
		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
}
