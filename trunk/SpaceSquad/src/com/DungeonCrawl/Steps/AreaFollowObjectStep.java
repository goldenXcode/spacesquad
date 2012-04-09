package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.AreaEffects.AreaEffect;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;

import de.steeringbehaviors.simulation.renderer.Point2d;

public class AreaFollowObjectStep implements StepHandler 
{
	AreaEffect area;
	GameObject attachToObject;
	
	float f_halfAreaWidth;
	float f_halfAreaHeight;

	public AreaFollowObjectStep(AreaEffect in_area , GameObject in_attachToObject)
	{
		area = in_area;
		f_halfAreaWidth = (float) in_area.area.getWidth()/2;
		f_halfAreaHeight = (float) in_area.area.getHeight()/2;
		
		attachToObject = in_attachToObject;
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {

		area.area.setp1(new Point2d(o_runningOn.v.getX() - f_halfAreaWidth,o_runningOn.v.getY() + f_halfAreaHeight));
		area.area.setp2(new Point2d(o_runningOn.v.getX() + f_halfAreaWidth,o_runningOn.v.getY() - f_halfAreaHeight));
		
		return false;
	}	
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
