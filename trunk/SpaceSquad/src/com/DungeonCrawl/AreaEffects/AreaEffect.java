package com.DungeonCrawl.AreaEffects;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect.Effect;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.renderer.Rect;


public class AreaEffect extends Drawable{
	
	public Color c_Color = Color.RED;
	public Rect area;
	
	public Effect effect;
	protected int i_duration;
	protected boolean b_hasDuration=false;
	
	
	public AreaEffect(Rect in_area,Drawable in_d)
	{
		area = in_area;
		
		this.i_animationFrame = in_d.i_animationFrame;
		this.i_animationFrameRow = in_d.i_animationFrameRow;
		this.i_animationFrameSizeHeight = in_d.i_animationFrameSizeHeight;
		this.i_animationFrameSizeWidth = in_d.i_animationFrameSizeWidth;
		this.str_spritename = in_d.str_spritename;
	}
	public void stepEffect(LogicEngine in_logicEngine)
	{
	

		//if it expires after x steps
		if(b_hasDuration)
			if(i_duration-- ==0)
			{
				in_logicEngine.currentAreaEffects.remove(this);
				return;
			}
		
	}
}
