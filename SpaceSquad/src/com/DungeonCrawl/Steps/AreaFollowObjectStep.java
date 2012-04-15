package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.AreaEffects.AreaEffect;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;

import de.steeringbehaviors.simulation.renderer.Point2d;

public class AreaFollowObjectStep implements StepHandler 
{
	AreaEffect ae_AreaEffect;
	GameObject attachToObject;
	
	float f_halfAreaWidth;
	float f_halfAreaHeight;

	public AreaFollowObjectStep(AreaEffect in_area , GameObject in_attachToObject)
	{
		ae_AreaEffect = in_area;
		
		attachToObject = in_attachToObject;
	}
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {

		f_halfAreaWidth = (float) ae_AreaEffect.area.getWidth()/2;
		f_halfAreaHeight = (float) ae_AreaEffect.area.getHeight()/2;
		
		
		ae_AreaEffect.area.setp1(new Point2d(o_runningOn.v.getX() - f_halfAreaWidth,o_runningOn.v.getY() + f_halfAreaHeight));
		ae_AreaEffect.area.setp2(new Point2d(o_runningOn.v.getX() + f_halfAreaWidth,o_runningOn.v.getY() - f_halfAreaHeight));
		
		return false;
	}	
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
