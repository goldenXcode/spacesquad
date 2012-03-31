package com.DungeonCrawl.Steps;


import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;

public class GravitonDevice implements StepHandler {

	Drawable gravitonBuff;
	
	public GravitonDevice()
	{
		gravitonBuff = new Drawable();
		gravitonBuff.str_spritename = "data/"+GameRenderer.dpiFolder+"/warpin.png";
		gravitonBuff.i_animationFrame=0;
		gravitonBuff.i_animationFrameRow=1;
		gravitonBuff.i_animationFrameSizeHeight=32;
		gravitonBuff.i_animationFrameSizeWidth=32;
		
	}
	
	int i_buffLinger = 0;

	float f_gravitonRange=50;
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {


		//deflect enemies
		for(int i=0;i<in_theLogicEngine.objectsEnemies.size();i++)
		{
			//if its infront 
			if(o_runningOn.v.getPos().getY()<in_theLogicEngine.objectsEnemies.get(i).v.getY() )
			{
				Vector2d betweenObjects =in_theLogicEngine.objectsEnemies.get(i).v.getPos().sub(o_runningOn.v.getPos());
				
				if(betweenObjects.length() < f_gravitonRange && betweenObjects.length() > -f_gravitonRange)
				{
					betweenObjects.scale(0.1);
					
					//if its a boss reduce its effect
					if(in_theLogicEngine.objectsEnemies.get(i).isBoss)
						betweenObjects.scale(0.5);
					
					
					Vehicle target =in_theLogicEngine.objectsEnemies.get(i).v;
					target.setX(target.getX()+ betweenObjects.getX());
					target.setY(target.getY()+ betweenObjects.getY());
					i_buffLinger=3;
				}
			}
		}
		
		//deflect obstacles
		for(int i=0;i<in_theLogicEngine.objectsObstacles.size();i++)
		{
			//if its infront 
			if(o_runningOn.v.getPos().getY()<in_theLogicEngine.objectsObstacles.get(i).v.getY() )
			{
				
				Vector2d betweenObjects = in_theLogicEngine.objectsObstacles.get(i).v.getPos().sub(o_runningOn.v.getPos());
				
				if(betweenObjects.length() < f_gravitonRange && betweenObjects.length() > -f_gravitonRange)
				{
					betweenObjects.scale(0.1);
					
				
					Vehicle target =in_theLogicEngine.objectsObstacles.get(i).v;
					target.setX(target.getX()+ betweenObjects.getX());
					target.setY(target.getY()+ betweenObjects.getY());
									
					i_buffLinger=3;
				
				}
			}
		}
		
		i_buffLinger--;
		
		if(i_buffLinger==0)
		{
			o_runningOn.visibleBuffs.remove(gravitonBuff);
		}
		
		//if buff has changed to visible from not visible
		if( i_buffLinger>0)
		{
			o_runningOn.visibleBuffs.remove(gravitonBuff);
			o_runningOn.visibleBuffs.add(gravitonBuff);
		}
			
		
		return false;
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

}
