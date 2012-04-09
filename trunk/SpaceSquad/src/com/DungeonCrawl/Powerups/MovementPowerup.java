package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.badlogic.gdx.graphics.Color;

public class MovementPowerup implements Powerup{

	private float f_movementModifier;
	private Drawable d_buff;
	private Color c_debuffColor = null;
	private Color c_initialColor;
	
	public MovementPowerup(float in_movementModifier,Drawable in_buff)
	{
		f_movementModifier = in_movementModifier;
		d_buff = in_buff;
		
	}
	public MovementPowerup(float in_movementModifier, Color in_color) {
		c_debuffColor = in_color;
		f_movementModifier = in_movementModifier;
		
	}
	///Must only apply to players
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

		if(c_debuffColor != null)
		{
			//set colour and record initial colour so we can undo change
			c_initialColor = in_toApplyTo.c_Color;
			in_toApplyTo.c_Color = c_debuffColor;
		}
		
		//
		if(d_buff !=null)
			in_toApplyTo.visibleBuffs.add(d_buff);
		
		in_toApplyTo.v.setMaxVel(in_toApplyTo.v.getMaxVel()*f_movementModifier);
		in_toApplyTo.activePowerups.add(this);
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		
		if(c_debuffColor != null)
			in_toApplyTo.c_Color = c_initialColor;
		
		in_toApplyTo.activePowerups.remove(this);

		//slow it down again
		in_toApplyTo.v.setMaxVel(in_toApplyTo.v.getMaxVel()/f_movementModifier);
		
		if(d_buff !=null)
			in_toApplyTo.visibleBuffs.remove(d_buff);
		
	}
	@Override
	public void collected() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object clone()
	{
		MovementPowerup toReturn = new MovementPowerup(f_movementModifier,d_buff);
		toReturn.c_debuffColor = this.c_debuffColor;
		return toReturn;
		
	
	}
}
