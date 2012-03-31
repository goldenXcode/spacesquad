package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public class MovementPowerup implements Powerup{

	private float f_movementModifier;
	private Drawable d_buff;
	
	public MovementPowerup(float in_movementModifier,Drawable in_buff)
	{
		f_movementModifier = in_movementModifier;
		d_buff = in_buff;
		
	}
	///Must only apply to players
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

		//if it is an odd number integer division will drop 
		if(d_buff !=null)
			in_toApplyTo.visibleBuffs.add(d_buff);
		
		in_toApplyTo.v.setMaxVel(in_toApplyTo.v.getMaxVel()*f_movementModifier);
		in_toApplyTo.activePowerups.add(this);
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		
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

}
